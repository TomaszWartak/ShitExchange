package pl.dev4lazy.shit_exchange.utils.values;

public interface Value<V> {

    boolean NULL_NOT_ALLOWED = false;
    void setValue( V value );
    V getValue();
    Value validationResult();

}
