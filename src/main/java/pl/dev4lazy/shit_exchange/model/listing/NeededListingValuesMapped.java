package pl.dev4lazy.shit_exchange.model.listing;

import pl.dev4lazy.shit_exchange.model.NeededValuesMapped;

import java.util.ArrayList;
import java.util.Arrays;

public class NeededListingValuesMapped extends NeededValuesMapped {

    public NeededListingValuesMapped() {
        neededHeaders = new ArrayList<>(
                Arrays.asList(
                        "SAP Id",
                        "Sklep nowy nr",
                        "Sklep+Nazwa",
                        "Zapas ilość na dzień",
                        "Zapas wartość na dzień",
                        "Sprzedaż netto 12 m",
                        "Wskaźnik marży 12 m",
                        "Typ plano",
                        "Typ zatowarow.",
                        "Twój wybór zatowar."
                )
        );
    }
}
