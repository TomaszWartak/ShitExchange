package pl.dev4lazy.shit_exchange.utils.values;

public class LongValue implements Value<Long> {

    private Long value;
    private boolean nullAllowed;

    public LongValue() {
        this.nullAllowed = true;
    }

    public LongValue( boolean nullAllowed ) {
        this.nullAllowed = nullAllowed;
    }

    public LongValue( LongValue longValue ) {
        this.value = longValue.value;
        this.nullAllowed = longValue.nullAllowed;
    }

    @Override
    public void setValue(Long value) {
        this.value = value;
    }

    @Override
    public Long getValue() {
        return value;
    }

    /**
     * Jeśli value spełnia reguły walidacji, to zwraca this.
     * Jeśli value nie spełnia reguł walidacji (np. "Nie może być null"), to zwraca null,
     * co oznacza niepowodzenie walidacji.
     * @return
     */
    @Override
    public Value validationResult() {
        if (!nullAllowed && value==null) {
            return null;
        }
        return this;
    }

    @Override
    public String toString() {
        return "LongValue{" +
                "value=" + value +
                '}';
    }
}
