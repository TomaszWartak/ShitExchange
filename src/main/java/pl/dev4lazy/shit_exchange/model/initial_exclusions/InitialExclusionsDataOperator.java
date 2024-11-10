package pl.dev4lazy.shit_exchange.model.initial_exclusions;

import pl.dev4lazy.shit_exchange.Config;
import pl.dev4lazy.shit_exchange.model.DataGrip;
import pl.dev4lazy.shit_exchange.model.ToolsGrip;
import pl.dev4lazy.shit_exchange.utils.KeyboardReader;
import pl.dev4lazy.shit_exchange.utils.csv_service.*;
import pl.dev4lazy.shit_exchange.utils.problem_handling.*;
import pl.dev4lazy.shit_exchange.utils.values.Value;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Odpowiada za odczytanie wyłączeń z pliku.
 * Jedynie udostępnia:
 *  1. listę wyłączonych artykułów dla każdego sklepu wymienionego w pliku wyłączeń
 *  2. listę id sklepów wyłączonych na podstawie pliku wyłączeń, oprócz one donor id
 * Po odczycie rezerwy z pliku
 * - na podstawie listy 1. ustawiane są wykluczenie pojedynczych oddawanych artykułów ResreveItem w sklepie OneDonor
 * - na podstawie listy 2. ustawianae są w ReserveDataForStore, że dane sklepu mają być wyłączone z tworzenia donacji
 *
 * NIE WSPIERA WYŁĄCZEN NA PÓZNIEJSZYM ETAPIE (przy kolejnych iteracjach generowania donacji, dla kolejnych biorców).
 *
 */
public class InitialExclusionsDataOperator implements ErrorsProne {

    private CsvFile csvFile;
    private Decoder csvLineToMappedItemDecoder;
    private final InitialExclusionsData initialExclusions;
    private final KeyboardReader keyboardReader = ToolsGrip.getKeyboardReader();
    private boolean errorOccurred = false;

    public InitialExclusionsDataOperator() {
        clearErrorOccurred();
        initialExclusions = DataGrip.getInstance().getInitialExclusionsData();
        csvFile = new CsvFile(
                DataGrip.getInputExclusionsFileNameFullPath(),
                Charset.forName("Windows-1250"),
                CsvUtils.CSV_SEPARATOR,
                true );
        if (csvFile.exists() ) {
            try {
                csvFile = new InitialExclusionsCsvFile( csvFile );
                csvLineToMappedItemDecoder = new CsvLineToMappedItemDecoder( csvFile );
            } catch ( Exception e ) {
                AppError appError = new AppError( e, AppError.INTERRUPTING_PROCESS );
                ToolsGrip.handleError( appError );
                // To poniżej to wprawka do ewewntualnej interakcji z użytkownikiem,
                // w kwestii podjęcia decyzji po pojawieniu się problemu.
                // Zob. saveReserveObjects()
                if (appError.isInterruptingProcess()) {
                    setErrorOccurred();
                }
            }
        }
    }

