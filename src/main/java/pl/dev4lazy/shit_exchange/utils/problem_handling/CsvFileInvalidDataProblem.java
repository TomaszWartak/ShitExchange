package pl.dev4lazy.shit_exchange.utils.problem_handling;

import pl.dev4lazy.shit_exchange.utils.csv_service.CsvFile;

import java.util.ArrayList;

public class CsvFileInvalidDataProblem extends CsvFileDataProblem {

    private ArrayList<String> headersOfNotValidValues;

    public CsvFileInvalidDataProblem(
                        Logger logger,
                        String description,
                        CsvFile csvFile,
                        int rowNr,
                        ArrayList<String> parsedCsvLine,
                        ArrayList<String> headersOfNotInvalidValues ) {
        super( logger, description, csvFile, rowNr, parsedCsvLine );
        this.headersOfNotValidValues = headersOfNotInvalidValues;
    }

    @Override
    public void handle() {
        super.handle();
        printParsedCsvLine();
    }

    private void printParsedCsvLine() {
        logger.emptyLine();
        logger.logInfo( "Zawartość odczytanego wiersza:" );
        ArrayList<String> parsedCsvHeaderLine = csvFile.getParsedCsvHeaderLine();
        int parsedCsvHeaderLineSize = parsedCsvHeaderLine.size();
        for (int index = 0; index < parsedCsvHeaderLineSize; index++) {
            String parsedCsvHeader = parsedCsvHeaderLine.get( index );
            String parsedCsvValue;
            if (index < parsedCsvLine.size()) {
                parsedCsvValue = parsedCsvLine.get( index );
                if (headersOfNotValidValues.contains( parsedCsvHeader) ) {
                    logger.logErr( parsedCsvHeader+" = "+parsedCsvValue );
                } else {
                    logger.logInfo( parsedCsvHeader+" = "+parsedCsvValue );
                }
            } else {
                logger.logErr( parsedCsvHeader+" = "+"BRAK WARTOSCI" );
            }
        }
    }

}
