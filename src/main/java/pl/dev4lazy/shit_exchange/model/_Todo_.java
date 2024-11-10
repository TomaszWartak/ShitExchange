package pl.dev4lazy.shit_exchange.model;

import pl.dev4lazy.shit_exchange.model.listing.ListingCsvFile;
import pl.dev4lazy.shit_exchange.model.reserve.ReserveCsvFile;
import pl.dev4lazy.shit_exchange.utils.csv_service.CsvFile;
import pl.dev4lazy.shit_exchange.utils.csv_service.CsvUtils;
import pl.dev4lazy.shit_exchange.utils.problem_handling.AppError;

import java.nio.charset.Charset;

public class _Todo_ {

    private CsvFile csvFile;

    public void showFilesInfo() {
        try {
            csvFile = new ListingCsvFile(
                    DataGrip.getInputListingFileNameFullPath(),
                    Charset.forName("Windows-1250"),
                    CsvUtils.CSV_SEPARATOR,
                    CsvUtils.HAS_HEADER);
            csvFile.openForReading();
            long estimateListingFileLinesQuantity = csvFile.getEstimateCsvFileLinesQuantity();
            csvFile.close();
            csvFile = new ReserveCsvFile(
                    DataGrip.getInputReserveFileNameFullPath(),
                    Charset.forName("Windows-1250"),
                    CsvUtils.CSV_SEPARATOR,
                    CsvUtils.HAS_HEADER);
            csvFile.openForReading();
            long estimateReserveFileLinesQuantity = csvFile.getEstimateCsvFileLinesQuantity();
            csvFile.close();
            ToolsGrip.handleNotification(
                    "Plik rezerwy: ok."+ roundToTenThousand( estimateReserveFileLinesQuantity )+" linii"
            );
            ToolsGrip.handleNotification(
                    "Plik listingu: ok."+ roundToTenThousand( estimateListingFileLinesQuantity )+" linii"
            );
        } catch (Exception e) {
            ToolsGrip.handleError( new AppError( e ) );
        }
    }

    private long roundToTenThousand(long value ) {
        return value / 10000 * 10000;
    }
}
