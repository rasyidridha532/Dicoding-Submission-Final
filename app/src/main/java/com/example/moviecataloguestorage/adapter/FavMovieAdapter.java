package com.example.moviecataloguestorage.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviecataloguestorage.R;
import com.example.moviecataloguestorage.model.Movie;
import com.example.moviecataloguestorage.view.detailfavorit.DetailFavMovieActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavMovieAdapter extends RecyclerView.Adapter<FavMovieAdapter.MovieViewHolder> {

    private ArrayList<Movie> movies;
    private Context context;
    private Cursor cursor;

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public FavMovieAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_favorite, parent, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = getItem(position);

        holder.tvTitle.setText(getItem(position).getTitle());
        holder.tvRating.setText(String.format("Rating: %s", getItem(position).getVote_average()));
        holder.tvDesc.setText(getItem(position).getOverview());

        Glide.with(context.getApplicationContext())
                .load("http://image.tmdb.org/t/p/w500" + getItem(position).getPoster_path())
                .into(holder.imgPoster);

        holder.cvMovie.setOnClickListener(v -> {
            Intent detail = new Intent(context, DetailFavMovieActivity.class);

            detail.putExtra("id_movie",movie.getId());
            detail.putExtra("judul",movie.getTitle());
            detail.putExtra("rating",movie.getVote_average());
            detail.putExtra("link_poster",movie.getPoster_path());
            detail.putExtra("deskripsi",movie.getOverview());

            context.startActivity(detail);
        });
    }

    @Override
    public int getItemCount() {
        if (cursor == null) {
            return 0;
        }
        return cursor.getCount();
    }

    private Movie getItem(int position) {
        if (!cursor.moveToPosition(position)) {
            throw new IllegalStateException("Position invalid");
        }
        return new Movie(cursor);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title_fav)
        TextView tvTitle;
        @BindView(R.id.tv_description_fav)
        TextView tvDesc;
        @BindView(R.id.tv_rating_fav)
        TextView tvRating;

        @BindView(R.id.img_poster_fav)
        ImageView imgPoster;

        @BindView(R.id.card_view)
        CardView cvMovie;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}