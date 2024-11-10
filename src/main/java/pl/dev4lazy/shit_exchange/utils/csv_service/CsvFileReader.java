package pl.dev4lazy.shit_exchange.utils.csv_service;

import pl.dev4lazy.shit_exchange.utils.TextEncodingTool;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class CsvFileReader {

    private BufferedReader reader;
    private String charsetName;
    private long fileSizeInBytes = 0;

    public CsvFileReader( String csvFileName, String withCharsetName )
            throws FileNotFoundException, UnsupportedEncodingException{
        openReaderForFile( csvFileName, withCharsetName);
    }

    private void openReaderForFile (String csvFileName, String withCharsetName )
            throws FileNotFoundException, UnsupportedEncodingException {
        fileSizeInBytes = new File( csvFileName ).length();
        FileInputStream fileInputStream = new FileInputStream( csvFileName );
        InputStreamReader inputStreamReader;
        if ( ( withCharsetName==null ) || ( withCharsetName.isEmpty() ) ) {
            charsetName = "";
        } else {
            charsetName = withCharsetName;
        }
        if (withCharsetName.isEmpty()) {
            inputStreamReader = new InputStreamReader( fileInputStream );
        } else {
            inputStreamReader = new InputStreamReader( fileInputStream, withCharsetName );
        }
        reader = new BufferedReader( inputStreamReader );
    }

    public ArrayList<String> readAllCsvLines() throws IOException {
        ArrayList<String> allCsvLines = new ArrayList<>();
        String csvLine;
        while ((csvLine = readCsvLine()) != null) {
            allCsvLines.add(csvLine);
        }
        return allCsvLines;
    }

    public String readCsvLine() throws IOException {
        String csvLine;
        boolean needToCloseReader = false;
        try {
            csvLine = reader.readLine();
            if (charsetName.isEmpty()) {
                charsetName = TextEncodingTool.getCharsetFromText(csvLine);
            }
        } catch(IOException ex1) {
            needToCloseReader = true;
            throw ex1;
        } finally {
            if (needToCloseReader && (reader!= null)) {
                try {
                    reader.close();
                } catch (IOException ex2) {
                    throw ex2;
                }
            }
        }
        return cleanZWNBSP(csvLine);
    }

    /**
     * ZWNBSP" jest znakiem o zerowej szerokości, więc nie jest widoczny, ale ma wpływ na sposób,
     * w jaki parser CSV traktuje ten wiersz - nie pozwala łamac wiersz.
     * A przede wszystkim, przy porównaniach napisów pomimo, że nie jest widoczny, to jest uwzględniany,
     * co powoduje różność napisów, które wyglądają tak samo...
     */
    private String cleanZWNBSP( String textToCleanZWNBSP ) {
        if (textToCleanZWNBSP!=null) {
            return textToCleanZWNBSP.replace("\uFEFF", "");
        } else {
            return null;
        }
    }

    public void close() throws IOException{
        if (reader != null) {
            reader.close();
        }
    }

    public String getCharsetName() {
        return charsetName;
    }

    public long getEstimateCsvFileLinesQuantity( ) throws IOException {
        long estimateCsvFileLinesQuantity = 0;
        // odczyt nagłówka
        String csvLine = readCsvLine();
        // odczyt pierwszego wiersza po nagłówku
        csvLine = readCsvLine();
        Charset charset = Charset.forName( charsetName );
        // powtórzenie 10x i wyliczenie średniej
        int lineSizesSum = 0;
        for (int index=0; index<10; index++) {
            byte[] csvLineBytes = csvLine.getBytes( charset );
            int csvLineSize = 0;
            if (csvLineBytes.length>0) {
                csvLineSize = csvLineBytes.length + 2; // Dodatkowe 2 bajty na CRLF
            }
            lineSizesSum = lineSizesSum + csvLineSize;
        }

        estimateCsvFileLinesQuantity = fileSizeInBytes / (lineSizesSum / 10);
        return estimateCsvFileLinesQuantity;
    }
}