package pl.dev4lazy.shit_exchange.model.donation;

import java.util.ArrayList;
import java.util.HashMap;

public class DonationsData {

    private HashMap< Integer, AllDonationsDataForDonor > nodes = new HashMap<>(100);;

    public HashMap< Integer, AllDonationsDataForDonor> getNodes() {
        return nodes;
    }

    public void addDonationItem( DonationItem donationItem ) {
        if (isNotDonorIdPresent( donationItem )) {
            createDonorNode( donationItem);
        }
        putDonationItem( donationItem );
    }

    private boolean isNotDonorIdPresent( DonationItem donationItem ) {
        return !isDonorIdPresent( donationItem.getDonorStoreId() );
    }

    private boolean isDonorIdPresent( Integer donorStoreId ) {
        return nodes.containsKey( donorStoreId );
    }

    private  void createDonorNode( DonationItem donationItem ) {
        nodes.put( donationItem.getDonorStoreId(), new AllDonationsDataForDonor() );
    }

    private void putDonationItem( DonationItem donationItem ) {
        nodes.get( donationItem.getDonorStoreId() ).addDonationItem( donationItem );
    }

    public boolean haveData() {
        return getNodes().size() > 0;
    }

    public boolean haveNoData() {
        return getNodes().size() == 0;
    }

    private int getDonationsQuantity() {
        int donationsQuantity = 0;
        for ( AllDonationsDataForDonor donorData : nodes.values() ) {
            donationsQuantity += donorData.getTakersNodes().size();
        }
        return donationsQuantity;
    }

    public void clearAllDonations() {
        nodes.forEach( (donorStoreId, allDonationsDataForDonor)  -> allDonationsDataForDonor.clearDonationItemsForAllTakerStores() );
        nodes.clear();
    }

    public AllDonationsDataForDonor getAllDonationsDataOfDonor(Integer donorStoreId) {
        return nodes.get( donorStoreId );
    }

    public ArrayList<Integer> getAllDonorStoresIds() {
        return new ArrayList<>( getNodes().keySet() );
    }

    public void sort() {

    }
}
