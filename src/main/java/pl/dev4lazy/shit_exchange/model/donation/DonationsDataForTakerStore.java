package pl.dev4lazy.shit_exchange.model.donation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DonationsDataForTakerStore implements Serializable {
    private HashMap< Integer, DonationItem > nodeBody = new HashMap<>(45000);

    public HashMap< Integer, DonationItem > getNodeBody() {
        return nodeBody;
    }

    public void putDonationItem( DonationItem donationItem ) {
        nodeBody.put( donationItem.getSapId(), donationItem );
    }

    public void removeDonationItem( DonationItem donationItem ) {
        nodeBody.remove( donationItem.getSapId() );
    }

    public void removeManyDonationItems( ArrayList<Integer> SapIds ) {
        nodeBody.keySet().removeAll( SapIds );
    }

    public void clearAllDonationItems() {
        nodeBody.clear();
    }

    public ArrayList<DonationItem> getAllDonationItems() {
        return new ArrayList<>(nodeBody.values());
    }

    public ArrayList<Integer> getAllDonationItemsSapIds () {
        return getAllDonationItems()
            .stream()
            .map( donationItem -> donationItem.getSapId() )
            .collect( Collectors.toCollection( ArrayList::new ) );
    }
}
