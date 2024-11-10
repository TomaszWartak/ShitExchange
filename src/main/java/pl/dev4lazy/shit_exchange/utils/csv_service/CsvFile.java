package pl.dev4lazy.shit_exchange.utils.csv_service;

import pl.dev4lazy.shit_exchange.utils.values.Value;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

/*
 * TODO opisz jak używać. Podaj również przykłąd klasy
 */
public class CsvFile {

    private final String fileName;
    private final String charsetName;
    // przesunąłeś wiedzę o separatorze do CsvUtils, ale w sumie każdy plik może mieć inny separator
    // choć rzeczywiście w zasadzie jedynym separatorem jest ";", który jest ustawiony w CsvUtils
    private final String csvSeparator;
    private final boolean hasHeader;
    private ArrayList<String> parsedCsvHeaderRow;
    private ArrayList<Value> csvValuesTypes;
    private CsvFileReader csvFileReader;
    private CsvFileWriter csvFileWriter;

    public CsvFile( String fileName, Charset charset, String csvSeparator, boolean hasHeader ) {
        this.fileName = fileName;
        this.charsetName = charset.displayName();
        this.csvSeparator = csvSeparator;
        this.hasHeader = hasHeader;
    }

    public CsvFile( CsvFile csvFile ) {
        this.fileName = csvFile.fileName;
        this.charsetName = csvFile.charsetName;
        this.csvSeparator = csvFile.csvSeparator;
        this.hasHeader = csvFile.hasHeader;
    }

    public boolean exists() {
        File file = new File( fileName );
        return file.exists();
    }

    public boolean notExits() {
        return !exists();
    }

    public void setParsedCsvHeaderRow( ArrayList<String> parsedCsvHeaderRow ) {
        this.parsedCsvHeaderRow = parsedCsvHeaderRow;
    }

    public ArrayList<String> getParsedCsvHeaderLine() {
        return parsedCsvHeaderRow;
    }

    public String getCsvHeaderRow() {
        return CsvUtils.serializeToCsvLine( parsedCsvHeaderRow );
    }

    public boolean hasHeader() {
        return hasHeader;
    }

    public void openForRead() throws IOException {
        openReader();
    }

    public void skipRow() throws IOException {
        if (csvFileReader!=null)  {
           readCsvLine();
        }
    }

    public void readHeaderRow() throws IOException {
        openReader();
        String csvHeaderRow = readCsvLine();
        setParsedCsvHeaderRow( CsvUtils.parseCsvLine( csvHeaderRow ) );
        closeReader();
    }

    protected void setCsvValuesTypes( ArrayList<Value> csvValuesTypes ) {
        this.csvValuesTypes = csvValuesTypes;
    }

    protected ArrayList<Value> getCsvValuesTypes() {
        return csvValuesTypes;
    }

    public void openForReading() throws IOException {
        openReader();
    }

    private void openReader() throws IOException {
        if (isWriterOpened()) {
            closeWriter();
        }
        if (isReaderNotOpened()) {
            csvFileReader = new CsvFileReader( getFileName(), getCharsetName() );
        }
    }

    private void closeReader() throws IOException {
        if (csvFileReader != null) {
            csvFileReader.close();
            csvFileReader = null;
        }
    }

    public boolean isReaderOpened() {
        return csvFileReader!=null;
    }

    public boolean isReaderNotOpened() {
        return !isReaderOpened();
    }

    public String readCsvLine() throws IOException {
        return csvFileReader.readCsvLine();
    }

    public ArrayList<String> readParsedDataFromCsvLine() throws IOException {
        String csvLine = csvFileReader.readCsvLine();
        return CsvUtils.parseCsvLine( csvLine );
    }

    public ArrayList<String> readAllCsvLines() throws IOException {
        return csvFileReader.readAllCsvLines();
    }

    public void openForWriting() throws IOException {
        openWriter();
    }

    public void openWriter() throws IOException {
        if (isReaderOpened()) {
            closeReader();
        }
        if (isWriterNotOpened()) {
            csvFileWriter = new CsvFileWriter(getFileName(), getCharsetName());
        }
    }

    public void closeWriter() throws IOException {
        if (csvFileWriter!=null) {
            csvFileWriter.close();
            csvFileWriter = null;
        }
    }

    public boolean isWriterOpened() {
        return csvFileWriter!=null;
    }

    public boolean isWriterNotOpened() {
        return !isWriterOpened();
    }

    public void writeCsvLine( String csvLine ) throws IOException {
        csvFileWriter.writeCsvLine( csvLine );
    }

    public void writeCsvLines( ArrayList<String> csvLines ) throws IOException {
        csvFileWriter.writeCsvLines( csvLines);
    }


    public String getFileName() {
        return fileName;
    }

    public String getCharsetName() {
        return charsetName;
    }

    public long getEstimateCsvFileLinesQuantity( ) throws IOException {
        return csvFileReader.getEstimateCsvFileLinesQuantity();
    }

    public void close() throws IOException {
        closeReader();
        closeWriter();
    }
}

