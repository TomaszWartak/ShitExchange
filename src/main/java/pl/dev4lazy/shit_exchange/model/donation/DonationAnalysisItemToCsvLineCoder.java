package pl.dev4lazy.shit_exchange.model.donation;

import pl.dev4lazy.shit_exchange.utils.csv_service.Coder;
import pl.dev4lazy.shit_exchange.model.analysis.AnalysisItem;

import java.util.ArrayList;
import java.util.List;

public class DonationAnalysisItemToCsvLineCoder implements Coder<AnalysisItem, ArrayList<String> > {

    @Override
    public ArrayList<String> code(AnalysisItem analysisItem) {
        return new ArrayList<>(
                List.of(
                        analysisItem.getDonorStoreId().toString(),
                        analysisItem.getTakerStoreId().toString(),
                        String.format("%.0f", analysisItem.getDonorReserveValueSum() ),
                        String.format("%.0f", analysisItem.getTakerReserveValueSum() ),
                        String.format("%.2f", analysisItem.getDiff() )
                )
        );
    }
}
