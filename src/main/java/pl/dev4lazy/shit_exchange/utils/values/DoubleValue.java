package pl.dev4lazy.shit_exchange.utils.values;

public class DoubleValue implements Value<Double> {

    private Double value;
    private boolean nullAllowed;

    public DoubleValue() {
        this.nullAllowed = true;
    }

    public DoubleValue( boolean nullAllowed ) {
        this.nullAllowed = nullAllowed;
    }

    public DoubleValue( DoubleValue doubleValue ) {
        this.value = doubleValue.value;
        this.nullAllowed = doubleValue.nullAllowed;
    }

    @Override
    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public Value validationResult() {
        if (!nullAllowed && value==null) {
            return null;
        }
        return this;
    }

    @Override
    public String toString() {
        return "DoubleValue{" +
                "value=" + value +
                '}';
    }
}
