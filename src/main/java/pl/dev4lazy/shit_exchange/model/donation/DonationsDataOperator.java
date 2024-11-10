package pl.dev4lazy.shit_exchange.model.donation;

import pl.dev4lazy.shit_exchange.Config;
import pl.dev4lazy.shit_exchange.model.DataGrip;
import pl.dev4lazy.shit_exchange.model.ToolsGrip;
import pl.dev4lazy.shit_exchange.model.initial_exclusions.InitialExclusionsData;
import pl.dev4lazy.shit_exchange.model.initial_exclusions.InitialExclusionsDataForStore;
import pl.dev4lazy.shit_exchange.model.listing.ListingData;
import pl.dev4lazy.shit_exchange.model.listing.ListingDataForStore;
import pl.dev4lazy.shit_exchange.model.listing.ListingItem;
import pl.dev4lazy.shit_exchange.model.reserve.ReserveData;
import pl.dev4lazy.shit_exchange.model.reserve.ReserveItem;
import pl.dev4lazy.shit_exchange.model.reserve.ReserveDataForStore;
import pl.dev4lazy.shit_exchange.utils.KeyboardReader;
import pl.dev4lazy.shit_exchange.utils.csv_service.CsvUtils;
import pl.dev4lazy.shit_exchange.utils.problem_handling.AppError;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

public class DonationsDataOperator {

    private final String outputDonationsFileName;

    private final ReserveData reserve;
    private final ListingData listing;
    private final DonationsData donations;
    private final InitialExclusionsData initialExclusions;
    private final KeyboardReader keyboardReader = ToolsGrip.getKeyboardReader();

    public DonationsDataOperator() {
        this.reserve = DataGrip.getInstance().getReserveData();
        this.listing = DataGrip.getInstance().getListingData();
        this.donations = DataGrip.getInstance().getDonationsData();
        this.initialExclusions = DataGrip.getInstance().getInitialExclusionsData();
        this.outputDonationsFileName = DataGrip.getOutputDonationsFileNameFullPath();
    }

    public void processDonationsData() {
        // TODO
        // jeżeli dane donations już są,
        if (donations.haveData()) {
            // - to trzeba zapytać czy wyczyścić dane,
            int resultOfAskingUserWantsToClearTheData = askUserWantsToClearTheDonationsData();
            switch (resultOfAskingUserWantsToClearTheData) {
                case 0: {
                    // rezygnacja z wyboru
                    return;
                }
                case 1: {
                    // wyczyszczenie danych
                    clearDonationsData();
                    restoreInitialExclusions();
                    ToolsGrip.handleNotification( "" );
                    ToolsGrip.handleNotification( "Dane pośrednie wyczyszczone." );
                    ToolsGrip.handleNotification( "" );
                    break;
                }
                case 2: {
                    // procesować przygotowanie danych dla kolejnego biorcy
                    choosePrimaryTakerStore();
                    break;
                }
            }
        // - czy procesować dane w celu wyboru kolejnego biorcy głównego
        //      - jeśli główny biorca jeszcze nie zosstał wybrany, to wybór i procesowanie
        //      - jeśli został wybrany to procesowanie
        }
        // TODO start
        int allCounter = 0;
        // TODO end
        for ( Integer donorStoreId : reserve.getAllDonorsStoreIds() ) {
            ReserveDataForStore donorReserveDataForStore = reserve.getReserveDataForStore( donorStoreId );
            if ( donorReserveDataForStore.isNotExcludedFromDonations() ) {
                for ( Map.Entry<Integer, ReserveDataForStore> entry : reserve.getStoresReserveNodes().entrySet() ) {
                    Integer takerStoreId = entry.getKey();
                    ReserveDataForStore takerReserveDataForStore = reserve.getReserveDataForStore( takerStoreId );
                    if ( takerReserveDataForStore.isNotExcludedFromDonations() ) {
                        if ( ( Config.IS_ONE_DONOR_ONLY ) &&
                             ( !takerStoreId.equals( Config.ONE_DONOR_ONLY_ID )) &&
                             ( !donorStoreId.equals( Config.ONE_DONOR_ONLY_ID )) )  {
                            continue;
                        }
                        if  ( !takerStoreId.equals( donorStoreId) ){
                            int okCounter = 0;
                            for (ReserveItem reserveItem : donorReserveDataForStore.getReserveItems()) {
                                if ( isDonorReserveItemValidForDonation( reserveItem, takerStoreId) ) {
                                    DonationItem donationItem = createDonationItem( reserveItem, takerStoreId );
                                    donations.addDonationItem( donationItem );
                                    // TODO start
                                    ToolsGrip.handleNotification("ok " + okCounter++);
                                    // TODO end
                                }
                                // TODO start
                                if ((allCounter++ % 100) == 0) {
                                    System.out.println(allCounter);
                                }
                                // TODO end
                            }
                        }
                    }
                }
            }
        }
    }

