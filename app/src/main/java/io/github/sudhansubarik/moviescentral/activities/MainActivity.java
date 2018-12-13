package io.github.sudhansubarik.moviescentral.activities;

/*
 * References
 *
 * https://www.androidhive.info/2016/05/android-working-with-card-view-and-recycler-view/
 * https://abhiandroid.com/materialdesign/recyclerview-gridview.html
 * https://stackoverflow.com/a/40587169
 * https://stackoverflow.com/questions/40118264/retrofit-library-with-api-the-movie-db
 * https://stackoverflow.com/questions/41632590/clicking-cardview-instead-of-clicking-items-inside
 */

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import io.github.sudhansubarik.moviescentral.BuildConfig;
import io.github.sudhansubarik.moviescentral.R;
import io.github.sudhansubarik.moviescentral.adapters.MoviesAdapter;
import io.github.sudhansubarik.moviescentral.firebase.LoginActivity;
import io.github.sudhansubarik.moviescentral.firebase.ProfileActivity;
import io.github.sudhansubarik.moviescentral.models.Movie;
import io.github.sudhansubarik.moviescentral.models.MoviesList;
import io.github.sudhansubarik.moviescentral.models.MoviesViewModel;
import io.github.sudhansubarik.moviescentral.room.DbMovies;
import io.github.sudhansubarik.moviescentral.utils.Api;
import io.github.sudhansubarik.moviescentral.utils.Constants;
import io.github.sudhansubarik.moviescentral.utils.MoviesApiService;
import io.github.sudhansubarik.moviescentral.widget.MoviesAppWidget;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static String API_KEY;

    Spinner filterSpinner;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    Boolean doubleBackToExitPressedOnce = false;
    GridLayoutManager manager;
    List<Movie> movieList = new ArrayList<>();
    List<DbMovies> dbMoviesList = new ArrayList<>();
    MoviesAdapter moviesAdapter;
    MoviesViewModel moviesViewModel;
    private Parcelable recyclerViewState;
    ArrayAdapter<String> dataAdapter;

    private FirebaseAuth auth;
    private FirebaseUser user;
    int movieRequestType = 1;

    StringBuilder builder;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get current user
        user = FirebaseAuth.getInstance().getCurrentUser();
        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        API_KEY = BuildConfig.ApiKey;
        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.invalid_api_key), Toast.LENGTH_LONG).show();
            return;
        }

        moviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);

        filterSpinner = findViewById(R.id.filter_spinner);
        recyclerView = findViewById(R.id.home_recyclerView);
        progressBar = findViewById(R.id.activity_main_progressBar);

        manager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(manager);
        // Designate all items in the grid to have the same size
        recyclerView.setHasFixedSize(true);

        // Spinner dropdown elements
        String[] filter = {getString(R.string.spinner_popular), getString(R.string.spinner_top_rated), getString(R.string.spinner__favorites)};
        // Creating adapter for spinner
        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filter);
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
                        movieRequestType = 1;
                        loadMovies();
                        break;
                    case 1:
                        movieRequestType = 2;
                        loadMovies();
                        break;
                    case 2:
                        movieRequestType = 3;
                        loadMovies();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                // db favourite
                moviesViewModel.getAllMovies().observe(MainActivity.this, new Observer<List<DbMovies>>() {
                    @Override
                    public void onChanged(@Nullable List<DbMovies> movies) {
                        dbMoviesList = movies;
                        assert dbMoviesList != null;
                        if (dbMoviesList.size() != 0 && movieRequestType == 3) {
                            Log.d(TAG, ">>>>>>>>>>\n\n>>>" + dbMoviesList.size());
                            loadMovies();
                        } else if (dbMoviesList.size() == 0 && movieRequestType == 3) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            movies.clear();
                            movieRequestType = 1;
                            loadMovies();
                        }
                    }
                });
            }

        }).start();

        if (savedInstanceState != null) {
            movieList = savedInstanceState.getParcelableArrayList(Constants.DATA_KEY);
            recyclerViewState = savedInstanceState.getParcelable(Constants.STATE_KEY);
        }

        // Ads
        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);
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
        if (id == R.id.action_my_profile) {
            myProfile();
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadMovies() {
        final int response = movieRequestType;
        final MoviesApiService apiService = Api.getClient().create(MoviesApiService.class);

        if (isOnline()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.VISIBLE);
                    if (response == 3) {
                        if (dbMoviesList.size() > 0) {
                            movieList.clear();
                            for (int i = 0; i < dbMoviesList.size(); i++) {
                                Log.d(TAG, "For " + dbMoviesList.get(i).getMovieId());
                                Call<Movie> call = apiService.getMovieDetails(dbMoviesList.get(i).getMovieId(), API_KEY);
                                call.enqueue(new Callback<Movie>() {
                                    @Override
                                    public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                                        Movie a = response.body();
                                        assert a != null;
                                        Log.d(TAG, a.getTitle());
                                        movieList.add(a);
                                        Log.d(TAG, movieList.get(0).getTitle());
                                        if (dbMoviesList.size() == dbMoviesList.size()) {
                                            Log.e(TAG, movieList.size() + " size");
                                            moviesAdapter = new MoviesAdapter(getApplicationContext(), movieList, MainActivity.this);
                                            recyclerView.setAdapter(moviesAdapter);
                                        }
                                        if (progressBar != null) {
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                                        // Log error here since request failed
                                        Log.e(TAG, t.toString());
                                    }
                                });
                            }
                        } else {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            recyclerView.setAdapter(null);
                            Toast.makeText(MainActivity.this, getString(R.string.no_fav_movie), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Call<MoviesList> call = apiService.getPopularMovies(API_KEY);
                        switch (response) {
                            case 1:
                                call = apiService.getPopularMovies(API_KEY);
                                break;
                            case 2:
                                call = apiService.getTopRatedMovies(API_KEY);
                                break;
                        }
                        // MoviesList Callback
                        call.enqueue(new Callback<MoviesList>() {
                            @Override
                            public void onResponse(@NonNull Call<MoviesList> call, @NonNull Response<MoviesList> response) {
                                assert response.body() != null;
                                List<Movie> movieList2 = response.body().getResults();
                                moviesAdapter = new MoviesAdapter(getApplicationContext(), movieList2, MainActivity.this);
                                recyclerView.setAdapter(moviesAdapter);

                                String[] movieName = new String[5];
                                for (int i = 0; i < 5; i++)
                                    movieName[i] = movieList2.get(i).getTitle();
                                saveDataToSharedPrefs(movieName);

                                moviesAdapter.notifyDataSetChanged();
                                if (progressBar != null) progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailure(@NonNull Call<MoviesList> call, @NonNull Throwable t) {
                                // Log error here since request failed
                                Log.e(TAG, t.toString());
                            }
                        });
                    }
                }
            });
        } else {
            Toast.makeText(this, getString(R.string.check_internet_connection), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Constants.DATA_KEY, (ArrayList<Movie>) movieList);
        recyclerViewState = manager.onSaveInstanceState();
        outState.putParcelable(Constants.STATE_KEY, recyclerViewState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            movieList = savedInstanceState.getParcelableArrayList(Constants.DATA_KEY);
            recyclerViewState = savedInstanceState.getParcelable(Constants.STATE_KEY);
        }
    }

    private void myProfile() {
        if (user == null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.info);
            builder.setMessage(R.string.login_to_continue);
            builder.setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    auth.signOut();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
    }

    public void saveDataToSharedPrefs(String[] prefMovies) {

        Intent intent = new Intent(getApplicationContext(), MoviesAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        int[] ids = AppWidgetManager.getInstance(getApplicationContext())
                .getAppWidgetIds(new ComponentName(getApplication(), MoviesAppWidget.class));

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        getApplication().sendBroadcast(intent);

        new MoviesAsyncTask().execute(prefMovies);
    }

    private class MoviesAsyncTask extends AsyncTask<String[], Void, Void> {

        @Override
        protected Void doInBackground(String[]... asyncMovie) {
            builder = new StringBuilder();

            int temp = 0;

            for (int i = 0; i < asyncMovie.length; i++) {
                if (asyncMovie[i] != null) {
                    temp++;
                    builder.append(temp)
                            .append(". ")
                            .append(asyncMovie[i])
                            .append("\n");
                }
                if (temp == 3) {
                    break;
                }
            }

            sharedPref = getApplication().getSharedPreferences(Constants.SHARED_PREF_MOVIE, Context.MODE_PRIVATE);
            editor = sharedPref.edit();
            editor.putString(Constants.SHARED_PREF_MOVIE_LIST, builder.toString());
            editor.apply();

            return null;
        }
    }

    // Double Back Press to Exit
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.click_back_again), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
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
