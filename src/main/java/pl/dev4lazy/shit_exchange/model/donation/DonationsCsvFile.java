package pl.dev4lazy.shit_exchange.model.donation;

import pl.dev4lazy.shit_exchange.utils.values.Value;
import pl.dev4lazy.shit_exchange.utils.csv_service.CsvFile;
import pl.dev4lazy.shit_exchange.utils.values.DoubleValue;
import pl.dev4lazy.shit_exchange.utils.values.IntegerValue;
import pl.dev4lazy.shit_exchange.utils.values.LongValue;
import pl.dev4lazy.shit_exchange.utils.values.StringValue;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class DonationsCsvFile extends CsvFile {

    private final ArrayList<Value> donationsCsvValuesTypes = new ArrayList<>(
            List.of(
                    // todo? new IntegerValue(),
                    new StringValue(),
                    new IntegerValue(),
                    new LongValue(),
                    new StringValue(),
                    new DoubleValue(),
                    new DoubleValue(),
                    new DoubleValue(),
                    new DoubleValue(),
                    new StringValue(),
                    new DoubleValue(),
                    // todo? new IntegerValue(),
                    new StringValue(),
                    new StringValue(),
                    new StringValue(),
                    new StringValue(),
                    new DoubleValue(),
                    new DoubleValue(),
                    new DoubleValue(),
                    new DoubleValue()
            )
    );

    public DonationsCsvFile(String fileName, Charset charset, String csvSeparator, boolean hasHeader ) {
        super( fileName, charset, csvSeparator, hasHeader );
        setCsvValuesTypes( donationsCsvValuesTypes );
        if (hasHeader()) {
            setParsedHeaderRow();
        }
    }

    private void setParsedHeaderRow( ) {
        ArrayList<String> elements = new ArrayList<>(
                List.of(
                        "Sklep dawca",
                        "Sap Id",
                        "EAN",
                        "Nazwa artykułu",
                        "Wartość rezerwy",
                        "ilość szt. do wyzerowaia rezerwy ",
                        "Zapas dawcy - szt.",
                        "Zapas dawcy - wartość",
                        "Kategoria wiodąca",
                        "COGS 12M dawcy",
                        "Sklep biorca",
                        "Rodzaj planogramu biorcy",
                        "Rodzaj zatowarowania biorcy",
                        "Wybór zatowarowania",
                        "Zapas biorcy - szt.",
                        "Zapas biorcy - wartość",
                        "Sprzedaż biorcy - wartość",
                        "Sprzedaż biorcy - ilość",
                        "Rotacja biorcy"
                )
        );
        setParsedCsvHeaderRow( elements );
    }

}
