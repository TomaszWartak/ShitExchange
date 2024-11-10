package pl.dev4lazy.shit_exchange.model.reserve;

import pl.dev4lazy.shit_exchange.utils.problem_handling.Logger;
import pl.dev4lazy.shit_exchange.utils.values.Value;
import pl.dev4lazy.shit_exchange.utils.csv_service.CsvFile;
import pl.dev4lazy.shit_exchange.utils.problem_handling.CsvFileDataProblem;

import java.util.ArrayList;

public class ReserveCsvFileDataProblem extends CsvFileDataProblem {

    private NeededReserveValuesMapped neededReserveValuesMapped;

    public ReserveCsvFileDataProblem(
                                    Logger logger,
                                    String description,
                                    CsvFile csvFile,
                                    int rowNr,
                                    ArrayList<String> parsedCsvLine,
                                    NeededReserveValuesMapped neededReserveValuesMapped
                               ) {
        super( logger, description, csvFile, rowNr, parsedCsvLine );
        this.neededReserveValuesMapped = neededReserveValuesMapped;
    }

    @Override
    public void handle() {
        super.handle();
        printMappedCsvLineValues();
    }

    private void printMappedCsvLineValues() {
        logger.emptyLine();
        logger.logInfo( "Zawartość mapy listingu:" );
        for ( String parsedListingNeededHeader : neededReserveValuesMapped.getNeededHeaders() ) {
            Value value = neededReserveValuesMapped.getValuesMapped().get( parsedListingNeededHeader );
            if ( value == null ) {
                logger.logErr( parsedListingNeededHeader + " = BRAK WARTOSCI" );
            } else {
                if (value.getValue()==null) {
                    logger.logErr( parsedListingNeededHeader+" = "+value );
                } else {
                    logger.logInfo( parsedListingNeededHeader+" = "+value );
                }
            }
        }
    }
}
