package pl.dev4lazy.shit_exchange.model.reserve;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class ReserveDataForStore implements Serializable {
    private Integer storeId;

    private HashMap<Integer, ReserveItem> storeReserveItemsNode = new HashMap<>(45000);

    private boolean excludedFromDonations = false;

    public ReserveDataForStore(Integer storeId) {
        this.storeId = storeId;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public HashMap<Integer, ReserveItem> getStoreReserveItemsNode() {
        return storeReserveItemsNode;
    }

    public ArrayList<ReserveItem> getReserveItems() {
        return new ArrayList<>(storeReserveItemsNode.values());
    }

    public void putReserveItem( ReserveItem reserveItem ) {
        storeReserveItemsNode.put(reserveItem.getSapId(), reserveItem);
    }

    public boolean isInReserve( ReserveItem reserveItem ) {
        return storeReserveItemsNode.containsKey(reserveItem.getSapId());
    }

    public boolean isNotInReserve( ReserveItem reserveItem ) {
        return !isInReserve(reserveItem);
    }

    public boolean isExcludedFromDonations() {
        return excludedFromDonations;
    }

    public boolean isNotExcludedFromDonations() {
        return !isExcludedFromDonations();
    }

    public void excludeFromDonations( ) {
        this.excludedFromDonations = true;
    }

    public void includeForDonations( ) {
        this.excludedFromDonations = false;
    }


    public ArrayList<ReserveItem> getAllReserveItems() {
        return new ArrayList<>( storeReserveItemsNode.values() );
    }

    public ArrayList<Integer> getAllReserveItemsSapIds () {
        return getAllReserveItems()
                .stream()
                .map( donationItem -> donationItem.getSapId() )
                .collect( Collectors.toCollection( ArrayList::new ) );
    }

    public void excludeReserveItemFromDonations( Integer sapId ) {
        if ( storeReserveItemsNode.containsKey( sapId ) ) {
            storeReserveItemsNode.get( sapId ).setExcludedFromDonation();
        }
    }

    public void excludeManyReserveItemsFromDonations( ArrayList<Integer> sapIds ) {
        storeReserveItemsNode.entrySet()
                .stream()
                .filter( entry -> sapIds.contains( entry.getKey() ) )
                .forEach( entry -> entry.getValue().setExcludedFromDonation() );
    }

    public void includeReserveItemForDonations( Integer sapId ) {
        if ( storeReserveItemsNode.containsKey( sapId ) ) {
            storeReserveItemsNode.get( sapId ).setIncludedForDonation();
        }
    }

    public void includeManyReserveItemsForDonations( ArrayList<Integer> sapIds ) {
        storeReserveItemsNode.entrySet()
                .stream()
                .filter( entry -> !sapIds.contains( entry.getKey() ) )
                .forEach( entry -> entry.getValue().setIncludedForDonation() );
    }

    public ArrayList<ReserveItem> getReserveItemsExcludedFromDonations() {
        return storeReserveItemsNode.entrySet()
                .stream()
                .filter( entry -> entry.getValue().isExcludedFromDonation() )
                .map( entry -> entry.getValue() )
                .collect( Collectors.toCollection( ArrayList::new ));
    }
}
