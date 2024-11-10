package pl.dev4lazy.shit_exchange.model.listing;

import pl.dev4lazy.shit_exchange.Config;

import java.io.Serializable;
import java.util.HashMap;

public class ListingData implements Serializable {

    private HashMap<Integer, ListingDataForStore> storesListingNodes = new HashMap<>(10);

    public HashMap<Integer, ListingDataForStore> getStoresListingNodes() {
        return storesListingNodes;
    }

    public void setStoresListingNodes( HashMap<Integer, ListingDataForStore> nodes ) {
        storesListingNodes = nodes;
    }

     public void setStoresListingNodes(Object nodes) {
        storesListingNodes = (HashMap<Integer, ListingDataForStore>) nodes;
    }

    public void addListingItem( ListingItem listingItem ) {
        if (isNotStoreIdPresent( listingItem.getStoreId() ) ) {
            createStoreListingNode( listingItem );
        }
        putListingItem( listingItem );
    }

    private void putListingItem( ListingItem listingItem ) {
        storesListingNodes.get( listingItem.getStoreId() ).putListingItem( listingItem );
    }

    private void createStoreListingNode( ListingItem listingItem ) {
        storesListingNodes.put( listingItem.getStoreId(), new ListingDataForStore() );
    }

    private boolean isStoreIdPresent( Integer storeId ) {
        return storesListingNodes.containsKey(storeId);
    }

    private boolean isNotStoreIdPresent( Integer storeId ) {
        return !isStoreIdPresent(storeId);
    }

    public boolean isArticleListedInStore( Integer sapId, Integer storeId ) {
        return
                isStoreIdPresent( storeId ) &&
                getListingDataForStore( storeId ).isArticleListed( sapId );
    }

    public boolean isChosenForStockingUpInStore( Integer sapId, Integer storeId ) {
        return
                isStoreIdPresent( storeId ) &&
                isArticleListedInStore( sapId, storeId ) &&
                getListingDataForStore( storeId ).isArticleChosenForStockingUp( sapId );
    }

    public boolean hasGoodRotationInStore( Integer sapId, Integer storeId ) {
        return
                isStoreIdPresent( storeId ) &&
                getListingDataForStore( storeId ).getListingItem( sapId ).getRotation() < Config.MAX_ROTATION_VALUE;
    }

    public ListingDataForStore getListingDataForStore( Integer storeId ) {
        return storesListingNodes.get( storeId );
    }

}
