package pl.dev4lazy.shit_exchange.utils.csv_service;

import java.util.ArrayList;


public class CsvSerializer implements Serializer {

    @Override
    public String serialize(ArrayList<String> pieces, String delimiter) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String piece : pieces) {
            stringBuilder
                    .append( piece )
                    .append( delimiter );
        }
        deleteLastDelimiter(stringBuilder);
        return stringBuilder.toString();
    }

    private static void deleteLastDelimiter(StringBuilder stringBuilder) {
        if (!stringBuilder.isEmpty()) {
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
        }
    }

}
