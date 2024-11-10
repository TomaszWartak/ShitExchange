package pl.dev4lazy.shit_exchange.model.listing;

import pl.dev4lazy.shit_exchange.model.reserve.ReserveItem;

import java.io.Serializable;
import java.util.HashMap;

public class ListingDataForStore implements Serializable {
    private HashMap<Integer, ListingItem> nodeBody = new HashMap<>(45000);

    public HashMap<Integer, ListingItem> getNodeBody() {
        return nodeBody;
    }

    public void putListingItem(ListingItem listingItem) {
        nodeBody.put(listingItem.getSapId(), listingItem);
    }

    public ListingItem getListingItem( Integer sapId ) {
        return nodeBody.get( sapId );
    }

    public boolean isArticleListed( ReserveItem reserveItem ) {
        return isArticleListed( reserveItem.getSapId() );
    }

    public boolean isArticleListed( Integer sapId ) {
        return nodeBody.containsKey( sapId );
    }

    public boolean isArticleChosenForStockingUp( Integer sapId ) {
        return getListingItem( sapId ).getChoiceOfStokingUp().equals( "Tak zamawiaj" );
    }
}
