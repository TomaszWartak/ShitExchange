package pl.dev4lazy.shit_exchange.model;

import pl.dev4lazy.shit_exchange.model.donation.DonationsData;
import pl.dev4lazy.shit_exchange.model.initial_exclusions.InitialExclusionsData;
import pl.dev4lazy.shit_exchange.model.listing.ListingData;
import pl.dev4lazy.shit_exchange.model.reserve.ReserveData;

public class DataGrip {

    private static DataGrip instance;

    private static final String DATA_PATH = "data/";
    private static final String INPUT_LISTING_FILE_NAME = "listing.csv";
    private static final String INPUT_RESERVE_FILE_NAME = "rezerwa.csv";
    private static final String INPUT_EXCLUSIONS_FILE_NAME = "exclusions.csv";
    private static final String SAVED_LISTING_FILE_NAME = "listing.ser";
    private static final String SAVED_RESERVE_FILE_NAME = "rezerwa.ser";

    private static final String OUTPUT_PATH = "output/";
    private static final String ANALYSIS_FILE_NAME = "analiza.csv";
    private static final String DONATIONS_FILE_NAME = "wymiana.csv";

    private final ListingData listingData = new ListingData();
    private final ReserveData reserveData = new ReserveData();
    private final DonationsData donations = new DonationsData();
    private final InitialExclusionsData exclusions = new InitialExclusionsData();

    private DataGrip() {
        // Prywatny konstruktor, aby uniemożliwić tworzenie wielu instancji.
    }

    public static DataGrip getInstance() {
        if (instance == null) {
            instance = new DataGrip();
        }
        return instance;
    }

    public static String getInputListingFileNameFullPath() {
        return DATA_PATH+INPUT_LISTING_FILE_NAME;
    }

    public static String getInputReserveFileNameFullPath() {
        return DATA_PATH+INPUT_RESERVE_FILE_NAME;
    }

    public static String getInputExclusionsFileNameFullPath() {
        return DATA_PATH+INPUT_EXCLUSIONS_FILE_NAME;
    }

    public static String getSavedListingFileNameFullPath() {
        return DATA_PATH+SAVED_LISTING_FILE_NAME;
    }

    public static String getSavedReserveFileNameFullPath() {
        return DATA_PATH+SAVED_RESERVE_FILE_NAME;
    }

    public static String getOutputAnalysisFileNameFullPath() {
        return OUTPUT_PATH+ANALYSIS_FILE_NAME;
    }

    public static String getOutputDonationsFileNameFullPath() {
        return OUTPUT_PATH+DONATIONS_FILE_NAME;
    }

    public ListingData getListingData() {
        return listingData;
    }

    public ReserveData getReserveData() {
        return reserveData;
    }

    public DonationsData getDonationsData() {
        return donations;
    }

    public InitialExclusionsData getInitialExclusionsData() {
        return exclusions;
    }
}
