package pl.dev4lazy.shit_exchange.model;

import pl.dev4lazy.shit_exchange.utils.values.Value;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class NeededValuesMapped {

    protected HashMap<String, Value> valuesMapped = new HashMap<>();
    protected ArrayList<String> neededHeaders;

    public ArrayList<String> getNeededHeaders() {
        return neededHeaders;
    }

    public HashMap<String, Value> getValuesMapped() {
        return valuesMapped;
    }

    public boolean isHeaderNeeded( String header ) {
        return neededHeaders.contains( header );
    }
    public void addValue(Value value, String header ) {
        valuesMapped.put( header, value );
    }

    public boolean hasCorrectAmountOfValues() {
        return getValuesMapped().size() == getNeededHeaders().size();
    }

    public boolean hasNoCorrectAmountOfValues() {
        return !hasCorrectAmountOfValues();
    }
}
