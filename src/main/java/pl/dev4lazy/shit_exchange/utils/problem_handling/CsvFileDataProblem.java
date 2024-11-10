package pl.dev4lazy.shit_exchange.utils.problem_handling;

import pl.dev4lazy.shit_exchange.utils.csv_service.CsvFile;

import java.util.ArrayList;

public class CsvFileDataProblem implements Problem {
    protected Logger logger;
    private String description;
    protected CsvFile csvFile;
    private int rowNr;
    protected ArrayList<String> parsedCsvLine;

    public CsvFileDataProblem( 
                        Logger logger,
                        String description,
                        CsvFile csvFile,
                        int rowNr,
                        ArrayList<String> parsedCsvLine ) {
        this.logger = logger;
        this.description = description;
        this.csvFile = csvFile;
        this.rowNr = rowNr;
        this.parsedCsvLine = parsedCsvLine;
    }

    @Override
    public void handle() {
        logger.emptyLine();
        logger.logInfo( csvFile.getFileName() );
        logger.logInfo( description );
        logger.logInfo( "Wiersz nr: " + rowNr );
    }

}
