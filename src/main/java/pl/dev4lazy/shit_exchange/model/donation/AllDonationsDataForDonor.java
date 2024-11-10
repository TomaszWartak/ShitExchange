package pl.dev4lazy.shit_exchange.model.donation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class AllDonationsDataForDonor implements Serializable {

    private HashMap<Integer, DonationsDataForTakerStore> takersNodes = new HashMap<>(10);

    public HashMap<Integer, DonationsDataForTakerStore> getTakersNodes() {
        return takersNodes;
    }

    public void setTakersNodes(HashMap<Integer, DonationsDataForTakerStore> takersNodes) {
        this.takersNodes = takersNodes;
    }

     public void setTakersNodes(Object nodes) {
        this.takersNodes = (HashMap<Integer, DonationsDataForTakerStore>) nodes;
    }

    public void addDonationItem( DonationItem donationItem ) {
        if (isNotStoreIdPresent( donationItem.getTakerStoreId() ) ) {
            createStoreIdNode( donationItem );
        }
        putDonationItem( donationItem );
    }

    private boolean isStoreIdPresent(Integer donorStoreId ) {
        return takersNodes.containsKey( donorStoreId );
    }

    private boolean isNotStoreIdPresent(Integer donorStoreId ) {
        return !isStoreIdPresent( donorStoreId );
    }

    private void createStoreIdNode( DonationItem donationItem ) {
        takersNodes.put( donationItem.getTakerStoreId(), new DonationsDataForTakerStore() );
    }

    private void putDonationItem( DonationItem donationItem ) {
        takersNodes.get( donationItem.getTakerStoreId() ).putDonationItem( donationItem );
    }

    public void clearDonationItemsForAllTakerStores() {
        takersNodes.forEach( (takerStoreId, donationsDataForTakerStore ) -> donationsDataForTakerStore.clearAllDonationItems() );
        takersNodes.clear();
    }

    public void clearDonationItemsForManyTakerStores( ArrayList<Integer> takerStoresIds ) {
        for ( Integer takerStoreId : takerStoresIds ) {
            if ( isStoreIdPresent( takerStoreId ) ){
                getDonationsDataForTakerStore( takerStoreId ).clearAllDonationItems();
                takersNodes.remove( takerStoreId );
            }
        }
        // TODO? takersNodes.clear();
    }

    public ArrayList<Integer> getAllTakerStoresIds() {
        return new ArrayList<>( getTakersNodes().keySet() );
    }

    public DonationsDataForTakerStore getDonationsDataForTakerStore( Integer takerStoreId ) {
        return takersNodes.get( takerStoreId );
    }

    public ArrayList<DonationItem> getAllDonationItemsForTakerStore( Integer takerStoreId ) {
        return new ArrayList<>( getDonationsDataForTakerStore( takerStoreId ).getAllDonationItems() );
    }

}

