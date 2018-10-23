package io.github.sudhansubarik.moviescentral.adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
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

    static List<MoviesReviews> reviews;
    private int rowLayout;
    private Context context;

    public ReviewsAdapter(List<MoviesReviews> reviews, int rowLayout, Context context) {
        ReviewsAdapter.reviews = reviews;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReviewViewHolder holder, final int position) {
        holder.movieTitle.setText(reviews.get(position).getAuthor());
        holder.movieSub.setText(reviews.get(position).getContent());
        holder.progressBar.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout moviesLayout;
        TextView movieTitle;
        TextView movieSub;
        ProgressBar progressBar;

        public ReviewViewHolder(View view) {
            super(view);
            moviesLayout = view.findViewById(R.id.reviews_layout);
            movieTitle = view.findViewById(R.id.reviews_title);
            movieSub = view.findViewById(R.id.reviews_comments_textView);
            progressBar = view.findViewById(R.id.reviews_item_loading_pb);
        }
    }
}
