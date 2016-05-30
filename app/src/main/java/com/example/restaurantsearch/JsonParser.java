package com.example.restaurantsearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ChitnisAnuragAshok on 3/20/2016.
 */
public class JsonParser {

    public static ArrayList<Restaurant> parseHTTPResponse(String responseString) {
        ArrayList<Restaurant> restaurantArrayList= new ArrayList<>();
        try {
            JSONObject baseObject = new JSONObject(responseString);
            JSONObject responseObject = baseObject.getJSONObject("response");
            JSONArray restaurantArray = responseObject.getJSONArray("data");
            for (int i =0; i<restaurantArray.length();i++) {
                Restaurant restaurant = new Restaurant();
                JSONObject restaurantObject = restaurantArray.getJSONObject(i);
                restaurant.setName(restaurantObject.getString("name"));
                restaurant.setAddress(restaurantObject.getString("address"));
                restaurant.setRating(restaurantObject.getDouble("rating"));
                // Check if the restaurant has its cuisine listed, if yes add it to type
                if(restaurantObject.has("cuisine")) {
                    JSONArray cuisineArray = restaurantObject.getJSONArray("cuisine");
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int j = 0; j < cuisineArray.length(); j++) {
                        String cuisine = cuisineArray.getString(j);
                        stringBuilder.append(cuisine);
                        stringBuilder.append(" ,");
                    }
                    restaurant.setType(stringBuilder.toString());
                }
                restaurantArrayList.add(restaurant);
            }
        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }

        return restaurantArrayList;
    }
}
