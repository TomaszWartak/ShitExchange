package pl.dev4lazy.shit_exchange.model.listing;

public class NeededListingValuesMapped2ListingItemConverter {
    private final String sapIdHeader = "SAP Id";
    private final String storeIdHeader = "Sklep nowy nr";
    private final String storeNameHeader = "Sklep+Nazwa";
    private final String stockQuantityHeader = "Zapas ilość na dzień";
    private final String stockValueHeader = "Zapas wartość na dzień";
    private final String sales12MHeader = "Sprzedaż netto 12 m";
    private final String margin12MHeader = "Wskaźnik marży 12 m";
    private final String planoKindHeader = "Typ plano";
    private final String typeOfStokingUpHeader = "Typ zatowarow.";
    private final String choiceOfStokingUpHeader = "Twój wybór zatowar.";


    private ListingItem.ListingItemBuilder listingItemBuilder = new ListingItem.ListingItemBuilder();

    public ListingItem convertNeededListingValuesMapped2ListingItem( NeededListingValuesMapped neededListingValuesMapped) {
        ListingItem listingItem = listingItemBuilder
                .withSapId( (Integer) neededListingValuesMapped.getValuesMapped().get(sapIdHeader).getValue() )
                .withStoreId( (Integer) neededListingValuesMapped.getValuesMapped().get(storeIdHeader).getValue() )
                .withStoreName( (String) neededListingValuesMapped.getValuesMapped().get(storeNameHeader).getValue() )
                .withStockQuantity( (Double) neededListingValuesMapped.getValuesMapped().get(stockQuantityHeader).getValue() )
                .withStockValue( (Double) neededListingValuesMapped.getValuesMapped().get(stockValueHeader).getValue() )
                .withSales12MValue( (Double) neededListingValuesMapped.getValuesMapped().get(sales12MHeader).getValue() )
                .withMargin12M( (Double) neededListingValuesMapped.getValuesMapped().get(margin12MHeader).getValue() )
                .withPlanoKind( (String) neededListingValuesMapped.getValuesMapped().get(planoKindHeader).getValue() )
                .withTypeOfStokingUp( (String) neededListingValuesMapped.getValuesMapped().get(typeOfStokingUpHeader).getValue() )
                .withChoiceOfStokingUp( (String) neededListingValuesMapped.getValuesMapped().get(choiceOfStokingUpHeader).getValue() )
                .build();
        listingItem.setCogs12M( listingItem.getSales12MValue() - listingItem.getMargin12M() );
        if (listingItem.getCogs12M().intValue()==0) {
            listingItem.setRotation( Double.MAX_VALUE );
            listingItem.setSales12MQuantity( 0.0 );
        } else {
            listingItem.setRotation( listingItem.getStockValue() / listingItem.getCogs12M() * 365 );
            // TODO to poniżej sprawdź
            double mapValue = listingItem.getStockValue() / listingItem.getStockQuantity();
            listingItem.setSales12MQuantity( listingItem.getCogs12M() / mapValue );
        }
        return listingItem;
    }

}
