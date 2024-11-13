package pl.dev4lazy.shit_exchange.model.initial_exclusions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class InitialExclusionsData implements Serializable {

    private HashMap<Integer, InitialExclusionsDataForStore> excludedStoresIdNodes = new HashMap<>(10);

    private ArrayList<Integer> fullInitiallyExcludedStoresIds = new ArrayList<>();
    // róznica pomiędzy fullInitiallyExcludedStoresIds a storeIdNodes.keySet() jest taka,
    // że na liscie fullInitiallyExcludedStoresIds nie ma OneDonorId

    public void setNodes( Object nodes) {
        excludedStoresIdNodes = (HashMap<Integer, InitialExclusionsDataForStore>) nodes;
    }

    public HashMap<Integer, InitialExclusionsDataForStore> getNodes() {
        return excludedStoresIdNodes;
    }

    public boolean areInitialExclusions() {
        return getAllStoreIdsWithInitialExclusions().size()>0;
    }

    public void addInitialExclusionItem( InitialExclusionItem initialExclusionItem ) {
        if (isNotStoreIdPresent( initialExclusionItem.getStoreId() ) ) {
            createStoreIdInitialExclusionsNode( initialExclusionItem );
        }
        putInitialExclusionItem( initialExclusionItem );
    }

    private void putInitialExclusionItem( InitialExclusionItem initialExclusionItem ) {
        excludedStoresIdNodes.get( initialExclusionItem.getStoreId() ).putInitialExclusionItem( initialExclusionItem );
    }

    public boolean isArticleExcludedInStore(Integer storeId, Integer sapId ) {
        InitialExclusionsDataForStore initialExclusionsDataForStore = getInitialExclusionsDataForStore( storeId );
        if (initialExclusionsDataForStore==null) {
            return false;
        }
        return getInitialExclusionsDataForStore( storeId ).isArticleInitiallyExcluded( sapId );
    }

    private void createStoreIdInitialExclusionsNode( InitialExclusionItem initialExclusionItem ) {
        excludedStoresIdNodes.put( initialExclusionItem.getStoreId(), new InitialExclusionsDataForStore() );
    }

    private boolean isStoreIdPresent( Integer storeId ) {
        return excludedStoresIdNodes.containsKey(storeId);
    }

    private boolean isNotStoreIdPresent (Integer storeId ) {
        return !isStoreIdPresent(storeId);
    }

    public InitialExclusionsDataForStore getInitialExclusionsDataForStore(Integer storeId ) {
        return excludedStoresIdNodes.get( storeId );
    }

    public ArrayList<Integer> getInitiallyExcludedArticlesSapIdsForStore( Integer storeId ) {
        ArrayList<Integer> result = new ArrayList<>();
        if (isStoreIdPresent( storeId )) {
            result = getInitialExclusionsDataForStore( storeId  )
                    .getInitialExclusionItems()
                    .stream()
                    .map( iei -> iei.getSapId() )
                    .collect( Collectors.toCollection( ArrayList::new ));
        }
        return result;
    }
    /**
     *
     * @return oddaje listę wszystkich id sklepów z wykluczeniami (równiez id ONE_DONOR)
     */
    public ArrayList<Integer> getAllStoreIdsWithInitialExclusions() {
        return new ArrayList<>( getNodes().keySet() );
    }

    /**
     *
     * @param fullInitiallyExcludedStoresIds lista id sklepów z pełnym wykluczeniem (bez id ONE_DONOR)
     */
    public void setFullInitiallyExcludedStoresIds( ArrayList<Integer> fullInitiallyExcludedStoresIds ) {
        this.fullInitiallyExcludedStoresIds = fullInitiallyExcludedStoresIds;
    }

    /**
     *
     * @return oddaje listę id sklepów z pełnym wykluczeniem (bez id ONE_DONOR)
     */
    public ArrayList<Integer> getFullInitiallyExcludedStoresIds() {
        return fullInitiallyExcludedStoresIds;
    }

}
