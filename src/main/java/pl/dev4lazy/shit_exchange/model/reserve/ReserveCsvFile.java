package pl.dev4lazy.shit_exchange.model.reserve;

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

public class ReserveCsvFile extends CsvFile {

    private final ArrayList<Value> reserveCsvValuesTypes = new ArrayList<>(
            List.of(
                    new StringValue( Value.NULL_NOT_ALLOWED ), // Sklep
                    new StringValue(), // Region
                    new LongValue( Value.NULL_NOT_ALLOWED ), // EAN
                    new IntegerValue( Value.NULL_NOT_ALLOWED ), // Numer artykułu
                    new IntegerValue(), // Artykuł podłączony
                    new StringValue( Value.NULL_NOT_ALLOWED ), // Nazwa
                    new DoubleValue( Value.NULL_NOT_ALLOWED ), // Rezerwa ostatni tydzień [wartość]
                    new DoubleValue(), // Rezerwa ostatni tydzień %
                    new DoubleValue( Value.NULL_NOT_ALLOWED ), // Ilość w rez. do oddania
                    new DoubleValue(), // Rezerwa poprzedni tydzień
                    new DoubleValue(), // Zmiana rezerwy tydzień do tygodnia
                    new DoubleValue(), // Zmiana rezerwy tydzień do tygodnia %
                    new DoubleValue(), // Marża %
                    new DoubleValue( Value.NULL_NOT_ALLOWED ), // COGS 12M
                    new DoubleValue(), // COGS ost. tydz.
                    new DoubleValue( Value.NULL_NOT_ALLOWED ), // Zapas
                    new DoubleValue( Value.NULL_NOT_ALLOWED ), // Zapas ilość
                    new IntegerValue( Value.NULL_NOT_ALLOWED ), // Status
                    new StringValue(), // SKU nowe
                    new StringValue(), // SKU out of range
                    new StringValue(), // Data nadania statusu out of range
                    new StringValue(), // Data usunięcia z planogramu
                    new StringValue(), // Przedział wiekowania
                    new StringValue(), // SKU w planogramie
                    new StringValue(), // Data zmiany statusu
                    new StringValue(), // Tytuł wyłączenia
                    new DoubleValue(), // Rezerwa przed wyłączeniami
                    new DoubleValue(), // Estymowana rez. +6M
                    new StringValue(), // 6M do wycof. z nowości
                    new StringValue(), // Art. Out of range - 90 dni bez rezerwy
                    new StringValue(), // Data pierwszego zatowarowania
                    new StringValue(), // Unhealthy Stock Delisted Val
                    new StringValue(), // Unhealthy Stock Ranged Val
                    new StringValue(), // MOQ
                    new StringValue(), // Powód wzrostu rezerwy
                    new DoubleValue(), // Cena sprzedaży brutto
                    new StringValue(), // PQ
                    new DoubleValue(), // Sztuki potrzebne do zejścia z całej rezerwy
                    new DoubleValue(), // Zapas ekspozycja ilość
                    new StringValue( Value.NULL_NOT_ALLOWED ), // Kategoria wiodąca
                    new StringValue(), // Kategoria
                    new StringValue(), // Podkategoria
                    new StringValue(), // Podpodkategoria
                    new StringValue(), // Brick
                    new StringValue(), // Brick nazwa
                    new StringValue(), // Dostawca
                    new IntegerValue(), // Kod dostawcy
                    new StringValue(), // Footprint
                    new StringValue() // RTM
            )
    );

    public ReserveCsvFile( String fileName, Charset charset, String csvSeparator, boolean hasHeader )
            throws IOException {
        super( fileName, charset, csvSeparator, hasHeader );
        setCsvValuesTypes( reserveCsvValuesTypes );
        if (hasHeader()) {
            readHeaderRow();
        }
    }

}
