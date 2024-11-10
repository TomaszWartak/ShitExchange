package pl.dev4lazy.shit_exchange.model.reserve;

import pl.dev4lazy.shit_exchange.Config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class ReserveData implements Serializable {

    private HashMap<Integer, ReserveDataForStore> storesReserveNodes = new HashMap<>(10);

    public HashMap<Integer, ReserveDataForStore> getStoresReserveNodes() {
        return storesReserveNodes;
    }

    public void setStoresReserveNodes(Object nodes) {
        storesReserveNodes = (HashMap<Integer, ReserveDataForStore>) nodes;
    }

    public void addReserveItem( ReserveItem reserveItem ) {
        if (isNotStoreIdPresent( reserveItem.getStoreId() ) ) {
            createStoreNode( reserveItem );
        }
        putReserveItem( reserveItem );
    }

    private void putReserveItem(ReserveItem reserveItem) {
        storesReserveNodes.get( reserveItem.getStoreId() ).putReserveItem( reserveItem );
    }

    private void createStoreNode( ReserveItem reserveItem ) {
        ReserveDataForStore reserveDataForStore = new ReserveDataForStore( reserveItem.getStoreId() );
        storesReserveNodes.put( reserveDataForStore.getStoreId(), reserveDataForStore );
    }

    private boolean isStoreIdPresent( Integer storeId ) {
        return storesReserveNodes.containsKey(storeId);
    }

    private boolean isNotStoreIdPresent (Integer storeId ) {
        return !isStoreIdPresent(storeId);
    }

    public ReserveDataForStore getReserveDataForStore( Integer storeId ) {
        return storesReserveNodes.get( storeId );
    }

    public boolean isNotInReserveInStore( ReserveItem reserveItem, Integer storeId ) {
        return getReserveDataForStore( storeId ).isNotInReserve( reserveItem );
    }

    public ArrayList<Integer> getAllDonorsStoreIds() {
        return new ArrayList<>( getStoresReserveNodes().keySet() );
    }

    public void excludeStoreFromDonations( Integer storeId ) {
        if (isStoreIdPresent( storeId )) {
            getReserveDataForStore( storeId ).excludeFromDonations();
        }
    }

    public boolean isStoreExcludedFromDonations(  Integer storeId ) {
        return
                isStoreIdPresent( storeId ) &&
                getReserveDataForStore( storeId ).isExcludedFromDonations();
    }

    public boolean isStoreNotExcludedFromDonations( Integer storeId ) {
        return !isStoreExcludedFromDonations( storeId );
    }

    public void excludeArticleFromDonationsForAllStores( Integer sapId ) {
        for (Integer storeId : getAllDonorsStoreIds()) {
            if ( !storeId.equals( Config.ONE_DONOR_ONLY_ID ) &&
                isStoreNotExcludedFromDonations( storeId )) {
                    excludeArticleInStoreFromDonations( storeId, sapId );
            }
        }
    }

    public void excludeArticleInStoreFromDonations( Integer storeId, Integer sapId ) {
        if (isStoreIdPresent( storeId )) {
            getReserveDataForStore( storeId ).excludeReserveItemFromDonations( sapId );
        }
    }

    public void excludeManyArticlesFromDonationsForNotExcludedStores(ArrayList<Integer> sapIds ) {
        for (Integer storeId : getAllDonorsStoreIds()) {
            if ( !storeId.equals( Config.ONE_DONOR_ONLY_ID ) &&
                    isStoreNotExcludedFromDonations( storeId )) {
                // TODO START TEST
                ArrayList<ReserveItem> excludedItems =
                        getReserveDataForStore( storeId ).getReserveItemsExcludedFromDonations();
                excludedItems = null;
                // TODO END TEST
                excludeManyArticlesInStoreFromDonations( storeId, sapIds );
                // TODO START TEST
                excludedItems =
                    getReserveDataForStore( storeId ).getReserveItemsExcludedFromDonations();
                excludedItems = null;
                // TODO END TEST
            }
        }
    }

    public void excludeManyArticlesInStoreFromDonations( Integer storeId, ArrayList<Integer> sapIds ) {
        if (isStoreIdPresent( storeId )) {
            getReserveDataForStore( storeId ).excludeManyReserveItemsFromDonations( sapIds );
        }
    }

    public void includeStoreForDonations( Integer storeId ) {
        if (isStoreIdPresent( storeId )) {
            getReserveDataForStore( storeId ).includeForDonations();
        }
    }

    public void includeArticleForDonations( Integer sapId ) {
        for (Integer storeId : getAllDonorsStoreIds()) {
            if (!storeId.equals(Config.ONE_DONOR_ONLY_ID)) {
                if (isStoreNotExcludedFromDonations( storeId )) {
                    getReserveDataForStore( storeId ).includeReserveItemForDonations( sapId );
                }
            }
        }
    }

    public ArrayList<Integer> getExcludedFromDonationsStoreIds() {
        return storesReserveNodes
                .values()
                .stream()
                .filter( reserveDataForStore -> reserveDataForStore.isExcludedFromDonations() )
                .map( reserveDataForStore -> reserveDataForStore.getStoreId() )
                .collect(Collectors.toCollection( ArrayList::new) );
    }

    public ArrayList<Integer> getNotExcludedFromDonationsStoreIds() {
        return storesReserveNodes
                .values()
                .stream()
                .filter( reserveDataForStore -> reserveDataForStore.isNotExcludedFromDonations() )
                .map( reserveDataForStore -> reserveDataForStore.getStoreId() )
                .collect(Collectors.toCollection( ArrayList::new) );
    }

}
