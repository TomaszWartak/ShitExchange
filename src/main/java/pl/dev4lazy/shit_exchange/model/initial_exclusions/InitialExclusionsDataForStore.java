package pl.dev4lazy.shit_exchange.model.initial_exclusions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InitialExclusionsDataForStore implements Serializable {
    private HashMap<Integer, InitialExclusionItem> nodeBody = new HashMap<>(45000);

    public HashMap<Integer, InitialExclusionItem> getNodeBody() {
        return nodeBody;
    }

    public void putInitialExclusionItem( InitialExclusionItem initialExclusionItem ) {
        nodeBody.put( initialExclusionItem.getSapId(), initialExclusionItem);
    }

    public ArrayList<InitialExclusionItem> getInitialExclusionItems() {
        return new ArrayList<>( nodeBody.values() );
    }

    public boolean isArticleInitiallyExcluded(Integer sapId ) {
        return nodeBody.containsKey( sapId );
    }

}
