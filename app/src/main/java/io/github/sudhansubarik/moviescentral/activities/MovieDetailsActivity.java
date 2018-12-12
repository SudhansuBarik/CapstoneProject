package io.github.sudhansubarik.moviescentral.activities;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.github.sudhansubarik.moviescentral.BuildConfig;
import io.github.sudhansubarik.moviescentral.R;
import io.github.sudhansubarik.moviescentral.adapters.TrailersAdapter;
import io.github.sudhansubarik.moviescentral.models.Movie;
import io.github.sudhansubarik.moviescentral.models.MoviesReviews;
import io.github.sudhansubarik.moviescentral.models.MoviesReviewsList;
import io.github.sudhansubarik.moviescentral.models.MoviesTrailers;
import io.github.sudhansubarik.moviescentral.models.MoviesTrailersList;
import io.github.sudhansubarik.moviescentral.models.MoviesViewModel;
import io.github.sudhansubarik.moviescentral.room.DbMovies;
import io.github.sudhansubarik.moviescentral.utils.Api;
import io.github.sudhansubarik.moviescentral.utils.MoviesApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    DbMovies dbMovies;
    MoviesViewModel moviesViewModel;

    private ImageView thumbnailImageView;
    List<MoviesReviews> reviews;
    private Movie movie1, movie;
    private Boolean isFavorite = false;
    private TextView titleTextView, ratingTextView, releaseDateTextView, overviewTextView, reviewsCommentsTextView, moreTextView;
    private CheckBox checkBox;
    private RecyclerView recyclerViewTrailers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_details);

        reviews = new ArrayList<>();
        movie1 = new Movie();

        thumbnailImageView = findViewById(R.id.thumbnail_imageView);
        titleTextView = findViewById(R.id.title_textView);
        ratingTextView = findViewById(R.id.rating_textView);
        releaseDateTextView = findViewById(R.id.release_date_textView);
        overviewTextView = findViewById(R.id.overview_textView);
        reviewsCommentsTextView = findViewById(R.id.reviews_comments_textView);
        moreTextView = findViewById(R.id.more_textView);
        checkBox = findViewById(R.id.details_fav_checkbox);
        recyclerViewTrailers = findViewById(R.id.trailers_recyclerView);

        Intent intent = getIntent();
        movie1 = intent.getParcelableExtra("movie");
        Log.d(TAG, "Parcel Received: \n" + "getId() > " + movie1.getId() + " \ngetTitle() > " + movie1.getTitle());
        moviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        dbMovies = moviesViewModel.getMovie(movie1.getId());

        reviewsCommentsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reviews.size() != 0) {
                    Intent intent = new Intent(v.getContext(), MovieReviewsActivity.class);
                    intent.putParcelableArrayListExtra("reviews", (ArrayList<MoviesReviews>) reviews);
                    startActivity(intent);
                } else {
                    Toast.makeText(MovieDetailsActivity.this, "No Reviews", Toast.LENGTH_LONG).show();
                }
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Check Movie added in favourite
                if (dbMovies != null) {
                    isFavorite = true; // set Favourite true
                }
            }
        }).start();

        setMovieDetails();

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                favorite(isFavorite);
            }
        });

        // Ads
        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);
    }

    private void setMovieDetails() {
        String API_KEY = BuildConfig.ApiKey;
        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Invalid API Key", Toast.LENGTH_LONG).show();
            return;
        }

        int id = getIntent().getIntExtra("id", 0);
        final MoviesApiService moviesApiService = Api.getClient().create(MoviesApiService.class);
        Call<Movie> call = moviesApiService.getMovieDetails(id, API_KEY);
        call.enqueue(new Callback<Movie>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
//                movie = response.body();

                movie = getIntent().getParcelableExtra("movie");
                // Set thumbnail
                Picasso.get().load(getResources()
                        .getString(R.string.base_tmdb_img_url) + "w342/" + movie.getPosterPath())
                        .into(thumbnailImageView);
                // Set title
                titleTextView.setText(movie.getTitle());
                // Set Release Date
                releaseDateTextView.setText(movie.getReleaseDate());
                // Set Rating
                ratingTextView.setText(movie.getVoteAverage() + "");
                // Set Overview
                overviewTextView.setText(movie.getOverview());
            }

            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable throwable) {
                // Log error here since request failed
                Log.e(TAG, throwable.toString());
            }
        });

        // Reviews
        MoviesApiService moviesApiService1 = Api.getClient().create(MoviesApiService.class);
        Call<MoviesReviewsList> call1 = moviesApiService1.getMovieReviews(id, API_KEY);
        call1.enqueue(new Callback<MoviesReviewsList>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<MoviesReviewsList> call, @NonNull Response<MoviesReviewsList> response) {
                reviews = response.body().getResults();

                final int min = 0;
                final int max = reviews.size();
                if (max > 0) {
                    if (max == 1) {
                        reviewsCommentsTextView.setText(reviews.get(0).getAuthor() + ": " + reviews.get(0).getContent());
                        moreTextView.setText("");
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Random random = new Random();
                                int r = random.nextInt((max - min) + 1) + min;
                                if (r == max)
                                    r = r - 1;
                                String rev = reviews.get(r).getContent();
                                Log.v(TAG, r + " >> " + rev);
                                final int finalR = r;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        moreTextView.setText("More " + (max - 1));
                                        reviewsCommentsTextView.setText(reviews.get(finalR).getAuthor() + ": " + reviews.get(finalR).getContent());
                                    }
                                });
                            }
                        }).start();
                    }
                } else {
                    reviewsCommentsTextView.setText(R.string.no_reviews);
                    moreTextView.setText("");
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesReviewsList> call, @NonNull Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });

        // Trailers
        recyclerViewTrailers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        MoviesApiService moviesApiService2 = Api.getClient().create(MoviesApiService.class);
        Call<MoviesTrailersList> call2 = moviesApiService2.getMovieVideos(id, API_KEY);
        call2.enqueue(new Callback<MoviesTrailersList>() {
            @Override
            public void onResponse(@NonNull Call<MoviesTrailersList> call, @NonNull Response<MoviesTrailersList> response) {
                List<MoviesTrailers> list = response.body().getResults();
                recyclerViewTrailers.setAdapter(new TrailersAdapter(list, R.layout.item_trailers, getApplicationContext()));
            }

            @Override
            public void onFailure(@NonNull Call<MoviesTrailersList> call, @NonNull Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    public void favorite(final boolean isFavorite1) {
        if (isFavorite1) {
            // Remove Movie from DB
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DbMovies movie = new DbMovies(movie1.getId(), movie1.getTitle(), movie1.getOriginalTitle(),
                            movie1.getTagline(), movie1.getOverview(), movie1.isAdult(), movie1.getPosterPath(),
                            movie1.getBackdropPath(), movie1.getReleaseDate(), movie1.isVideo(),
                            movie1.getVoteAverage(), movie1.getVoteCount());
                    moviesViewModel.deleteMovie(movie);
                    isFavorite = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            checkBox.setText(R.string.add_to_favorites);
                            Toast.makeText(MovieDetailsActivity.this, "Removed from Favourites", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).start();
        } else {
            // Add Movie to DB
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DbMovies movie = new DbMovies(movie1.getId(), movie1.getTitle(), movie1.getOriginalTitle(),
                            movie1.getTagline(), movie1.getOverview(), movie1.isAdult(), movie1.getPosterPath(),
                            movie1.getBackdropPath(), movie1.getReleaseDate(), movie1.isVideo(),
                            movie1.getVoteAverage(), movie1.getVoteCount());
                    moviesViewModel.addMovie(movie);
                    isFavorite = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            checkBox.setText(R.string.remove_from_favorites);
                            Toast.makeText(MovieDetailsActivity.this, "Added to Favourites", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).start();
        }
    }
}
