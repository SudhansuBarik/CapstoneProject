package io.github.sudhansubarik.moviescentral.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
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
import io.github.sudhansubarik.moviescentral.models.MoviesTrailers;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {

    private static List<MoviesTrailers> trailers;
    private int rowLayout;
    private Context context;

    public TrailersAdapter(List<MoviesTrailers> trailers, int rowLayout, Context context) {
        TrailersAdapter.trailers = trailers;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @NonNull
    @Override
    public TrailersAdapter.TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);

        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TrailerViewHolder holder, int position) {
        String url = context.getResources().getString(R.string.youtube_img_url) + trailers.get(holder.getAdapterPosition()).getKey() + "/hqdefault.jpg";

        holder.trailerTitle.setText(trailers.get(holder.getAdapterPosition()).getName());
        holder.trailerSub.setText(trailers.get(holder.getAdapterPosition()).getSite());
        Picasso.with(context).load(url).error(R.drawable.error).into(holder.trailerImageView, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                if (holder.progressBar != null) {
                    holder.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    static class TrailerViewHolder extends RecyclerView.ViewHolder {

        TextView trailerTitle, trailerSub;
        ImageView trailerImageView;
        ProgressBar progressBar;

        TrailerViewHolder(View itemView) {
            super(itemView);

            trailerTitle = itemView.findViewById(R.id.trailer_title_textView);
            trailerSub = itemView.findViewById(R.id.trailer_subtitle_textView);
            trailerImageView = itemView.findViewById(R.id.trailer_thumbnail_imageView);
            progressBar = itemView.findViewById(R.id.trailer_progressBar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + trailers.get(getAdapterPosition()).getKey()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setPackage("com.google.android.youtube");
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
