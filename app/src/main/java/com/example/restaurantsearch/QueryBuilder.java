package com.example.restaurantsearch;

/**
 * Created by ChitnisAnuragAshok on 3/20/2016.
 */
public class QueryBuilder {

    private static final String KEY = "<api-key>";

    private static final String BASE_URL = "http://api.v3.factual.com/t/restaurants-us?";

    private String query;

    public QueryBuilder() {

        //build the base query with key
        this.query = BASE_URL + "KEY=" + KEY;
    }
    public QueryBuilder setLocalityFilter(String locality) {
        this.query = this.query + "&filters={\"locality\":{\"$eq\":\"" + locality + "\"}}";
        return this;
    }

    public String build() {
        return this.query;
    }

}
