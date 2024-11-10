package pl.dev4lazy.shit_exchange.model.analysis;

import pl.dev4lazy.shit_exchange.utils.values.Value;
import pl.dev4lazy.shit_exchange.utils.csv_service.CsvFile;
import pl.dev4lazy.shit_exchange.utils.values.DoubleValue;
import pl.dev4lazy.shit_exchange.utils.values.IntegerValue;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class AnalysisCsvFile extends CsvFile {

    private final ArrayList<Value> analysisCsvValuesTypes = new ArrayList<>(
            List.of(
                    new IntegerValue(),
                    new IntegerValue(),
                    new DoubleValue(),
                    new DoubleValue(),
                    new DoubleValue()
            )
    );

    public AnalysisCsvFile( String fileName, Charset charset, String csvSeparator, boolean hasHeader ) {
        super( fileName, charset, csvSeparator, hasHeader );
        setCsvValuesTypes(analysisCsvValuesTypes);
        if (hasHeader()) {
            setHeaderRow();
        }
    }

    private void setHeaderRow( ) {
        ArrayList<String> elements = new ArrayList<>(
                List.of(
                        "Sklep dawca",
                        "Sklep biorca",
                        "Rezerwa dawcy",
                        "Rezerwa biorcy",
                        "Różnica"
                )
        );
        setParsedCsvHeaderRow( elements );
    }

}
