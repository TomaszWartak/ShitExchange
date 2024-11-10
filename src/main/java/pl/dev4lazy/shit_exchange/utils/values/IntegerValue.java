package pl.dev4lazy.shit_exchange.utils.values;

public class IntegerValue implements Value<Integer> {

    private Integer value;
    private boolean nullAllowed;

    public IntegerValue() {
        this.nullAllowed = true;
    }

    public IntegerValue( boolean nullAllowed ) {
        this.nullAllowed = nullAllowed;
    }

    public IntegerValue( IntegerValue integerValue ) {
        this.value = integerValue.value;
        this.nullAllowed = integerValue.nullAllowed;
    }

    @Override
    public void setValue( Integer value ) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
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
        return "IntegerValue{" +
                "value=" + value +
                '}';
    }
}
