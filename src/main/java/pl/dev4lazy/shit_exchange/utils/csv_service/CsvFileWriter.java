package pl.dev4lazy.shit_exchange.utils.csv_service;

import java.io.*;
import java.util.ArrayList;

public class CsvFileWriter {

    private BufferedWriter writer;

    public CsvFileWriter(String csvFileName, String charsetName )
                 throws FileNotFoundException, UnsupportedEncodingException {
            openWriterForFile(csvFileName,charsetName);
    }

    private void openWriterForFile(String csvFileName, String charsetName)
            throws FileNotFoundException, UnsupportedEncodingException {
        OutputStream outputStream = new FileOutputStream(csvFileName);
        OutputStreamWriter outputStreamWriter;
        if (charsetName.isEmpty()) {
            outputStreamWriter = new OutputStreamWriter(outputStream);
        } else {
            outputStreamWriter = new OutputStreamWriter(outputStream, charsetName);
        }
        writer = new BufferedWriter(outputStreamWriter);
    }

    public void writeCsvLine( String csvLine ) throws IOException {
        boolean needToCloseWriter = false;
        try {
            writer.write( csvLine );
            writer.newLine();
        } catch(IOException ex1) {
            needToCloseWriter = true;
            throw ex1;
        } finally {
            if (needToCloseWriter && (writer != null)) {
                try {
                    writer.close();
                } catch (IOException ex2) {
                    throw ex2;
                }
            }
        }
    }

    public void writeCsvLines(ArrayList<String> allCsvLines ) throws IOException {
        for (String csvLine: allCsvLines) {
            writeCsvLine(csvLine);
        }
    }

    public void close() throws IOException {
        if (writer != null) {
            writer.close();
        }
    }

}