    public void readInitialExclusions( InitialExclusionValuesMapped2ExclusionItemConverter converter ) {
        // TODO przy powtórnym wywołaniu musi być wyzerowanie danych
        int csvLineNumber = 0;
        ArrayList<String> parsedCsvLine;
        CsvLineDecodingResult excludingsCsvLineDecodingResult;
        HashMap<String, Value> validatedCsvLineDecodedValues;
        InitialExclusionValuesMapped initialExclusionValuesMapped;
        InitialExclusionItem initialExclusionItem;
        // todo czy ConsoleLogger nie powinien byc jeden w aplikacji? zobacz poniżej (1), jak możnaby to rozwiązać
        ConsoleLogger consoleLogger = new ConsoleLogger();
        if (csvFile.exists() ) {
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
                    excludingsCsvLineDecodingResult =
                            (CsvLineDecodingResult) csvLineToMappedItemDecoder.decode(parsedCsvLine);
                    // ##### walidacja mapy nagłówek->wartość zdekodowanego wiersza #####
                    validatedCsvLineDecodedValues =
                            validateCsvLineDecodedValues( excludingsCsvLineDecodingResult.getDecodingResultMapped() );
                    if (validatedCsvLineDecodedValues == null) {
                        // todo: może jakaś rejestracja błędnych wierszy
                        ToolsGrip.handleProblem(
                                new CsvFileInvalidDataProblem(
                                        consoleLogger, // todo (1) - tutaj mogłoby być ToolsGrip.getLogger()
                                        "Błąd wartości w wierszu",
                                        csvFile,
                                        csvLineNumber,
                                        parsedCsvLine,
                                        excludingsCsvLineDecodingResult.getHeadersForInvalidValues()
                                )
                        );
                        continue;
                    }
                    // ##### weryfikacja kompletności zdekodowanych danych przed konwersją na ListingItem
                    initialExclusionValuesMapped =
                            getInitialExclusionValuesMapped( validatedCsvLineDecodedValues );
                    if (initialExclusionValuesMapped.hasNoCorrectAmountOfValues()) {
                        // todo: może jakaś rejestracja błędnych wierszy
                        ToolsGrip.handleProblem(
                                new CsvFileMissingDataProblem(
                                        consoleLogger,
                                        "Brak wartości w wierszu",
                                        csvFile,
                                        csvLineNumber,
                                        parsedCsvLine,
                                        initialExclusionValuesMapped
                                )
                        );
                        continue;
                    }
                    initialExclusionItem = converter.convertMappedExcludingItem2ExcludingItem( initialExclusionValuesMapped );
                    initialExclusions.addInitialExclusionItem( initialExclusionItem );
                }
                ToolsGrip.handleNotification( "" );
                ToolsGrip.handleNotification( "Wykluczenia zostały odczytane z pliku." );
            } catch (Exception e) {
                // todo
                // Tutaj wprawka implementacji podejmowania decyzji (np. w interakcji z użytkownikiem),
                // czy przerwać proces
                AppError appError = new AppError(e, AppError.INTERRUPTING_PROCESS);
                ToolsGrip.handleError( appError );
                if (appError.isInterruptingProcess()) {
                    setErrorOccurred();
                }
            } finally {
                try {
                    csvFile.close();
                } catch (IOException e) {
                    AppError appError = new AppError( e, AppError.INTERRUPTING_PROCESS );
                    ToolsGrip.handleError( appError );
                    if (appError.isInterruptingProcess()) {
                        setErrorOccurred();
                    }
                }
            }
        }
    }

    private HashMap<String, Value> validateCsvLineDecodedValues(HashMap<String, Value> csvLineDecoded ) {
        for ( Value value: csvLineDecoded.values() ) {
            if (value.validationResult()==null) {
                return null;
            }
        }
        return csvLineDecoded;
    }

    private InitialExclusionValuesMapped getInitialExclusionValuesMapped(HashMap<String, Value> initialExclusionItemMapped ) {
        InitialExclusionValuesMapped initialExclusionValuesMapped = new InitialExclusionValuesMapped();
        for ( String header : initialExclusionItemMapped.keySet() ) {
            if (initialExclusionValuesMapped.isHeaderNeeded( header ) ) {
                initialExclusionValuesMapped.addValue( initialExclusionItemMapped.get( header ), header );
            }
        }
        return initialExclusionValuesMapped;
    }

    // todo to trzeba przerobić ? bo listy
    public void setOnStartTakersStoresAsInitiallyFullExcluded() {
        if (areInitialExclusions() ) {
            ArrayList<Integer> idsOfStoresWithInitialExclusions =
                    initialExclusions.getAllStoreIdsWithInitialExclusions();
            if (idsOfStoresWithInitialExclusions.size() > 0) {
                /* todo
                // pokaż sklepy z wyłączeniami oprócz onedonor
                // uzytkownik wskazuje, który sklep jest głównym odbiorcą
                // po wybraniu biorcy głównego, jest on ustawiany jako excluded, a podczas tworzenia donacji w ogóle nie jest
                // brany pod uwagę.
                if (Config.IS_ONE_DONOR_ONLY) {
                    idsOfStoresWithInitialExclusions.remove( Config.ONE_DONOR_ONLY_ID );
                }
                Integer mainTakerStoreId;
                if (idsOfStoresWithInitialExclusions.size() == 1) {
                    mainTakerStoreId = idsOfStoresWithInitialExclusions.get(0);
                } else {
                    mainTakerStoreId = getFromUserMainTakerStoreId( idsOfStoresWithInitialExclusions );
                }
                setTakerStoreAsInitiallyExcluded( mainTakerStoreId );
                */
                ArrayList<Integer> fullInitiallyExcludedStoresIds = new ArrayList<>( idsOfStoresWithInitialExclusions );
                fullInitiallyExcludedStoresIds.remove( Config.ONE_DONOR_ONLY_ID );
                initialExclusions.setFullInitiallyExcludedStoresIds(
                        fullInitiallyExcludedStoresIds
                );
            }
        }
    }

    public void showTakersStoresInitiallyFullExcluded() {
        ToolsGrip.handleNotification( "" );
        ToolsGrip.handleNotification( "WYKLUCZONE SKLEPY:" );
        for ( Integer storeId : initialExclusions.getFullInitiallyExcludedStoresIds() ) {
            ToolsGrip.handleNotification( String.valueOf( storeId ) );
        }
    }

    public boolean areInitialExclusions() {
        return initialExclusions.getAllStoreIdsWithInitialExclusions().size()>0;
    }

 /*  todo ? private Integer getFromUserMainTakerStoreId( ArrayList<Integer> idsOfStoresWithExclusions ) {
        ToolsGrip.handleNotification( "Znaleziono plik z wykluczeniemi." );
        Integer mainTakerStoreId = null;
        do {
            showInfoForExcludingMainTakerStore( idsOfStoresWithExclusions );
            try {
                mainTakerStoreId = keyboardReader.nextInt();
            } catch (Exception e) {
                showImproperValueNotification();
            }
            if (!idsOfStoresWithExclusions.contains( mainTakerStoreId )) {
                showImproperValueNotification();
            }
        } while ( !idsOfStoresWithExclusions.contains( mainTakerStoreId ) );
        return mainTakerStoreId;
    }

    private void showImproperValueNotification() {
        ToolsGrip.handleNotification( "" );
        ToolsGrip.handleNotification( "BŁĄD: Nieprawidłowa wartość..." );
        ToolsGrip.handleNotification( "" );
    }

    private void showInfoForExcludingMainTakerStore( ArrayList<Integer> idsOfStoresWithExclusions ) {
        ToolsGrip.handleNotification( "Wybierz sklep - głównego biorcę:" );
        for ( Integer storeId : idsOfStoresWithExclusions ) {
            ToolsGrip.handleNotification( storeId.toString() );
        }
    }*/

    /* todo ?
    public void saveReserveObjects() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("rezerwa.ser"))) {
            oos.writeObject( DataGrip.getInstance().getReserveData().getNodes() );
            ToolsGrip.handleNotification("Mapa rezerwy została zapisana do pliku.");
        } catch (IOException e) {
            // todo wpływ na dalsze dzialanie programu można byz zrobic tak, że:
            // AppError appError = new AppError( e ); albo w zależności od sytuacji new BreakProcessError
            // ToolsGrip.handleError( appError );
            // w ErrorHandlerze podejmowana byłaby decyzja (sam ErrorHandler, lub w interakcji z użytkownikiem) co dalej
            // W appError ustawiana byłaby flaga informaująca co dalej
            // Po powrocie z ToolsGrip.handleError( appError ) flaga byłaby sprawdzana i wg niej ustawiana byłaby flaga stanu
            // obiktu w którym wywołano ToolsGrip.handleError()
            AppError appError = new AppError( e, AppError.INTERRUPTING_PROCESS );
            ToolsGrip.handleError( appError );
            if (appError.isInterruptingProcess()) {
                setErrorOccurred();
            }
        }
    }

    public void readReserveObjects() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("rezerwa.ser"))) {
            DataGrip.getInstance().getReserveData().setNodes( ois.readObject() );
            ToolsGrip.handleNotification( "Mapa rezerwy została odczytana z pliku." );
        } catch (IOException | ClassNotFoundException e) {
            ToolsGrip.handleError( new AppError( e ));
        }
    }
     */

    @Override
    public void setErrorOccurred( ) {
        errorOccurred = true;
    }

    @Override
    public void clearErrorOccurred() {
        errorOccurred = false;
    }

    @Override
    public boolean didErrorOccur() {
        return errorOccurred;
    }

}
