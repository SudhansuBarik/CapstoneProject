package io.github.sudhansubarik.moviescentral.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import io.github.sudhansubarik.moviescentral.R;
import io.github.sudhansubarik.moviescentral.activities.MovieDetailsActivity;
import io.github.sudhansubarik.moviescentral.models.Movie;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private List<Movie> moviesList;
    private Context context;
    private Activity activity;

    @NonNull
    @Override
    public MoviesAdapter.MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_grid_item, parent, false);
        return new MoviesViewHolder(view);
    }

    public MoviesAdapter(Context context, List<Movie> moviesList, Activity activity) {
        this.context = context;
        this.moviesList = moviesList;
        this.activity = activity;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MoviesViewHolder holder, final int position) {
        String url = context.getResources().getString(R.string.base_tmdb_img_url) + "w185/" + moviesList.get(position).getPosterPath();

        Movie movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());
        holder.year.setText(movie.getReleaseDate());
        holder.rating.setText(movie.getVoteAverage() + "");

        // In picasso:2.71828 :::: Picasso.get().load(url).into(holder.thumbnail, new com.squareup.picasso.Callback()
        Picasso.get().load(url).into(holder.thumbnail, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                if (holder.progressBar != null) {
                    holder.progressBar.setVisibility(View.GONE);
                }
            }

            // picasso:2.71828 :: public void onError(Exception e)
            @Override
            public void onError(Exception e) {
            }
        });
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        TextView title;
        TextView rating;
        TextView year;
        ProgressBar progressBar;

        MoviesViewHolder(View itemView) {
            super(itemView);

            thumbnail = itemView.findViewById(R.id.thumbnail_imageView);
            title = itemView.findViewById(R.id.title_textView);
            rating = itemView.findViewById(R.id.rating_textView);
            year = itemView.findViewById(R.id.year_textView);
            progressBar = itemView.findViewById(R.id.progressBar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), MovieDetailsActivity.class);
                    intent.putExtra("id", moviesList.get(getAdapterPosition()).getId());
                    intent.putExtra("movie", moviesList.get(getAdapterPosition()));
                    intent.putExtra("position", getAdapterPosition());
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                            thumbnail, ViewCompat.getTransitionName(thumbnail));
                    v.getContext().startActivity(intent, options.toBundle());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
