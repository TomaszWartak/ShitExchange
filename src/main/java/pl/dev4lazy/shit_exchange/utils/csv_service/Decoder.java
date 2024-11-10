package pl.dev4lazy.shit_exchange.utils.csv_service;

public interface Decoder<D,R> {

    R decode(D data );

}
