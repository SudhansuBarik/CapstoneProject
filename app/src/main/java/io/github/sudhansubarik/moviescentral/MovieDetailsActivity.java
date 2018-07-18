package io.github.sudhansubarik.moviescentral;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import io.github.sudhansubarik.moviescentral.models.Movie;
import io.github.sudhansubarik.moviescentral.utils.Api;
import io.github.sudhansubarik.moviescentral.utils.MoviesApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailsActivity.class.getSimpleName();
    private static String API_KEY;

    private Movie movie;
    private int id;

    private ImageView thumbnailImageView;
    private TextView titleTextView;
    private TextView taglineTextView;
    private TextView ratingTextView;
    private TextView releaseDateTextView;
    private TextView overviewTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_details);

        thumbnailImageView = findViewById(R.id.thumbnail_imageView);
        titleTextView = findViewById(R.id.title_textView);
        taglineTextView = findViewById(R.id.tagline_textView);
        ratingTextView = findViewById(R.id.rating_textView);
        releaseDateTextView = findViewById(R.id.release_date_textView);
        overviewTextView = findViewById(R.id.overview_textView);

        setMovieDetails();
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
                // Set tag line
                taglineTextView.setText(movie.getTagline());
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
    }
}
