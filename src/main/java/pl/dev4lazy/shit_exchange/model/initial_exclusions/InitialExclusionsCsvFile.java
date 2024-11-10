package pl.dev4lazy.shit_exchange.model.initial_exclusions;

import pl.dev4lazy.shit_exchange.utils.csv_service.CsvFile;
import pl.dev4lazy.shit_exchange.utils.values.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class InitialExclusionsCsvFile extends CsvFile {

    private final ArrayList<Value> exclusionsCsvValuesTypes = new ArrayList<>(
            List.of(
                    new IntegerValue( Value.NULL_NOT_ALLOWED ), // "Sklep dawca",
                    new LongValue( Value.NULL_NOT_ALLOWED ), // EAN
                    new IntegerValue( Value.NULL_NOT_ALLOWED ) // "Sap Id"
            )
    );

    public InitialExclusionsCsvFile(String fileName, Charset charset, String csvSeparator, boolean hasHeader )
            throws IOException {
        super( fileName, charset, csvSeparator, hasHeader );
        setCsvValuesTypes( exclusionsCsvValuesTypes );
        if (hasHeader()) {
            readHeaderRow();
        }
    }

    public InitialExclusionsCsvFile(CsvFile csvFile )
           throws IOException {
        super( csvFile );
        setCsvValuesTypes( exclusionsCsvValuesTypes );
        if (hasHeader()) {
            readHeaderRow();
        }
    }
}
