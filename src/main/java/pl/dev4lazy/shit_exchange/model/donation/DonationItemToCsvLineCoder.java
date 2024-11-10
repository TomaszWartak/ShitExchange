package pl.dev4lazy.shit_exchange.model.donation;

import pl.dev4lazy.shit_exchange.utils.csv_service.Coder;

import java.util.ArrayList;
import java.util.List;

public class DonationItemToCsvLineCoder implements Coder<DonationItem, ArrayList<String> > {

    @Override
    public ArrayList<String> code(DonationItem donationItem) {
        return new ArrayList<>(
                List.of(
                        // todo? donationItem.getDonorStoreId().toString(),
                        donationItem.getDonorStoreName(),
                        donationItem.getSapId().toString(),
                        donationItem.getEanCode().toString(),
                        donationItem.getArticleName(),
                        String.format("%.0f", donationItem.getDonorReserveValue() ),
                        String.format("%.2f", donationItem.getDonorQuantityToZeroReserve() ),
                        String.format("%.2f", donationItem.getDonorStockQuantity() ),
                        String.format("%.2f", donationItem.getDonorStockValue() ),
                        donationItem.getSector(),
                        String.format("%.2f", donationItem.getDonorCogs12M() ),
                        // todo? donationItem.getTakerStoreId().toString(),
                        donationItem.getTakerStoreName(),
                        donationItem.getTakerPlanoKind(),
                        donationItem.getTakerTypeOfStokingUp(),
                        donationItem.getTakerChoiceOfStokingUp(),
                        String.format("%.2f", donationItem.getTakerStockQuantity() ),
                        String.format("%.2f", donationItem.getTakerStockValue() ),
                        String.format("%.2f", donationItem.getTakerSales12MValue() ),
                        String.format("%.2f", donationItem.getTakerSales12MQuantity() ),
                        String.format("%.0f", donationItem.getTakerRotation() )
                )
        );
    }
}
