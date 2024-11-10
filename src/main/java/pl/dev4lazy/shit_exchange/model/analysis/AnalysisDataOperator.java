package pl.dev4lazy.shit_exchange.model.analysis;

import pl.dev4lazy.shit_exchange.Config;
import pl.dev4lazy.shit_exchange.model.DataGrip;
import pl.dev4lazy.shit_exchange.model.ToolsGrip;
import pl.dev4lazy.shit_exchange.model.donation.*;
import pl.dev4lazy.shit_exchange.model.reserve.ReserveData;
import pl.dev4lazy.shit_exchange.utils.problem_handling.AppError;
import pl.dev4lazy.shit_exchange.utils.csv_service.CsvUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class AnalysisDataOperator {

    private final String outputDonationsAnalysisFileName;
    private final DonationsData donations;
    private final ReserveData reserve;
    private HashMap<AnalysisItemCompositeKey, AnalysisItem> analysisData;

    public AnalysisDataOperator() {
        this.donations = DataGrip.getInstance().getDonationsData();
        this.reserve = DataGrip.getInstance().getReserveData();
        this.outputDonationsAnalysisFileName  = DataGrip.getOutputAnalysisFileNameFullPath();
    }

    public void analyzeData() {
        // TODO po wybraniu 3  biorców i usnięciu pozostałych biorców analiza pokazuje głupoty
        if ( donations.haveNoData() ) {
            ToolsGrip.handleNotification( "BRAK DANYCH ..." );
            return;
        } else {
            if ( (analysisData!=null) && (!analysisData.isEmpty()) ) {
                analysisData.clear();
            }
        }
        analysisData = new HashMap<>( 100 );
        for ( Integer donorStoreId : donations.getAllDonorStoresIds() ) {
            AllDonationsDataForDonor allDonationsDataForDonor = donations.getAllDonationsDataOfDonor( donorStoreId );
            if (allDonationsDataForDonor.getTakersNodes().size()>0) {
                for ( Integer takerStoreId: allDonationsDataForDonor.getAllTakerStoresIds() ) {
                    DonationsDataForTakerStore donationsDataForTakerStore =
                            allDonationsDataForDonor.getDonationsDataForTakerStore(takerStoreId);
                    double donorReserveValueSum = 0.0;
                    // todo zapas nie potrzebny... double donorStockValueSum = 0.0;
                    for (DonationItem donationItem : donationsDataForTakerStore.getAllDonationItems()) {
                        donorReserveValueSum += donationItem.getDonorReserveValue();
                        // todo zapas nie potrzebny... donorStockValueSum += donationItem.getDonorStockValue();
                    }
                    AnalysisItem donorAnalysisItem = new AnalysisItem(
                            new AnalysisItemCompositeKey(donorStoreId, takerStoreId),
                            donorReserveValueSum
                    );
                    AnalysisItemCompositeKey takerIdDonorIdKey = new AnalysisItemCompositeKey(takerStoreId, donorStoreId);
                    if (analysisData.containsKey(takerIdDonorIdKey)) {
                        AnalysisItem takerAnalysisItem = analysisData.get(takerIdDonorIdKey);
                        donorAnalysisItem.setTakerReserveValueSum(takerAnalysisItem.getDonorReserveValueSum());
                        donorAnalysisItem.countDiff();
                        takerAnalysisItem.setTakerReserveValueSum(donorAnalysisItem.getDonorReserveValueSum());
                        takerAnalysisItem.countDiff();
                        analysisData.put(takerAnalysisItem.getDonorIdTakerIdKey(), takerAnalysisItem);
                    }
                    analysisData.put(donorAnalysisItem.getDonorIdTakerIdKey(), donorAnalysisItem);
                    if (Config.IS_ONE_DONOR_ONLY &&
                            !donorStoreId.equals(Config.ONE_DONOR_ONLY_ID)) {
                        continue;
                    }
                    /* todo wyświetlenie zapasu - to nie potrzebne jest
                    ToolsGrip.handleNotification(
                            donorStoreId+" -> "+takerStoreId+" : "+
                            "Suma rezerwy = "+String.format("%.0f", donorReserveValueSum)+"; "+
                            "Suma zapasu = "+String.format("%.0f", donorStockValueSum)
                    );
                     */

                }
            }
        }
        ArrayList<AnalysisItem> sortedAnalysisItems = new ArrayList<>( analysisData.values() );
        sortedAnalysisItems.sort( Comparator
                .comparing( AnalysisItem::getDonorStoreId )
                .thenComparing( AnalysisItem::getDiff )
        );
        // Wyświetlenie wymian z wybranymi już biorcami
        // TODO zrób tu if() który zapewni że komunikat wyświetli się gdy w sortedAnalysisItems są juz wybrani biorcy
        ToolsGrip.handleNotification("");
        ToolsGrip.handleNotification("Wymiany z wybranymi sklepami:");
        for (AnalysisItem analysisItem : sortedAnalysisItems) {
            if ( Config.IS_ONE_DONOR_ONLY &&
                 !analysisItem.getDonorStoreId().equals( Config.ONE_DONOR_ONLY_ID )) {
                    continue;
            }
            if ( isTakerReserveExcluded( analysisItem.getTakerStoreId() ) ) {
                ToolsGrip.handleNotification(
                        analysisItem.getDonorStoreId() + " -> " + analysisItem.getTakerStoreId() + " : " +
                                "Rezerwa: = " + String.format("%.0f", analysisItem.getDonorReserveValueSum()) + " -> " +
                                String.format(" %.0f", analysisItem.getTakerReserveValueSum()) + " = " +
                                String.format(" %.0f", analysisItem.getDiff())
                );
            }
        }
        ToolsGrip.handleNotification( "" );
        // Wyświetlenie propozycji wymian z pozostałymi biorcami
        // TODO zrób tu if() który zapewni że komunikat wyświetli się gdy w sortedAnalysisItems są biorcy
        // nie brani jeszcze pod uwagę
        ToolsGrip.handleNotification("Propozycje wymiany:");
        for (AnalysisItem analysisItem : sortedAnalysisItems) {
            if ( Config.IS_ONE_DONOR_ONLY &&
                 !analysisItem.getDonorStoreId().equals( Config.ONE_DONOR_ONLY_ID )) {
                    continue;
            }
            if ( isTakerReserveNotExcluded( analysisItem.getTakerStoreId() ) ) {
                ToolsGrip.handleNotification(
                        analysisItem.getDonorStoreId() + " -> " + analysisItem.getTakerStoreId() + " : " +
                                "Rezerwa: = " + String.format("%.0f", analysisItem.getDonorReserveValueSum()) + " -> " +
                                String.format(" %.0f", analysisItem.getTakerReserveValueSum()) + " = " +
                                String.format(" %.0f", analysisItem.getDiff())
                );
            }
        }
        ToolsGrip.handleNotification("");

    }

    private boolean isTakerReserveExcluded( Integer takerStoreId ) {
        return reserve.getExcludedFromDonationsStoreIds().contains( takerStoreId );
    }

    private boolean isTakerReserveNotExcluded( Integer takerStoreId ) {
        return !isTakerReserveExcluded( takerStoreId );
    }

    public void generateAnalysisCsvFile() {
        if ((analysisData==null) || analysisData.isEmpty()) {
            ToolsGrip.handleNotification( "BRAK DANYCH... Wygeneruj dane analizy" );
            return;
        }

        AnalysisCsvFile analysisCsvFile = new AnalysisCsvFile(
                outputDonationsAnalysisFileName,
                Charset.forName("Windows-1250"),
                CsvUtils.CSV_SEPARATOR,
                CsvUtils.HAS_HEADER );
        try {
            analysisCsvFile.openForWriting();
        } catch (Exception e) {
            ToolsGrip.handleError( new AppError( e ));
        }

        ArrayList<String> outputCsvLineList = new ArrayList<>();
        outputCsvLineList.add( analysisCsvFile.getCsvHeaderRow() );

        ArrayList<String> parsedCsvLine;
        ArrayList<AnalysisItem> sortedAnalysisItems = getSortedDonationsAnalysisItems( );
        DonationAnalysisItemToCsvLineCoder csvLineCoder = new DonationAnalysisItemToCsvLineCoder();
        for (AnalysisItem analysisItem : sortedAnalysisItems) {
            parsedCsvLine = csvLineCoder.code( analysisItem );
            outputCsvLineList.add( CsvUtils.serializeToCsvLine( parsedCsvLine ) );
        }

        try {
            saveCsvLines( outputCsvLineList, analysisCsvFile );
            analysisCsvFile.close();
        } catch (Exception e) {
            ToolsGrip.handleError( new AppError( e ));
        }
    }

    private void saveCsvLines( ArrayList<String> outputCsvLineList, AnalysisCsvFile analysisCsvFile ) {
        try {
            analysisCsvFile.writeCsvLines( outputCsvLineList );
        } catch (IOException ioException ) {
            throw new IllegalStateException();
        }
    }

    private ArrayList<AnalysisItem> getSortedDonationsAnalysisItems( ) {
        ArrayList<AnalysisItem> sortedAnalysisItems = new ArrayList<>( analysisData.values() );
        sortedAnalysisItems.sort( Comparator
                .comparing( AnalysisItem::getDonorStoreId )
                .thenComparing( AnalysisItem::getDiff )
        );
        return sortedAnalysisItems;
    }

}
