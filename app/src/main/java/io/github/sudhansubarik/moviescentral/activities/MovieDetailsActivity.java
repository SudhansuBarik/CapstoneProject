package io.github.sudhansubarik.moviescentral.activities;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private static String API_KEY;

    DbMovies dbMovies;
    private int id;
    MoviesViewModel moviesViewModel;

    private ImageView thumbnailImageView;
    List<MoviesReviews> reviews = new ArrayList<>();
    private Movie movie1 = new Movie(), movie;
    private Boolean isFavorite = false;
    private TextView titleTextView, ratingTextView, releaseDateTextView, overviewTextView, reviewsCommentsTextView, moreTextView;
    private FloatingActionButton favoriteFab;
    private RecyclerView recyclerViewTrailers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_details);

        thumbnailImageView = findViewById(R.id.thumbnail_imageView);
        titleTextView = findViewById(R.id.title_textView);
        ratingTextView = findViewById(R.id.rating_textView);
        releaseDateTextView = findViewById(R.id.release_date_textView);
        overviewTextView = findViewById(R.id.overview_textView);
        reviewsCommentsTextView = findViewById(R.id.reviews_comments_textView);
        moreTextView = findViewById(R.id.more_textView);
        favoriteFab = findViewById(R.id.details_favorite_fab);
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
                    Log.d(TAG, "Database: " + movie1.getPosterPath() + " > " + dbMovies.getPosterPath() + " " + isFavorite);
                    favouriteBtn(); // refresh fav button
                    Movie movie = new Movie(
                            dbMovies.getMovieId(),
                            dbMovies.getMovieName(),
                            dbMovies.getOriginalTitle(),
                            dbMovies.getTagline(),
                            dbMovies.getOverview(),
                            dbMovies.isAdult(),
                            dbMovies.getPosterPath(),
                            dbMovies.getBackdropPath(),
                            dbMovies.getReleaseDate(),
                            dbMovies.isVideo(),
                            dbMovies.getVoteAverage(),
                            dbMovies.getVoteCount());
                } else {
                    favouriteBtn();
                }
            }
        }).start();

        setMovieDetails();

        favoriteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favorite(isFavorite);
            }
        });

    }

    private void setMovieDetails() {
        API_KEY = BuildConfig.ApiKey;
        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Invalid API Key", Toast.LENGTH_LONG).show();
            return;
        }

        id = getIntent().getIntExtra("id", 0);
        final MoviesApiService moviesApiService = Api.getClient().create(MoviesApiService.class);
        Call<Movie> call = moviesApiService.getMovieDetails(id, API_KEY);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                movie = response.body();

                // Set thumbnail
                Picasso.with(MovieDetailsActivity.this).load(getResources().getString(R.string.base_tmdb_img_url) + "w342/"
                        + movie.getPosterPath()).into(thumbnailImageView);
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
            public void onFailure(Call<Movie> call, Throwable throwable) {
                // Log error here since request failed
                Log.e(TAG, throwable.toString());
            }
        });

        // Reviews
        MoviesApiService moviesApiService1 = Api.getClient().create(MoviesApiService.class);
        Call<MoviesReviewsList> call1 = moviesApiService1.getMovieReviews(id, API_KEY);
        call1.enqueue(new Callback<MoviesReviewsList>() {
            @Override
            public void onResponse(Call<MoviesReviewsList> call, Response<MoviesReviewsList> response) {
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
                                    @SuppressLint("SetTextI18n")
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
                    reviewsCommentsTextView.setText("No Reviews");
                    moreTextView.setText("");
                }
            }

            @Override
            public void onFailure(Call<MoviesReviewsList> call, Throwable t) {
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
            public void onResponse(Call<MoviesTrailersList> call, Response<MoviesTrailersList> response) {
                List<MoviesTrailers> list = response.body().getResults();
                recyclerViewTrailers.setAdapter(new TrailersAdapter(list, R.layout.item_trailers, getApplicationContext()));
            }

            @Override
            public void onFailure(Call<MoviesTrailersList> call, Throwable t) {
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
                    favouriteBtn();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MovieDetailsActivity.this, "Removed Favourite", Toast.LENGTH_LONG).show();
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
                    favouriteBtn();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MovieDetailsActivity.this, "Added to Favourite", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).start();
        }
    }

    public void favouriteBtn() {
        if (isFavorite) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    favoriteFab.setImageResource(R.drawable.if_favorite_delete_51907);
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    favoriteFab.setImageResource(R.drawable.if_favorite_add_51906);
                }
            });
        }
    }
}
