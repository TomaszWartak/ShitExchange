package pl.dev4lazy.shit_exchange.utils.csv_service;

import pl.dev4lazy.shit_exchange.utils.values.Value;
import pl.dev4lazy.shit_exchange.utils.values.DoubleValue;
import pl.dev4lazy.shit_exchange.utils.values.IntegerValue;
import pl.dev4lazy.shit_exchange.utils.values.LongValue;
import pl.dev4lazy.shit_exchange.utils.values.StringValue;

import java.util.ArrayList;

public class CsvLineToMappedItemDecoder implements Decoder< ArrayList<String>, CsvLineDecodingResult> {
// W sumie chyba wyszedł uniwersalny decoder :-)
    private CsvFile csvFile;

    public CsvLineToMappedItemDecoder( CsvFile csvFile ) {
        this.csvFile = csvFile;
    }

    @Override
    public CsvLineDecodingResult decode(ArrayList<String> parsedCsvLine ) {
        ArrayList<String> parsedCsvHeaderRow = csvFile.getParsedCsvHeaderLine();
        ArrayList<Value> csvValuesTypes = csvFile.getCsvValuesTypes();
        parsedCsvLine = CsvUtils.replaceCommasToPoints( parsedCsvLine );
        CsvLineDecodingResult decodingResult = new CsvLineDecodingResult();
        for (int index = 0; index < parsedCsvLine.size(); index++) {
            String element = parsedCsvLine.get(index);
            /* todo od wersji 21
            switch ( csvValuesTypes.get( index ) ) {
                case StringValue ()-> System.out.println("Jest to String: ");
                case DoubleValue:;
                case IntegerValue:;
                default:;
            }
             */
            Value value;
            Value valueType = csvValuesTypes.get( index );
            if ( valueType instanceof StringValue) {
                value = new StringValue( (StringValue)valueType );
                value.setValue( element );
            } else if ( csvValuesTypes.get(index) instanceof DoubleValue) {
                value = new DoubleValue( (DoubleValue)valueType );
                value.setValue( CsvUtils.getDoubleFromString( element ) );
            } else if ( csvValuesTypes.get(index) instanceof IntegerValue) {
                value = new IntegerValue( (IntegerValue)valueType );
                value.setValue( CsvUtils.getIntegerFromString( element ) );
            } else if ( csvValuesTypes.get(index) instanceof LongValue) {
                value = new LongValue( (LongValue) valueType );
                value.setValue( CsvUtils.getLongFromString( element ) );
            } else {
                throw new IllegalStateException();
            }
            decodingResult.getDecodingResultMapped().put( parsedCsvHeaderRow.get( index ), value );
            if ( valueIsNotDecoded( value ) ) {
                decodingResult.getInvalidValuesMapped().put( parsedCsvHeaderRow.get( index ), value );
            }
        }
        return decodingResult;
    }

    private boolean valueIsNotDecoded( Value value ) {
        return value.getValue()== null;
    }
}