    private int askUserWantsToClearTheDonationsData() {
        int chosenOption = 0;
        do {
            showAskingClearTheDonationsDataMenu( );
            boolean typeInputMismatchErrorNotOccurred = true;
            try {
                chosenOption = keyboardReader.nextInt();
            } catch (Exception e) {
                showImproperValueNotification();
                typeInputMismatchErrorNotOccurred = false;
            }
            if ( typeInputMismatchErrorNotOccurred && chosenOption!=0 && chosenOption!=1 && chosenOption!=2 ) {
                showImproperValueNotification();
            }
        } while ( chosenOption!=0 && chosenOption!=1 && chosenOption!=2 );
        return chosenOption;
    }

    private void showAskingClearTheDonationsDataMenu( ) {
        ToolsGrip.handleNotification( "Dane pośrednie były już przygotowane." );
        ToolsGrip.handleNotification( "Czy checesz zrezygnować z tworzenia danych pośrednich? (0)." );
        ToolsGrip.handleNotification( "Czy chcesz je usunąć? (1)" );
        ToolsGrip.handleNotification( "Czy chcesz procesować przygotowanie danych dla kolejnego biorcy? (2)" );
    }

    private void restoreInitialExclusions() {
        restoreInitialFullExclusionsForTakers();
        restoreInitialExclusionsForMainDonor();
    }

    private void restoreInitialFullExclusionsForTakers() {
        ArrayList<Integer> fullInitiallyExcludedStoresIds =
                initialExclusions.getFullInitiallyExcludedStoresIds();
        for ( ReserveDataForStore reserveDataForStore : reserve.getStoresReserveNodes().values() ) {
            Integer storeId = reserveDataForStore.getStoreId();
            if ( !fullInitiallyExcludedStoresIds.contains( storeId ) && reserveDataForStore.isExcludedFromDonations() ) {
               reserveDataForStore.includeForDonations( );
            }
        }
    }

    public void restoreInitialExclusionsForMainDonor() {
        if ( Config.IS_ONE_DONOR_ONLY) {
            InitialExclusionsDataForStore initialExclusionsDataForMainDonor =
                    initialExclusions
                    .getInitialExclusionsDataForStore( Config.ONE_DONOR_ONLY_ID);
            ArrayList<ReserveItem> donorReserveItems =
                    reserve
                    .getReserveDataForStore( Config.ONE_DONOR_ONLY_ID )
                    .getReserveItems();
            for ( ReserveItem reserveItem : donorReserveItems ) {
                if ( reserveItem.isExcludedFromDonation() &&
                        initialExclusionsDataForMainDonor.isArticleInitiallyExcluded( reserveItem.getSapId() ) ) {
                    reserveItem.setIncludedForDonation();
                }
            }
        }
    }

    public void clearDonationsData() {
        donations.clearAllDonations();
        /* TODO !!! a rezerwa? Żeby odtworzyc stan sprzed donacji, to:
         Jeśli nie ma initial exclusions, to
            - dla sklepów gdzie ReserveDataForStore.excludedFromDonations=false, przejrzec wszystkie artykuły i ustawić
                wszystkie ReserveItem.excluded na false
            - dla sklepów gdzie ReserveDataForStore.excludedFromDonations=true trzeba  ustawić wszystkie
                ReserveDataForStore.excludedFromDonations na false
         Jeśli są initial exclusions, to nie należy tego, co wyżej, robić dla sklepów biorców
         wymieniownych w exclusions.csv (nie zmieniac dla nich ReserveDataForStore.excludedFromDonations=true)
         oraz dla głownego dawcy należy zostawic ReserveItem.excluded=true dla jego artykułów wymienionych
         w  exclusions.csv
        */
        /*
         Dane analizy chyba również należy usunąć, bo gdy jeśli naliza była robiona, to pomimo wykoanani tego,
         co powyżej, to po wybraniu "Generuj plik z danymi analizy" wygenerowane zostaną dane z istniejącej analizy.
         */
    }

