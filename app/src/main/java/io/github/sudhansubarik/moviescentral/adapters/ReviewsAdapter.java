package io.github.sudhansubarik.moviescentral.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import io.github.sudhansubarik.moviescentral.R;
import io.github.sudhansubarik.moviescentral.models.MoviesReviews;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    private static List<MoviesReviews> reviews;
    private int rowLayout;

    public ReviewsAdapter(List<MoviesReviews> reviews, int rowLayout, Context context) {
        ReviewsAdapter.reviews = reviews;
        this.rowLayout = rowLayout;
        Context context1 = context;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReviewViewHolder holder, final int position) {
        holder.movieTitle.setText(reviews.get(position).getAuthor());
        holder.movieSub.setText(reviews.get(position).getContent());
        holder.progressBar.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout moviesLayout;
        TextView movieTitle;
        TextView movieSub;
        ProgressBar progressBar;

        ReviewViewHolder(View view) {
            super(view);
            moviesLayout = view.findViewById(R.id.reviews_layout);
            movieTitle = view.findViewById(R.id.reviews_title);
            movieSub = view.findViewById(R.id.reviews_comments_textView);
            progressBar = view.findViewById(R.id.reviews_item_loading_pb);
        }
    }
}
