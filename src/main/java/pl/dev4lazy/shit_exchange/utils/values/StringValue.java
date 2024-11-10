package pl.dev4lazy.shit_exchange.utils.values;

public class StringValue implements Value<String> {

    private String value;
    private boolean nullAllowed;

    public StringValue() {
        this.nullAllowed = true;
    }

    public StringValue( boolean nullAllowed ) {
        this.nullAllowed = nullAllowed;
    }

    public StringValue( StringValue stringValue ) {
        this.value = stringValue.value;
        this.nullAllowed = stringValue.nullAllowed;
    }

    @Override
    public void setValue( String value ) {
        this.value = value;
    }

    @Override
    public String getValue() {
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
        return "StringValue{" +
                "value='" + value + '\'' +
                '}';
    }
}
