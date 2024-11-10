package pl.dev4lazy.shit_exchange.model.listing;

import pl.dev4lazy.shit_exchange.utils.csv_service.Decoder;
import pl.dev4lazy.shit_exchange.utils.values.Value;
import pl.dev4lazy.shit_exchange.model.DataGrip;
import pl.dev4lazy.shit_exchange.model.ToolsGrip;
import pl.dev4lazy.shit_exchange.utils.csv_service.CsvFile;
import pl.dev4lazy.shit_exchange.utils.csv_service.CsvLineDecodingResult;
import pl.dev4lazy.shit_exchange.utils.csv_service.CsvLineToMappedItemDecoder;
import pl.dev4lazy.shit_exchange.utils.csv_service.CsvUtils;
import pl.dev4lazy.shit_exchange.utils.problem_handling.AppError;
import pl.dev4lazy.shit_exchange.utils.problem_handling.ConsoleLogger;
import pl.dev4lazy.shit_exchange.utils.problem_handling.CsvFileInvalidDataProblem;
import pl.dev4lazy.shit_exchange.utils.problem_handling.CsvFileMissingDataProblem;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

public class ListingDataOperator {

    private final ListingData listing;
    private CsvFile csvFile;
    private Decoder csvLineToMappedItemDecoder;

    public ListingDataOperator() {
        listing = DataGrip.getInstance().getListingData();
        try {
            csvFile = new ListingCsvFile(
                    DataGrip.getInputListingFileNameFullPath(),
                    Charset.forName("Windows-1250"),
                    CsvUtils.CSV_SEPARATOR,
                    CsvUtils.HAS_HEADER );
            csvLineToMappedItemDecoder = new CsvLineToMappedItemDecoder(csvFile);
        } catch (Exception e) {
            // todo: wpływ na dalsze dzialanie programu można byz zrobic tak, że:
            // AppError appError = new AppError( e ); albo w zależności od sytuacji new BreakProcessError
            // ToolsGrip.handleError( appError );
            // w ErrorHandlerze podejmowana byłaby decyzja (sam ErrorHandler, lub w interakcji z użytkownikiem) co dalej
            // W appError ustawiana byłaby flaga informaująca co dalej
            // Po powrocie z ToolsGrip.handleError( appError ) flaga byłaby sprawdzana i wg niej ustawiana byłaby flaga stanu
            // obiktu w którym wywołano ToolsGrip.handleError()
            ToolsGrip.handleError( new AppError( e ));
        }
    }

    public void readAllStoresListingData(NeededListingValuesMapped2ListingItemConverter converter ) {
        // TODO przy powtórnym wywołaniu musi być wyzerowanie danych
        int csvLineNumber = 0;
        ArrayList<String> parsedCsvLine;
        CsvLineDecodingResult listingCsvLineDecodingResult;
        HashMap<String, Value> validatedCsvLineDecodedValues;
        NeededListingValuesMapped neededListingValuesMapped;
        ListingItem listingItem;
        ConsoleLogger consoleLogger = new ConsoleLogger();
        try {
            csvFile.openForReading();
            if (csvFile.hasHeader()) {
                csvFile.skipRow();
                csvLineNumber++;
            }
            while ((parsedCsvLine = csvFile.readParsedDataFromCsvLine()) != null) {
                csvLineNumber++;
                // TODO start
                if ((csvLineNumber % 100) == 0) {
                    System.out.println(csvLineNumber);
                }
                // TODO end
                // ##### dekodowanie wiersza w mapę nagłówek->wartość #####
                listingCsvLineDecodingResult =
                        (CsvLineDecodingResult)csvLineToMappedItemDecoder.decode( parsedCsvLine );
                // ##### walidacja mapy nagłówek->wartość zdekodowanego wiersza #####
                validatedCsvLineDecodedValues =
                        validateCsvLineDecodedValues( listingCsvLineDecodingResult.getDecodingResultMapped() );
                if (validatedCsvLineDecodedValues==null) {
                    // todo: może jakaś rejestracja błędnych wierszy
                    ToolsGrip.handleProblem(
                            new CsvFileInvalidDataProblem(
                                    consoleLogger,
                                    "Błąd wartości w wierszu",
                                    csvFile,
                                    csvLineNumber,
                                    parsedCsvLine,
                                    listingCsvLineDecodingResult.getHeadersForInvalidValues()
                            )
                    );
                    continue;
                }
                // ##### weryfikacja kompletności zdekodowanych danych przed konwersją na ListingItem
                neededListingValuesMapped =
                        getNeededListingValuesMapped( validatedCsvLineDecodedValues );
                if ( neededListingValuesMapped.hasNoCorrectAmountOfValues() ) {
                    // todo: może jakaś rejestracja błędnych wierszy
                    // todo: może wyświetlenie raportu podsumowującego?
                    ToolsGrip.handleProblem(
                            new CsvFileMissingDataProblem(
                                    consoleLogger,
                                    "Brak wartości w wierszu",
                                    csvFile,
                                    csvLineNumber,
                                    parsedCsvLine,
                                    neededListingValuesMapped
                            )
                    );
                    continue;
                }
                // ##### konwersja zdekodowanych danych na ListingItem
                listingItem = converter.convertNeededListingValuesMapped2ListingItem( neededListingValuesMapped );
                // ##### dodanie nowego ListingItem do mapy wszystkich LI
                listing.addListingItem( listingItem );
            }
            csvFile.close();
        } catch (Exception e) {
            ToolsGrip.handleError( new AppError( "Błąd w wierszu nr: "+csvLineNumber, e  ));
        }// todo finnalu z csvFile.close();
    }

    /**
     * Jeśli, któraś ze zmapowanych wartości lini csv nie spełnia reguł walidacji, to zwraca null.
     * Jest to sygnał, że w wierszu występuje wartość, której nie da sie prawidłowo (stosownie do oczekiwanego typu)
     * skonwertować.
     * @param csvLineDecoded
     * @return
     */
    private HashMap<String, Value> validateCsvLineDecodedValues(HashMap<String, Value> csvLineDecoded ) {
        for ( Value value: csvLineDecoded.values() ) {
            if (value.validationResult()==null) {
                return null;
            }
        }
        return csvLineDecoded;
    }

    private NeededListingValuesMapped getNeededListingValuesMapped( HashMap<String, Value> validatedListingCsvLineDecoded ) {
        NeededListingValuesMapped neededListingValuesMapped = new NeededListingValuesMapped();
        for ( String header : validatedListingCsvLineDecoded.keySet() ) {
            if (neededListingValuesMapped.isHeaderNeeded( header ) ) {
                neededListingValuesMapped.addValue( validatedListingCsvLineDecoded.get( header ), header );
            }
        }
        return neededListingValuesMapped;
    }

    public void saveListingObjects() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream( DataGrip.getSavedListingFileNameFullPath() )
        )) {
            oos.writeObject( listing.getStoresListingNodes() );
            ToolsGrip.handleNotification("Mapa listingu została zapisana do pliku.");
        } catch (IOException e) {
            ToolsGrip.handleError( new AppError( e ));
        }
    }

    public void readListingObjects() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream( DataGrip.getSavedListingFileNameFullPath() )
        )) {
            listing.setStoresListingNodes( ois.readObject() );
            ToolsGrip.handleNotification("Mapa listingu została odczytana z pliku.");
        } catch (IOException | ClassNotFoundException e) {
            ToolsGrip.handleError( new AppError( e ));
        }
    }


}
