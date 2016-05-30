package com.example.restaurantsearch;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.github.kevinsawicki.http.HttpRequest;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SearchView searchView;
    private ArrayList<Restaurant> restaurantArrayList;
    private RestaurantArrayAdapter adapter;
    private TextView emptyView;
    private TextView welcomeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        searchView = (SearchView) findViewById(R.id.mySearchView);
        searchView.setQueryHint("SearchView");

        //*** setOnQueryTextListener ***
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub
                String httpQuery = new QueryBuilder().setLocalityFilter(query).build();
                new RetrieveRestaurantsTask().execute(httpQuery);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO Auto-generated method stub
                return false;
            }
        });

        final ListView listview = (ListView) findViewById(R.id.listview);
        emptyView = (TextView) findViewById(R.id.emptyView);
        welcomeView = (TextView) findViewById(R.id.welcome);

        restaurantArrayList = new ArrayList<>();
        adapter = new RestaurantArrayAdapter(this,
                R.layout.row_layout, restaurantArrayList);
        listview.setAdapter(adapter);
    }

    private class RestaurantArrayAdapter extends ArrayAdapter<Restaurant> {

        private Context context;
        private List<Restaurant> restaurantList;

        public RestaurantArrayAdapter(Context context, int textViewResourceId,
                                      List<Restaurant> restaurantList) {
            super(context, textViewResourceId, restaurantList);
            this.context = context;
            this.restaurantList = restaurantList;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.row_layout, parent, false);
            TextView restaurantNameTextView = (TextView) rowView.findViewById(R.id.restaurantNameTextView);
            TextView addressTextView = (TextView) rowView.findViewById(R.id.addressTextView);
            TextView typeTextView = (TextView) rowView.findViewById(R.id.typeTextView);
            RatingBar ratingBar = (RatingBar) rowView.findViewById(R.id.ratingBar);
            restaurantNameTextView.setText(restaurantList.get(position).getName());
            addressTextView.setText(restaurantList.get(position).getAddress());
            typeTextView.setText(restaurantList.get(position).getType());
            float rating = (float) restaurantList.get(position).getRating();
            ratingBar.setRating(rating);

            return rowView;
        }

    }

    private class RetrieveRestaurantsTask extends AsyncTask<String, Long, ArrayList<Restaurant>> {
        protected ArrayList<Restaurant> doInBackground(String... urls) {
            ArrayList<Restaurant> restaurantArrayList = null;
            try {
                HttpRequest request =  HttpRequest.get(urls[0]);
                if (request.ok()) {
                    String response = request.body();
                    restaurantArrayList = JsonParser.parseHTTPResponse(response);
                }
                return restaurantArrayList;
            } catch (HttpRequest.HttpRequestException exception) {
                return null;
            }
        }

        protected void onPostExecute(ArrayList<Restaurant> restaurantArrayList) {
            if (restaurantArrayList != null) {
                welcomeView.setVisibility(View.GONE);
                Log.d("RestaurantSearch", "Restaurant ArrayList returned successfully");
                MainActivity.this.restaurantArrayList.clear();
                MainActivity.this.restaurantArrayList.addAll(restaurantArrayList);
                adapter.notifyDataSetChanged();

                if(restaurantArrayList.isEmpty()) {
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    emptyView.setVisibility(View.GONE);
                }

            } else
                Log.d("RestaurantSearch", "Failed to retrieve restaurants");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
