package pl.dev4lazy.shit_exchange.utils.csv_service;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class CsvUtils {
    public static final String CSV_SEPARATOR = ";";
    public static final boolean HAS_HEADER = true;
    public static final boolean DOESNT_HAVE_HEADER = false;

    private static final CsvParser csvParser = new CsvParser();
    private static final CsvSerializer csvSerializer = new CsvSerializer();


    public static String replaceCommasToPoints( String csvLine ) {
        if (csvLine !=null) {
            csvLine = csvLine.replace(",", ".");
        }
        return csvLine;
    }

    public static ArrayList<String> replaceCommasToPoints( ArrayList<String> strings ) {
        return strings
                .stream()
                .map( string -> string.replace(",", ".") )
                .collect(Collectors.toCollection(ArrayList::new));
    }

     public static Integer getIntegerFromString( String value ) {
        value = value.replace(" ", "");
        if (value.isEmpty()) {
            return 0;
        } else {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }

    public static Long getLongFromString( String value ) {
        value = value.replace(" ", "");
        if (value.isEmpty()) {
            return 0L;
        } else {
            try {
                return Long.parseLong( value );
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }

    public static Double getDoubleFromString( String value ) {
        value = value.replace(" ", "");
        if (value.isEmpty()) {
            return 0.0;
        } else {
            try {
                return Double.parseDouble( value );
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }


    public static ArrayList<String> parseCsvLine( String csvLine ) {
        return csvParser.parse( csvLine );
    }

    public static String serializeToCsvLine(ArrayList<String> elements ) {
        return csvSerializer.serialize( elements, CSV_SEPARATOR );
    }


}
