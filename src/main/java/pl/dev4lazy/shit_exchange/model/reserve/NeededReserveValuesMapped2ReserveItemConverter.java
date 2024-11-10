package pl.dev4lazy.shit_exchange.model.reserve;

import pl.dev4lazy.shit_exchange.utils.values.Value;

import java.util.HashMap;

public class NeededReserveValuesMapped2ReserveItemConverter {
    private final String sapIdName = "Numer artykułu";
    private final String eanCode = "EAN";
    private final String storeName = "Sklep"; // 4 pierwsze znaki
    private final String articleName = "Nazwa";
    private final String valueName = "Rezerwa ostatni tydzień [wartość]";
    private final String quantityToZeroReserveName = "Ilość w rez. do oddania";
    private final String stockQuantityName = "Zapas ilość";
    private final String stockValueName = "Zapas";
    private final String statusName = "Status";
    private final String sectorName = "Kategoria wiodąca";
    private final String cogs12MName = "COGS 12M";
    private Double rotation; // wartość COGS12 w pliku jest chyba nieprawidłowa,
    // więc nie liczę jej z rezerwy (zostawiam null) - wykorzystam rezerwę z listingu, choć nie wiem, jaki będzie skutek...

    private ReserveItem.ReserveItemBuilder reserveItemBuilder = new ReserveItem.ReserveItemBuilder();

    public ReserveItem convertMappedReserveItem2ReserveItem( NeededReserveValuesMapped neededReserveValuesMapped ) {
        ReserveItem reserveItem = reserveItemBuilder
                .withSapId( (Integer) neededReserveValuesMapped.getValuesMapped().get( sapIdName ).getValue() )
                .withEanCode( (Long) neededReserveValuesMapped.getValuesMapped().get( eanCode ).getValue() )
                .withArticleName( (String) neededReserveValuesMapped.getValuesMapped().get( articleName ).getValue() )
                .withValue( (Double) neededReserveValuesMapped.getValuesMapped().get( valueName ).getValue() )
                .withQuantityToZeroReserve( (Double) neededReserveValuesMapped.getValuesMapped().get( quantityToZeroReserveName ).getValue() )
                .withStockQuantity( (Double) neededReserveValuesMapped.getValuesMapped().get( stockQuantityName ).getValue() )
                .withStockValue( (Double) neededReserveValuesMapped.getValuesMapped().get( stockValueName ).getValue() )
                .withSector( (String) neededReserveValuesMapped.getValuesMapped().get( sectorName ).getValue() )
                .withCogs12M( (Double) neededReserveValuesMapped.getValuesMapped().get( cogs12MName ).getValue() )
                .withStoreName( (String) neededReserveValuesMapped.getValuesMapped().get(storeName).getValue() )
                .build();
        String fullStoreName = (String) neededReserveValuesMapped.getValuesMapped().get(storeName).getValue();
        reserveItem.setStoreId( Integer.valueOf( fullStoreName.substring(0,4) ) );
        /* wartość COGS12 w pliku jest chyba nieprawidłowa,więc nie liczę jej z rezerwy - wykorzystam z listingu,
        choć nie wiem, jaki będzie skutek...
        if (reserveItem.getCogs12M().intValue()==0) {
            reserveItem.setRotation( Double.MAX_VALUE );
        } else {
            reserveItem.setRotation( reserveItem.getStockValue() / reserveItem.getCogs12M() * 365 );
        }*/
        return reserveItem;
    }

    public boolean reserveItemMappedHasStatusActive( HashMap<String, Value> mappedReserveItem ) {
        return (Integer) mappedReserveItem.get( statusName ).getValue()==20;
    }
}
