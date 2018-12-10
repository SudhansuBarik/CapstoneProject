package io.github.sudhansubarik.moviescentral.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import io.github.sudhansubarik.moviescentral.R;
import io.github.sudhansubarik.moviescentral.adapters.ReviewsAdapter;
import io.github.sudhansubarik.moviescentral.models.MoviesReviews;

public class MovieReviewsActivity extends AppCompatActivity {

    private static final String TAG = MovieReviewsActivity.class.getSimpleName();
    List<MoviesReviews> reviews = new ArrayList<>();
    RecyclerView recyclerView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_reviews);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        recyclerView = findViewById(R.id.reviews_recyclerView);
        progressBar = findViewById(R.id.activity_movie_reviews_progressBar);
        Intent intent = getIntent();
        reviews = intent.getParcelableArrayListExtra("reviews");
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new ReviewsAdapter(reviews, R.layout.item_review));
        Log.d(TAG, "\nParcel Received: \n" + "getAuthor() > " + reviews.get(0).getAuthor() + " \nsize() > " + reviews.size());
        progressBar.setVisibility(View.GONE);
    }
}