    public void choosePrimaryTakerStore() {
        // jeśli nie ma wygenerowanych danych pośrednich to na drzewo
        if ( donations.haveNoData() ) {
            ToolsGrip.handleNotification( "Brak danych pośrednich." );
            ToolsGrip.handleNotification( "Wygeneruj dane pośrednie" );
            ToolsGrip.handleNotification( "" );
            return;
        }
        // jeśli dane są wygenerowane wyświetl informacje "Wybierz głównego biorcę dla wygenerowanych danych
        // uruchom metodę wyboru głównego biorcy
        //      wyświetl listę biorców
        //      szczytaj id wybranego biorcy
        ArrayList<Integer> idsOfNotExcludedTakersStores =
                new ArrayList<>( reserve.getNotExcludedFromDonationsStoreIds() );
        idsOfNotExcludedTakersStores.remove( Config.ONE_DONOR_ONLY_ID );
        Integer primaryTakerStoreId = askUserForSelectionOfPrimaryTakerStoreId( idsOfNotExcludedTakersStores );
        if (primaryTakerStoreId!=0) {
            // Usuń artykuły "użyte" w donacji do wybranego biorcy artykuły
            // z donacji dla pozostałych takerów (aktywnych = nie wykluczonych)
            updateDataAfterPrimaryTakerSelection( primaryTakerStoreId );
            // Po wyborze wyświetl wiersz informacji o donacji
            // czyli wygeneruj informację jak w analiziee tylko dla jednego biorcy
            // zablokuj w reserwie u onedonora "użyte" artykuły
            ToolsGrip.handleNotification( "" );
            ToolsGrip.handleNotification( "Główny biorca wybrany: "+primaryTakerStoreId );
            ToolsGrip.handleNotification( "" );
        }
     }

    private Integer askUserForSelectionOfPrimaryTakerStoreId(ArrayList<Integer> idsOfNotExcludedTakersStores ) {
        Integer choiceResult = null;
        do {
            showGettingPrimaryTakerStoreMenu( idsOfNotExcludedTakersStores );
            try {
                choiceResult = keyboardReader.nextInt();
            } catch (Exception e) {
                showImproperValueNotification();
            }
            // TODO czy choiceResult==0 działa? Zrób stałą REZYGNACJA_Z_WYBORU=0;
            // TODO zró mrtodę isChosenValueImproper() zamiast
            //  !idsOfNotExcludedTakersStores.contains( choiceResult ) || (choiceResult==0))
            if (!idsOfNotExcludedTakersStores.contains( choiceResult ) || (choiceResult==0)) {
                showImproperValueNotification();
            }
        } while ( !idsOfNotExcludedTakersStores.contains( choiceResult ) || (choiceResult==0));
        return choiceResult;
    }

    private void showGettingPrimaryTakerStoreMenu(ArrayList<Integer> idsOfStoresWithExclusions ) {
        Collections.sort( idsOfStoresWithExclusions );
        ToolsGrip.handleNotification( "Wybierz sklep - głównego biorcę:" );
        for ( Integer storeId : idsOfStoresWithExclusions ) {
            ToolsGrip.handleNotification( storeId.toString() );
        }
        ToolsGrip.handleNotification( "Rezygnacja z wyboru (0)" );
    }

    private void showImproperValueNotification() {
        ToolsGrip.handleNotification( "" );
        ToolsGrip.handleNotification( "BŁĄD: Nieprawidłowa wartość..." );
        ToolsGrip.handleNotification( "" );
    }

