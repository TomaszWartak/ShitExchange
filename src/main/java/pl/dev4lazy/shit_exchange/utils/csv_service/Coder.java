package pl.dev4lazy.shit_exchange.utils.csv_service;

public interface Coder<D,R> {

    R code(D data );

}