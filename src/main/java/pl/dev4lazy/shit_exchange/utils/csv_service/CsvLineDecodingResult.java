package pl.dev4lazy.shit_exchange.utils.csv_service;

import pl.dev4lazy.shit_exchange.utils.values.Value;

import java.util.ArrayList;
import java.util.HashMap;

public class CsvLineDecodingResult {
    private HashMap<String, Value> decodingResultMapped = new HashMap<>();
    private HashMap<String, Value> invalidValuesMapped = new HashMap<>();

    public HashMap<String, Value> getDecodingResultMapped() {
        return decodingResultMapped;
    }

    public void setDecodingResultMapped(HashMap<String, Value> decodingResultMapped) {
        this.decodingResultMapped = decodingResultMapped;
    }

    public HashMap<String, Value> getInvalidValuesMapped() {
        return invalidValuesMapped;
    }

    public void setInvalidValuesMapped(HashMap<String, Value> invalidValuesMapped) {
        this.invalidValuesMapped = invalidValuesMapped;
    }

    public ArrayList<String> getHeadersForInvalidValues() {
        return new ArrayList<>(invalidValuesMapped.keySet());
    }
}