    private void updateDataAfterPrimaryTakerSelection(Integer primaryTakerStoreId ) {
        // TODO
        // usuń artykuły "użyte" w donacji do wybranego biorcy artykuły
        // z donacji dla pozostałych takerów (aktywnych = nie wykluczonych)
        AllDonationsDataForDonor allDonationsDataOfMainDonor =
                donations.getAllDonationsDataOfDonor( Config.ONE_DONOR_ONLY_ID );
        ArrayList<Integer> sapIdsOfMainDonorDonationsForPrimaryTaker =
                allDonationsDataOfMainDonor
                        .getDonationsDataForTakerStore( primaryTakerStoreId )
                        .getAllDonationItemsSapIds();

        removeDonationDataWhichWillBeDonatedFromMainDonorToPrimaryTaker(
                primaryTakerStoreId,
                allDonationsDataOfMainDonor,
                sapIdsOfMainDonorDonationsForPrimaryTaker
        );
        // Trzeba zrobić również w drugą stronę - to co my bierzemy od main takera trzeba usunąć z donacji
        // od innych takerów oraz
        ArrayList<Integer> sapIdsOfDonationsWhichPrimaryTakerGivesMainDonorStore =
                donations
                        .getAllDonationsDataOfDonor( primaryTakerStoreId )
                        .getDonationsDataForTakerStore( Config.ONE_DONOR_ONLY_ID  )
                        .getAllDonationItemsSapIds();

        removeDonationDataWhichWillBeDonatedFromPrimaryTakerToMainDonor(
                primaryTakerStoreId,
                sapIdsOfDonationsWhichPrimaryTakerGivesMainDonorStore
        );

        // TODO
        // zrobić excluded w rezerwie u innych takerów <- czy tak robisz przy wyłączeniach startowych?
        // w rezerwie zaznacz sklep main takera, jako excluded
        // w rezerwie artykuły one donora wzięte przez main takera zaznacz, jako excluded
        // ale ta metoda nazywa się updateDonations, a nie updateReserve...
        // jak uwzględnić to co bierzemy od main takera?
        excludeReserveWhichWillBeDonatedFromMainDonorToPrimaryTaker(
                sapIdsOfMainDonorDonationsForPrimaryTaker
        );

        excludeReserveWhichWillBeDonatedFromPrimaryTakerToMainDonor(
                primaryTakerStoreId,
                sapIdsOfDonationsWhichPrimaryTakerGivesMainDonorStore
        );
    }

    private void removeDonationDataWhichWillBeDonatedFromMainDonorToPrimaryTaker(
            Integer primaryTakerStoreId,
            AllDonationsDataForDonor allDonationsDataForMainDonor,
            ArrayList<Integer> sapIdsOfMainDonorDonationsForPrimaryTaker ) {
        for ( Integer takerStoreId : allDonationsDataForMainDonor.getAllTakerStoresIds() ) {
            if ( !takerStoreId.equals( Config.ONE_DONOR_ONLY_ID ) &&
                 !takerStoreId.equals( primaryTakerStoreId ) ) {
                DonationsDataForTakerStore donationsDataForTakerStore =
                        allDonationsDataForMainDonor.getDonationsDataForTakerStore( takerStoreId );
                donationsDataForTakerStore.removeManyDonationItems(
                        sapIdsOfMainDonorDonationsForPrimaryTaker
                );
            }
        }
    }
    
    private void removeDonationDataWhichWillBeDonatedFromPrimaryTakerToMainDonor(
            Integer primaryTakerStoreId,
            ArrayList<Integer> sapIdsOfDonationsWhichPrimaryTakerGivesMainDonorStore ) {
        for ( Integer donorStoreId : donations.getAllDonorStoresIds() ) {
            if ( !donorStoreId.equals( Config.ONE_DONOR_ONLY_ID ) &&
                 !donorStoreId.equals( primaryTakerStoreId ) ) {
                AllDonationsDataForDonor allDonationsDataForDonor =
                        donations.getAllDonationsDataOfDonor( donorStoreId );
                DonationsDataForTakerStore donationsDataWhenTakerIsMainDonorStore = 
                        allDonationsDataForDonor.getDonationsDataForTakerStore( Config.ONE_DONOR_ONLY_ID );
                donationsDataWhenTakerIsMainDonorStore.removeManyDonationItems(
                        sapIdsOfDonationsWhichPrimaryTakerGivesMainDonorStore
                );
            }
        }
    }

    private void excludeReserveWhichWillBeDonatedFromMainDonorToPrimaryTaker(
            ArrayList<Integer> sapIdsOfMainDonorDonationsForPrimaryTaker ) {
        reserve.excludeManyArticlesInStoreFromDonations(
                Config.ONE_DONOR_ONLY_ID,
                sapIdsOfMainDonorDonationsForPrimaryTaker
        );
    }

