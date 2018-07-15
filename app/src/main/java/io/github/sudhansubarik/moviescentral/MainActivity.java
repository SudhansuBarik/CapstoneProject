package io.github.sudhansubarik.moviescentral;

/*
 * References
 *
 * https://www.androidhive.info/2016/05/android-working-with-card-view-and-recycler-view/
 * https://abhiandroid.com/materialdesign/recyclerview-gridview.html
 * https://stackoverflow.com/a/40587169
 * https://stackoverflow.com/questions/40118264/retrofit-library-with-api-the-movie-db
 * https://stackoverflow.com/questions/41632590/clicking-cardview-instead-of-clicking-items-inside
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import io.github.sudhansubarik.moviescentral.adapters.MoviesAdapter;
import io.github.sudhansubarik.moviescentral.models.Movie;
import io.github.sudhansubarik.moviescentral.models.MoviesList;
import io.github.sudhansubarik.moviescentral.utils.Api;
import io.github.sudhansubarik.moviescentral.utils.MoviesApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static String API_KEY;

    Spinner filterSpinner;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        API_KEY = getResources().getString(R.string.tmdb_api_key);
        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "API Key Not Found", Toast.LENGTH_LONG).show();
            return;
        }

        filterSpinner = findViewById(R.id.filter_spinner);
        recyclerView = findViewById(R.id.home_recyclerView);
        progressBar = findViewById(R.id.activity_main_progressBar);

        // Spinner dropdown elements
        String[] filter = {"Popular", "Top Rated"};

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filter);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        filterSpinner.setAdapter(dataAdapter);

        // Set what would happen when an option is selected in the spinner
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                switch (position) {
                    case 0:
                        loadMovies(1);
                        break;
                    case 1:
                        loadMovies(2);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void loadMovies(int response) {

        if (isOnline()) {
            // GridLayoutManager with default vertical orientation and 3 columns
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
            // Designate all items in the grid to have the same size
            recyclerView.setHasFixedSize(true);

            MoviesApiService moviesApiService = Api.getClient().create(MoviesApiService.class);
            Call<MoviesList> call = moviesApiService.getPopularMovies(API_KEY);
            switch (response) {
                case 1:
                    call = moviesApiService.getPopularMovies(API_KEY);
                    break;
                case 2:
                    call = moviesApiService.getTopRatedMovies(API_KEY);
                    break;
            }
            call.enqueue(new Callback<MoviesList>() {
                @Override
                public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                    List<Movie> movies = response.body().getResults();
                    recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(), movies));
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<MoviesList> call, Throwable t) {
                    // Log error here since request failed
                    Log.e(TAG, t.toString());
                }
            });
        } else {
            Toast.makeText(this, "Please check your Internet Connection and try again!", Toast.LENGTH_LONG).show();
        }
    }

    /*
        Network changing:
            https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
     */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
