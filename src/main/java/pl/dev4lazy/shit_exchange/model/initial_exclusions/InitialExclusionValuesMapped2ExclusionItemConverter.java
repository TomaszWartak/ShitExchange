package pl.dev4lazy.shit_exchange.model.initial_exclusions;

public class InitialExclusionValuesMapped2ExclusionItemConverter {
    private final String sapIdName = "Sap Id";
    private final String eanCode = "EAN";
    private final String storeIdName = "Sklep dawca"; // 4 pierwsze znaki

    private InitialExclusionItem.InitialExclusionItemBuilder initialExclusionItemBuilder = new InitialExclusionItem.InitialExclusionItemBuilder();

    public InitialExclusionItem convertMappedExcludingItem2ExcludingItem( InitialExclusionValuesMapped initialExclusionValuesMapped ) {
        InitialExclusionItem initialExclusionItem = initialExclusionItemBuilder
                .withSapId( (Integer) initialExclusionValuesMapped.getValuesMapped().get( sapIdName ).getValue() )
                .withEanCode( (Long) initialExclusionValuesMapped.getValuesMapped().get( eanCode ).getValue() )
                .withStoreId( (Integer) initialExclusionValuesMapped.getValuesMapped().get( storeIdName ).getValue() )
                .build();
        return initialExclusionItem;
    }

}