    private void excludeReserveWhichWillBeDonatedFromPrimaryTakerToMainDonor(
            Integer primaryTakerStoreId,
            ArrayList<Integer> sapIdsOfDonationsWhichPrimaryTakerGivesMainDonorStore ) {
        reserve.excludeStoreFromDonations( primaryTakerStoreId );
        reserve.excludeManyArticlesFromDonationsForNotExcludedStores(
            sapIdsOfDonationsWhichPrimaryTakerGivesMainDonorStore
        );
        /* TODO sprawdź, czy to co powyżej jest tożsame z tym co poniżej
         chyba jest ok... :-)
        for ( Integer donorStoreId: reserve.getAllDonorsStoreIds() ) {
            if ( !donorStoreId.equals( Config.ONE_DONOR_ONLY_ID ) &&
                    !donorStoreId.equals( primaryTakerStoreId ) ) {
                ReserveDataForStore reserveDataForDonorStore = reserve.getReserveDataForStore( donorStoreId );
                if ( reserveDataForDonorStore.isNotExcludedFromDonations() ) {
                    reserveDataForDonorStore.excludeManyReserveItemsFromDonations(
                            sapIdsOfDonationsWhichPrimaryTakerGivesMainDonorStore
                    );
                }
            }
        } */
    }

    public void removeUnusedTakersData() {
        ArrayList<Integer> notExcludedFromDonationsStoreIds = reserve.getNotExcludedFromDonationsStoreIds();
        for ( Integer storeId : notExcludedFromDonationsStoreIds ) {
            if (!storeId.equals( Config.ONE_DONOR_ONLY_ID ) ) {
                reserve.excludeStoreFromDonations( storeId) ;
                donations.getAllDonationsDataOfDonor( storeId ).clearDonationItemsForAllTakerStores();
            } else {
                donations.getAllDonationsDataOfDonor( storeId ).clearDonationItemsForManyTakerStores( notExcludedFromDonationsStoreIds );
            }
        }
        // TODO zrób wyświetlenie (jak w AnalysisDataOperator.analyzeData())
        //  komunikatu żesą juz wybrani biorcy do wymiany
        //  oraz komunikatu że są biorcy nie brani jeszcze pod uwagę
    }

    private boolean isDonorReserveItemValidForDonation( ReserveItem donorReserveItem, int takerStoreId ) {
        return
                donorReserveItem.isNotExcludedFromDonation() &&
                hasHighReserveValue( donorReserveItem ) &&
                isListedInTaker( donorReserveItem, takerStoreId ) &&
                isNotInReserveInTaker( donorReserveItem, takerStoreId ) &&
                isChosenForStockingUpInTaker( donorReserveItem, takerStoreId ) &&
                hasGoodRotationInTaker( donorReserveItem, takerStoreId );
    }

    private boolean hasHighReserveValue( ReserveItem reserveItem ) {
        return reserveItem.getValue()>Config.MIN_DONOR_RESERVE_VALUE;
    }

    private boolean isListedInTaker( ReserveItem reserveItem, int takerStoreId ) {
        return listing.isArticleListedInStore( reserveItem.getSapId(), takerStoreId );
    }

    private boolean isChosenForStockingUpInTaker( ReserveItem reserveItem, int takerStoreId ) {
        return listing.isChosenForStockingUpInStore( reserveItem.getSapId(), takerStoreId );
    }

    private boolean isNotInReserveInTaker(ReserveItem reserveItem, int takerStoreId ) {
        return reserve.isNotInReserveInStore( reserveItem, takerStoreId );
    }

    private boolean hasGoodRotationInTaker( ReserveItem reserveItem, int takerStoreId ) {
        return listing.hasGoodRotationInStore( reserveItem.getSapId(), takerStoreId );
    }

