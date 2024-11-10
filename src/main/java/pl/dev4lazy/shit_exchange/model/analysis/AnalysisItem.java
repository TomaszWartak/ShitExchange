package pl.dev4lazy.shit_exchange.model.analysis;

import java.util.Objects;

public class AnalysisItem {
    private AnalysisItemCompositeKey donorIdTakerIdKey;
    private double donorReserveValueSum;
    private double takerReserveValueSum;
    private double diff;

    public AnalysisItem(AnalysisItemCompositeKey donorIdTakerIdKey, double donorReserveValueSum ) {
        this.donorIdTakerIdKey = donorIdTakerIdKey;
        this.donorReserveValueSum = donorReserveValueSum;
    }

    public AnalysisItemCompositeKey getDonorIdTakerIdKey() {
        return donorIdTakerIdKey;
    }

    public Integer getDonorStoreId() {
        return donorIdTakerIdKey.donorStoreId();
    }

    public Integer getTakerStoreId() {
        return donorIdTakerIdKey.takerStoreId();
    }

    public double getDonorReserveValueSum() {
        return donorReserveValueSum;
    }

    public double getTakerReserveValueSum() {
        return takerReserveValueSum;
    }

    public void setTakerReserveValueSum(double takerReserveValueSum) {
        this.takerReserveValueSum = takerReserveValueSum;
    }

    public double getDiff() {
        return diff;
    }

    public void setDiff(double diff) {
        this.diff = diff;
    }

    public void countDiff() {
        diff = Math.abs( donorReserveValueSum-takerReserveValueSum);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnalysisItem that = (AnalysisItem) o;
        return Objects.equals( donorIdTakerIdKey.donorStoreId(), that.donorIdTakerIdKey.donorStoreId() ) &&
                Objects.equals( donorIdTakerIdKey.takerStoreId(), that.donorIdTakerIdKey.takerStoreId() );
    }

    @Override
    public int hashCode() {
        return Objects.hashCode( donorIdTakerIdKey );
    }

}
