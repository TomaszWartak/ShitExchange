package pl.dev4lazy.shit_exchange.model.reserve;

import pl.dev4lazy.shit_exchange.model.NeededValuesMapped;

import java.util.ArrayList;
import java.util.Arrays;

public class NeededReserveValuesMapped extends NeededValuesMapped {

    public NeededReserveValuesMapped() {
        neededHeaders = new ArrayList<>(
                Arrays.asList(
                        "Numer artykułu",
                        "EAN",
                        "Sklep",
                        "Nazwa",
                        "Rezerwa ostatni tydzień [wartość]",
                        "Ilość w rez. do oddania",
                        "Zapas ilość",
                        "Zapas",
                        "Status",
                        "Kategoria wiodąca",
                        "COGS 12M"
                )
        );
    }

    public boolean isStatusActive() {
        return (Integer) getValuesMapped().get( "Status" ).getValue()==20;
    }
}