    private DonationItem createDonationItem( ReserveItem reserveItem, Integer takerStoreId ) {
        ListingDataForStore listingDataForTakerStore = listing.getListingDataForStore( takerStoreId );
        ListingItem takerListingItem = listingDataForTakerStore.getListingItem( reserveItem.getSapId() );
        return new DonationItem.DonationItemBuilder()
                .withDonorStoreId( reserveItem.getStoreId() )
                .withDonorStoreName( reserveItem.getStoreName() )
                .withSapId( reserveItem.getSapId() )
                .withEanCode( reserveItem.getEanCode() )
                .withArticleName( reserveItem.getArticleName() )
                .withDonorReserveValue( reserveItem.getValue() )
                .withDonorQuantityToZeroReserve( reserveItem.getQuantityToZeroReserve() )
                .withDonorStockQuantity( reserveItem.getStockQuantity() )
                .withDonorStockValue( reserveItem.getStockValue() )
                .withSector( reserveItem.getSector() )
                .withDonorCogs12M( reserveItem.getCogs12M() )
                .withTakerStoreId( takerStoreId )
                .withTakerStoreName( takerListingItem.getStoreName() )
                .withTakerPlanoKind( takerListingItem.getPlanoKind() )
                .withTakerTypeOfStokingUp( takerListingItem.getTypeOfStokingUp() )
                .withTakerChoiceOfStokingUp( takerListingItem.getChoiceOfStokingUp() )
                .withTakerStockQuantity( takerListingItem.getStockQuantity() )
                .withTakerStockValue( takerListingItem.getStockValue() )
                .withTakerSales12MValue( takerListingItem.getSales12MValue() )
                .withTakerSales12MQuantity( takerListingItem.getSales12MQuantity() )
                .withTakerRotation( takerListingItem.getRotation() )
                .build();
    }

    public void generateDonationsCsvFile() {
        if (donations.getNodes().isEmpty()) {
            ToolsGrip.handleNotification( "BRAK DANYCH..." );
            return;
        }

        DonationsCsvFile donationsCsvFile = new DonationsCsvFile(
                outputDonationsFileName,
                Charset.forName("Windows-1250"),
                CsvUtils.CSV_SEPARATOR,
                CsvUtils.HAS_HEADER );
        try {
            donationsCsvFile.openForWriting();
        } catch (Exception e){
            ToolsGrip.handleError( new AppError( e ));
        }

        ArrayList<String> outputCsvLineList = new ArrayList<>();
        outputCsvLineList.add( donationsCsvFile.getCsvHeaderRow() );

        ArrayList<Integer> donorStoresIds = donations.getAllDonorStoresIds();
        donorStoresIds.sort( Integer::compareTo );
        DonationItemToCsvLineCoder csvLineCoder = new DonationItemToCsvLineCoder();

        for ( Integer donorStoreId : donorStoresIds ) {
            AllDonationsDataForDonor allDonationsDataForDonor = donations.getAllDonationsDataOfDonor( donorStoreId );
            ArrayList<Integer> takerStoresIds = allDonationsDataForDonor.getAllTakerStoresIds();
            takerStoresIds.sort( Integer::compareTo );

            ArrayList<String> parsedCsvLine;
            for ( Integer takerStoreId: takerStoresIds ) {
                DonationsDataForTakerStore donationsDataForTakerStore =
                        allDonationsDataForDonor.getDonationsDataForTakerStore( takerStoreId );
                ArrayList<DonationItem> allDonationItemsForTakerStore = donationsDataForTakerStore.getAllDonationItems();
                allDonationItemsForTakerStore.sort( Comparator.comparing( DonationItem::getDonorReserveValue ).reversed() );
                for ( DonationItem donationItem: allDonationItemsForTakerStore ) {
                    // TODO sprawdz czy dla takera ilosc sprzedaży nie równa się wartość, i czy rotacja jest
                    parsedCsvLine = csvLineCoder.code( donationItem );
                    outputCsvLineList.add( CsvUtils.serializeToCsvLine( parsedCsvLine ) );
                }
            }
        }

        saveCsvLines( outputCsvLineList, donationsCsvFile );

        try {
            donationsCsvFile.close();
        } catch ( Exception e ) {
            ToolsGrip.handleError( new AppError( e ));
        }
    }

    private void saveCsvLines( ArrayList<String> outputCsvLineList, DonationsCsvFile donationsCsvFile ) {
        try {
            donationsCsvFile.writeCsvLines( outputCsvLineList );
        } catch (IOException ioException ) {
            throw new IllegalStateException();
        }
    }

}
