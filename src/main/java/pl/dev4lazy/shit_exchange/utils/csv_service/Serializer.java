package pl.dev4lazy.shit_exchange.utils.csv_service;

import java.util.ArrayList;

public interface Serializer {

    String serialize(ArrayList<String> elements, String delimiter );

}



