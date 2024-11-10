package pl.dev4lazy.shit_exchange.model.initial_exclusions;

import pl.dev4lazy.shit_exchange.model.NeededValuesMapped;

import java.util.ArrayList;
import java.util.Arrays;

public class InitialExclusionValuesMapped extends NeededValuesMapped {

    public InitialExclusionValuesMapped() {
        neededHeaders = new ArrayList<>(
                Arrays.asList(
                        "Sap Id",
                        "EAN",
                        "Sklep dawca"
                )
        );
    }

}
