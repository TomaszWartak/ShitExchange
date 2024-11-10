package pl.dev4lazy.shit_exchange;

import pl.dev4lazy.shit_exchange.model.ToolsGrip;
import pl.dev4lazy.shit_exchange.model._Todo_;
import pl.dev4lazy.shit_exchange.model.analysis.AnalysisDataOperator;
import pl.dev4lazy.shit_exchange.model.donation.DonationsDataOperator;
import pl.dev4lazy.shit_exchange.model.initial_exclusions.InitialExclusionValuesMapped2ExclusionItemConverter;
import pl.dev4lazy.shit_exchange.model.initial_exclusions.InitialExclusionsDataOperator;
import pl.dev4lazy.shit_exchange.model.listing.ListingDataOperator;
import pl.dev4lazy.shit_exchange.model.listing.NeededListingValuesMapped2ListingItemConverter;
import pl.dev4lazy.shit_exchange.model.reserve.NeededReserveValuesMapped2ReserveItemConverter;
import pl.dev4lazy.shit_exchange.model.reserve.ReserveDataOperator;
import pl.dev4lazy.shit_exchange.utils.KeyboardReader;

public class ShitExchangeApplication {

    // todo private Scanner scanner;
    private final ListingDataOperator listingDataOperator = new ListingDataOperator();
    private final ReserveDataOperator reserveDataOperator = new ReserveDataOperator();
    private final InitialExclusionsDataOperator initialExclusionsDataOperator = new InitialExclusionsDataOperator();
    private final DonationsDataOperator donationsDataOperator = new DonationsDataOperator();
    private final AnalysisDataOperator analysisDataOperator = new AnalysisDataOperator();
    private final _Todo_ _todo_  = new _Todo_();
    private final KeyboardReader keyboardReader = ToolsGrip.getKeyboardReader();

    public InitialExclusionsDataOperator getInitialExclusionsDataOperator() {
        return initialExclusionsDataOperator;
    }

    public void run() {
        showParameters();
        getInitialExclusionsData();
        // todo
        // - Jeśli są wyłączenia, bo jesteśmy w trakcie wymiany z jakimiś sklepami, to ustawinie głownego biorcy
        //   Chyba powinno się to odbyć tak, że po zaczytaniu wyłączeń wyświetla się lista sklepów (oprócz dawcy głównego)
        //   z wyłączeniami, i zapytanie, który sklep jest głównym biorcą.
        // - Po wybraniu biorcy głównego, jest on ustawiany jako excluded, a podczas tworzenia donacji w ogóle nie jest
        //   brany pod uwagę.
        mainLoop();
    }

    private void showParameters() {
        ToolsGrip.handleNotification( "" );
        ToolsGrip.handleNotification( "PARAMETRY:" );
        ToolsGrip.handleNotification( "Jeden dawca: "+Config.IS_ONE_DONOR_ONLY );
        if (Config.IS_ONE_DONOR_ONLY) {
            ToolsGrip.handleNotification("Jeden dawca - nr sklepu: " + Config.ONE_DONOR_ONLY_ID);
        }
        ToolsGrip.handleNotification("Maksymalna wartość rotacji: " + String.format(" %.0f", Config.MAX_ROTATION_VALUE ));
        ToolsGrip.handleNotification("Minimalna wartość rezerwy: " + String.format(" %.0f", Config.MIN_DONOR_RESERVE_VALUE ));
    }

    private void getInitialExclusionsData() {
        initialExclusionsDataOperator.readInitialExclusions( new InitialExclusionValuesMapped2ExclusionItemConverter() );
        initialExclusionsDataOperator.setOnStartTakersStoresAsInitiallyFullExcluded();
        initialExclusionsDataOperator.showTakersStoresInitiallyFullExcluded();
    }

    private void mainLoop() {
        // todo scanner = new Scanner(System.in);
        Option option = null;
        do {
            printMenu();
            try {
                option = chooseOption();
                executeOption( option );
            } catch (Exception e) {
                // TODO !!! Komunkat w tym catchu informuje o wybraniu nieprawidłowej opcji w chooseOption(),
                //  co jest bez sensu, jeśli wyjątek pojawi się w executeOption();
                // zwłaszcza, jeśli dostajesz NullPOinterException...
                showImproperValueNotification();
            }
        } while ( option != Option.EXIT );
    }

    private void printMenu() {
        ToolsGrip.handleNotification( "" );
        ToolsGrip.handleNotification( "Wybierz opcję:" );
        for ( Option option : Option.values() ) {
            ToolsGrip.handleNotification( option.toString() );
        }
    }

    private Option chooseOption() {
        int option;
        try {
            // todo option = scanner.nextInt();
            option = keyboardReader.nextInt();
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }
        return Option.fromInt( option );
    }

