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
import com.example.moviecataloguestorage.model.TVShow;
import com.example.moviecataloguestorage.view.detailfavorit.DetailFavTVShowActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavTVShowAdapter extends RecyclerView.Adapter<FavTVShowAdapter.TVShowViewHolder> {

    private ArrayList<TVShow> tvShows;
    private Context context;
    private Cursor cursor;

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public FavTVShowAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public TVShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_favorite, parent, false);

        return new TVShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TVShowViewHolder holder, int position) {
        TVShow tvShow = getItem(position);

        holder.tvTitle.setText(getItem(position).getName());
        holder.tvDesc.setText(getItem(position).getOverview());
        holder.tvRating.setText(String.format("Rating: %s", getItem(position).getVote_average()));

        Glide.with(context.getApplicationContext())
                .load("http://image.tmdb.org/t/p/w500" + getItem(position).getPoster_path())
                .into(holder.imgPoster);

        holder.cvTV.setOnClickListener(v -> {
            Intent detail = new Intent(context, DetailFavTVShowActivity.class);

            detail.putExtra("id_tvshow",tvShow.getId());
            detail.putExtra("judul",tvShow.getName());
            detail.putExtra("rating",tvShow.getVote_average());
            detail.putExtra("link_poster",tvShow.getPoster_path());
            detail.putExtra("deskripsi",tvShow.getOverview());

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

    private TVShow getItem(int position) {
        if (!cursor.moveToPosition(position)) {
            throw new IllegalStateException("Position invalid");
        }
        return new TVShow(cursor);
    }

    class TVShowViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title_fav)
        TextView tvTitle;
        @BindView(R.id.tv_description_fav)
        TextView tvDesc;
        @BindView(R.id.tv_rating_fav)
        TextView tvRating;
        @BindView(R.id.img_poster_fav)
        ImageView imgPoster;
        @BindView(R.id.card_view)
        CardView cvTV;

        TVShowViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
