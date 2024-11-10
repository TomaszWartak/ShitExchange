package pl.dev4lazy.shit_exchange.model.listing;

import pl.dev4lazy.shit_exchange.utils.values.Value;
import pl.dev4lazy.shit_exchange.utils.csv_service.CsvFile;
import pl.dev4lazy.shit_exchange.utils.values.DoubleValue;
import pl.dev4lazy.shit_exchange.utils.values.IntegerValue;
import pl.dev4lazy.shit_exchange.utils.values.LongValue;
import pl.dev4lazy.shit_exchange.utils.values.StringValue;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ListingCsvFile extends CsvFile {

    private final ArrayList<Value> listingCsvValuesTypes = new ArrayList<>(
            List.of(
                    new IntegerValue( Value.NULL_NOT_ALLOWED ), // SAP Id
                    new StringValue(), // Nowy produkt
                    new LongValue(), // Kod EAN
                    new IntegerValue(), // Kod casto
                    new StringValue(), // Nr plano
                    new DoubleValue( Value.NULL_NOT_ALLOWED ), // Zapas ilość na dzień
                    new DoubleValue( Value.NULL_NOT_ALLOWED ), // Zapas wartość na dzień
                    new StringValue(), // Dostępny 12 m
                    new DoubleValue( Value.NULL_NOT_ALLOWED ), // Sprzedaż netto 12 m
                    new DoubleValue( Value.NULL_NOT_ALLOWED ), // Wskaźnik marży 12 m
                    new StringValue(), // data dodania do plano
                    new StringValue(), // Poprzedni wybór TAK / NIE zamawiam
                    new StringValue( Value.NULL_NOT_ALLOWED ), // Twój wybór zatowar.
                    new DoubleValue(), // Facing plano
                    new DoubleValue(), // Zapas prezentacyjny na 1 facing
                    new DoubleValue(), // Facing kierownika
                    new DoubleValue(), // Facing kierownika zeszły tydzień
                    new StringValue(), // Data modyfikacji
                    new StringValue(), // Nazwa artykułu
                    new IntegerValue(), // Dział
                    new IntegerValue(), // Nr dostawcy
                    new StringValue( Value.NULL_NOT_ALLOWED ), // Typ plano
                    new StringValue(), // Plano nazwa
                    new StringValue(), // Wiek artykułu
                    new StringValue( Value.NULL_NOT_ALLOWED ), // Typ zatowarow.
                    new StringValue(), // Dostawca
                    new StringValue(), // Status art.
                    new StringValue(), // Kto modyfikował
                    new IntegerValue(), // Sklep
                    new IntegerValue( Value.NULL_NOT_ALLOWED ), // Sklep nowy nr
                    new StringValue( Value.NULL_NOT_ALLOWED ), // Sklep => Sklep+Nazwa)
                    new StringValue() // Region

            )
    );

    public ListingCsvFile( String fileName, Charset charset, String csvSeparator, boolean hasHeader )
            throws IOException {
        super( fileName, charset, csvSeparator, hasHeader );
        setCsvValuesTypes( listingCsvValuesTypes );
        if (hasHeader()) {
            readHeaderRow();
        }
    }

}