    private void executeOption(Option option) {
        switch (option) {
            case GET_LISTING_INPUT_DATA -> getListingInputData();
            case GET_RESERVE_INPUT_DATA -> getReserveInputData();
            case SAVE_LISTING_OBJECTS -> saveListingObjects();
            case SAVE_RESERVE_OBJECTS -> saveReserveObjects();
            case READ_LISTING_OBJECTS -> readListingObjects();
            case READ_RESERVE_OBJECTS -> readReserveObjects();
            case PROCESS_DONATIONS_DATA -> processDonationsData();
            case CLEAR_DONATIONS_DATA -> clearDonationsData();
            case ANALYZE_DONATIONS_DATA -> analyzeDonationsData();
            case CHOOSE_NEXT_TAKER_STORE -> chooseMainTakerStore();
            case REMOVE_UNUSED_TAKERS -> removeUnusedTakersFromDonationsData();
            case GENERATE_ANALYZE_FILE -> generateAnalyzeFile();
            case GENERATE_OUTPUT_FILE -> generateOutputFile();
            case SHOW_FILES_INFO -> showFilesInfo();
            case EXIT -> close();
        }
    }

    private void showImproperValueNotification() {
        ToolsGrip.handleNotification( "" );
        ToolsGrip.handleNotification( "BŁĄD: Nieprawidłowa wartość..." );
        ToolsGrip.handleNotification( "" );
    }

    private void getListingInputData() {
        // odczyt pliku listingu dla wszystkich sklepów
        //        stworzenie mapy gdzie kluczem jest sap ID a wartością lista sklepów, w której ten SAP ID jest w listingu
        listingDataOperator.readAllStoresListingData( new NeededListingValuesMapped2ListingItemConverter() );
    }

    private void getReserveInputData() {
        // odczyt pliku listingu dla wszystkich sklepów
        //        stworzenie mapy gdzie kluczem jest sap ID a wartością lista sklepów, w której ten SAP ID jest w listingu
        reserveDataOperator.readAllStoreReserveData( new NeededReserveValuesMapped2ReserveItemConverter() );
    }

    private void saveListingObjects() {
        listingDataOperator.saveListingObjects();
    }

    private void saveReserveObjects() {
        reserveDataOperator.saveReserveObjects();
    }

    private void readListingObjects() {
        listingDataOperator.readListingObjects();
    }

    private void readReserveObjects() {
        reserveDataOperator.readReserveObjects();
    }

    private void processDonationsData() {
        donationsDataOperator.processDonationsData();
    }

    private void clearDonationsData() {
        donationsDataOperator.clearDonationsData();
    }

    private void analyzeDonationsData() {
        analysisDataOperator.analyzeData();
    }

    private void chooseMainTakerStore() {
        donationsDataOperator.choosePrimaryTakerStore();
    }

    private void removeUnusedTakersFromDonationsData() {
        donationsDataOperator.removeUnusedTakersData();
    }

    private void generateAnalyzeFile() {
        analysisDataOperator.generateAnalysisCsvFile();
    }

    private void generateOutputFile() {
        donationsDataOperator.generateDonationsCsvFile();
    }

    private void showFilesInfo() {
        _todo_.showFilesInfo();
    }

    private void close() {
        // todo scanner.close();
        ToolsGrip.getKeyboardReader().close();
        ToolsGrip.handleNotification("Bye Bye!");
    }

    private enum Option {
        GET_LISTING_INPUT_DATA(1, "Pobierz dane wejściowe z pliku listingu"),
        GET_RESERVE_INPUT_DATA(2, "Pobierz dane wejściowe z pliku rezerwy"),
        SAVE_LISTING_OBJECTS( 3, "Zapisz obiekty listingu"),
        SAVE_RESERVE_OBJECTS( 4, "Zapisz obiekty rezerwy" ),
        READ_LISTING_OBJECTS( 5, "Odczytaj obiekty listingu"),
        READ_RESERVE_OBJECTS( 6, "Odczytaj obiekty rezerwy" ),
        PROCESS_DONATIONS_DATA(7, "Procesuj dane donacji"),
        CLEAR_DONATIONS_DATA(8, "Wyczyść dane donacji"),
        ANALYZE_DONATIONS_DATA(9, "Analizuj dane donacji"),
        CHOOSE_NEXT_TAKER_STORE( 10, "Wybierz biorcę"),
        REMOVE_UNUSED_TAKERS( 11, "Usuń biorców bez wymian z danych donacji"),
        GENERATE_ANALYZE_FILE( 12, "Generuj plik z danymi analizy"),
        GENERATE_OUTPUT_FILE(13, "Generuj plik z danymi wyjściowymi"),
        SHOW_FILES_INFO(14, "Pokaż informacje o plikach"),
        EXIT(15, "Zakończ");

        private final int optionNumber;

        private final String description;

        Option(int optionNumber, String description) {
            this.optionNumber = optionNumber;
            this.description = description;
        }

        static Option fromInt(int option) {
            if (option < 0 || option > values().length) {
                throw new IllegalArgumentException("Opcja o takim numerze nie istnieje");
            }
            return values()[option - 1];
        }

        @Override
        public String toString() {
            return String.format("%d - %s", optionNumber, description);
        }
    }


}
