package pl.dev4lazy.shit_exchange.utils.problem_handling;

import pl.dev4lazy.shit_exchange.utils.values.Value;
import pl.dev4lazy.shit_exchange.model.NeededValuesMapped;
import pl.dev4lazy.shit_exchange.utils.csv_service.CsvFile;

import java.util.ArrayList;

public class CsvFileMissingDataProblem extends CsvFileDataProblem {

    private NeededValuesMapped neededValuesMapped;

    public CsvFileMissingDataProblem(
                                    Logger logger,
                                    String description,
                                    CsvFile csvFile,
                                    int rowNr,
                                    ArrayList<String> parsedCsvLine,
                                    NeededValuesMapped neededValuesMapped
                               ) {
        super( logger, description, csvFile, rowNr, parsedCsvLine );
        this.neededValuesMapped = neededValuesMapped;
    }

    @Override
    public void handle() {
        super.handle();
        printMappedCsvLineValues();
    }

    private void printMappedCsvLineValues() {
        logger.emptyLine();
        logger.logInfo( "Zawartość mapy listingu:" );
        for ( String parsedListingNeededHeader : neededValuesMapped.getNeededHeaders() ) {
            Value value = neededValuesMapped.getValuesMapped().get( parsedListingNeededHeader );
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
