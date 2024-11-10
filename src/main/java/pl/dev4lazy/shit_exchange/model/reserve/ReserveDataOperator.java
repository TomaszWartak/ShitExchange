package pl.dev4lazy.shit_exchange.model.reserve;

import pl.dev4lazy.shit_exchange.Config;
import pl.dev4lazy.shit_exchange.model.initial_exclusions.InitialExclusionsData;
import pl.dev4lazy.shit_exchange.utils.csv_service.Decoder;
import pl.dev4lazy.shit_exchange.utils.problem_handling.ErrorsProne;
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

public class ReserveDataOperator implements ErrorsProne {

    private final ReserveData reserve;
    private final InitialExclusionsData initialExclusions;
    private CsvFile csvFile;
    private Decoder csvLineToMappedItemDecoder;
    private boolean errorOccurred = false;

    public ReserveDataOperator() {
        clearErrorOccurred();
        reserve = DataGrip.getInstance().getReserveData();
        initialExclusions = DataGrip.getInstance().getInitialExclusionsData();
        try {
            csvFile = new ReserveCsvFile(
                    DataGrip.getInputReserveFileNameFullPath(),
                    Charset.forName("Windows-1250"),
                    CsvUtils.CSV_SEPARATOR,
                    true
            );
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

    public void readAllStoreReserveData( NeededReserveValuesMapped2ReserveItemConverter converter ) {
        // TODO przy powtórnym wywołaniu musi być wyzerowanie danych
        int csvLineNumber = 0;
        ArrayList<String> parsedCsvLine;
        CsvLineDecodingResult reserveCsvLineDecodingResult;
        HashMap<String, Value> validatedCsvLineDecodedValues;
        NeededReserveValuesMapped neededReserveValuesMapped;
        ReserveItem reserveItem;
        ConsoleLogger consoleLogger = new ConsoleLogger();
        try {
            csvFile.openForReading();
            if (csvFile.hasHeader()) {
                csvFile.skipRow();
                csvLineNumber++;
            }
            while ((parsedCsvLine = csvFile.readParsedDataFromCsvLine())!=null) {
                csvLineNumber++;
                // TODO start
                if ((csvLineNumber % 100) ==0) {
                    System.out.println(csvLineNumber);
                }
                // TODO end
                // ##### dekodowanie wiersza w mapę nagłówek->wartość #####
                reserveCsvLineDecodingResult =
                        (CsvLineDecodingResult) csvLineToMappedItemDecoder.decode( parsedCsvLine );
                // ##### walidacja mapy nagłówek->wartość zdekodowanego wiersza #####
                validatedCsvLineDecodedValues =
                        validateCsvLineDecodedValues( reserveCsvLineDecodingResult.getDecodingResultMapped() );
                if (validatedCsvLineDecodedValues==null) {
                    // todo: może jakaś rejestracja błędnych wierszy
                    ToolsGrip.handleProblem(
                            new CsvFileInvalidDataProblem(
                                    consoleLogger,
                                    "Błąd wartości w wierszu",
                                    csvFile,
                                    csvLineNumber,
                                    parsedCsvLine,
                                    reserveCsvLineDecodingResult.getHeadersForInvalidValues()
                            )
                    );
                    continue;
                }
                // ##### weryfikacja kompletności zdekodowanych danych przed konwersją na ReserveItem
                neededReserveValuesMapped =
                        getNeededReserveValuesMapped( validatedCsvLineDecodedValues );
                if ( neededReserveValuesMapped.hasNoCorrectAmountOfValues() ) {
                    // todo: może jakaś rejestracja błędnych wierszy
                    ToolsGrip.handleProblem(
                            new CsvFileMissingDataProblem(
                                    consoleLogger,
                                    "Brak wartości w wierszu",
                                    csvFile,
                                    csvLineNumber,
                                    parsedCsvLine,
                                    neededReserveValuesMapped
                            )
                    );
                    continue;
                }
                // #### konwersja zdekodowanych danych na ReserveItem oraz dodanie go kolekcji, jeśli spełnia warunki biznesowe
                if ( neededReserveValuesMapped.isStatusActive() ) {
                    reserveItem = converter.convertMappedReserveItem2ReserveItem( neededReserveValuesMapped );
                    // TODO tutaj, czy przy odczycie wyłączeń? reserveItem.setExclusionState( isReserveItemExcluded( reserveItem ));
                    if (isReserveItemUseful( reserveItem )) {
                        reserve.addReserveItem( reserveItem );
                    }
                }
            }
            // #### jeśli są wyłączenia to:
            // - jeśli jest ONE_DONOR to wyłączenie z analizy wszystkich danych rezerwy głownego biorcy
            // - jeśli jest ONE_DONOR to wyłączenie z analizy wskazaanych w wyłączeniach artykułów głównego dawcy
            if ( ToolsGrip.getApp().getInitialExclusionsDataOperator().areInitialExclusions() ) {
                setInitiallyExcludedArticlesInMainDonorStore();
                setFullInitiallyExcludedTakersStores();
                // TODO !!! czy oprócz wstępnego wyłączenia sklepów dawców, nie trzeba również wyłączyć
                // pojedynczych artykułów oddawanaych do tych biorców w innych sklepach
                setInitiallyExcludedArticlesInOtherTakersStores();
                ToolsGrip.handleNotification( "Wykluczenia z pliku zostały uwzględnione" );
            }
        } catch (Exception e) {
            // todo
            // Tutaj wprawka implementacji podejmowania decyzji (np. w interakcji z użytkownikiem),
            // czy przerwać proces
            AppError appError = new AppError( e, AppError.INTERRUPTING_PROCESS );
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

    private HashMap<String, Value> validateCsvLineDecodedValues(HashMap<String, Value> csvLineDecoded ) {
        for ( Value value: csvLineDecoded.values() ) {
            if (value.validationResult()==null) {
                return null;
            }
        }
        return csvLineDecoded;
    }

    private NeededReserveValuesMapped getNeededReserveValuesMapped(HashMap<String, Value> reserveItemMapped ) {
        NeededReserveValuesMapped neededReserveValuesMapped = new NeededReserveValuesMapped();
        for ( String header : reserveItemMapped.keySet() ) {
            if (neededReserveValuesMapped.isHeaderNeeded( header ) ) {
                neededReserveValuesMapped.addValue( reserveItemMapped.get( header ), header );
            }
        }
        return neededReserveValuesMapped;
    }

    private boolean isReserveItemExcluded( ReserveItem reserveItem ) {
        return initialExclusions.isArticleExcludedInStore( reserveItem.getStoreId(), reserveItem.getSapId() );
    }

    private boolean isReserveItemUseful( ReserveItem reserveItem ) {
        return
                ( reserveItem.getValue()>0 );
    }

    private void setInitiallyExcludedArticlesInMainDonorStore() {
        if ( Config.IS_ONE_DONOR_ONLY ) {
            /* todo
            InitialExclusionsDataForStore initialExclusionsForMainDonorStore =
                    initialExclusionsData.getInitialExclusionsDataForStore( Config.ONE_DONOR_ONLY_ID );
            ReserveDataForStore reserveDataForOneDonorStore =
                    reserve.getReserveDataForStore( Config.ONE_DONOR_ONLY_ID );
            for ( Map.Entry<Integer, ReserveItem> entry : reserveDataForOneDonorStore.getStoreReserveItemsNode().entrySet() ) {
                if ( initialExclusionsForMainDonorStore.getNodeBody().containsKey( entry.getKey() ) ) {
                    entry.getValue().setExcludedFromDonation();
                }
            }
            */

            ArrayList<Integer> initiallyExcludedArticlesSapIdsForMainDonorStore =
                    initialExclusions.getInitiallyExcludedArticlesSapIdsForStore( Config.ONE_DONOR_ONLY_ID );
            reserve.excludeManyArticlesInStoreFromDonations(
                    Config.ONE_DONOR_ONLY_ID,
                    initiallyExcludedArticlesSapIdsForMainDonorStore
            );
        }
    }

    public void setFullInitiallyExcludedTakersStores() {
        ArrayList<Integer> fullInitiallyExcludedStoresIds =
                initialExclusions.getFullInitiallyExcludedStoresIds();
        for ( Integer excludedStoreId : fullInitiallyExcludedStoresIds ) {
            excludeStoreReserveData( excludedStoreId );
        }
    }

    public void excludeStoreReserveData( Integer storeId ) {
        reserve.excludeStoreFromDonations( storeId );
    }

    public void setInitiallyExcludedArticlesInOtherTakersStores() {
        ArrayList<Integer> fullInitiallyExcludedStoresIds = initialExclusions.getFullInitiallyExcludedStoresIds();
        for (Integer excludedStoreId: fullInitiallyExcludedStoresIds) {
            /* ArrayList<InitialExclusionItem> initialExclusionItems =
                    initialExclusionsData
                            .getInitialExclusionsDataForStore( excludedStoreId )
                            .getInitialExclusionItems();
            for (InitialExclusionItem initialExclusionItem : initialExclusionItems ) {
                 reserve.excludeArticleFromDonationsForAllStores( initialExclusionItem.getSapId() );
            } */
            // TODO sprawdź, czy to co powyżej jest tożsame z tym co poniżej

            ArrayList<Integer> initiallyExcludedArticlesSapIds =
                    initialExclusions.getInitiallyExcludedArticlesSapIdsForStore( excludedStoreId );
            reserve.excludeManyArticlesFromDonationsForNotExcludedStores(
                    initiallyExcludedArticlesSapIds
            );
        }
    }

    public void saveReserveObjects() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream( DataGrip.getSavedReserveFileNameFullPath() )
        )) {
            oos.writeObject( reserve.getStoresReserveNodes() );
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
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream( DataGrip.getSavedReserveFileNameFullPath() )
        )) {
            reserve.setStoresReserveNodes( ois.readObject() );
            ToolsGrip.handleNotification( "Mapa rezerwy została odczytana z pliku." );
        } catch (IOException | ClassNotFoundException e) {
            ToolsGrip.handleError( new AppError( e ));
        }
    }

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
