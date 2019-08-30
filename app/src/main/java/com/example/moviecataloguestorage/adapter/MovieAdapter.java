package com.example.moviecataloguestorage.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.moviecataloguestorage.R;
import com.example.moviecataloguestorage.model.Movie;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.CategoryViewHolder> {

    public final static String EXTRA_MOVIE = "extra_movie";

    private Context context;
    private List<Movie> listMovie;

    public MovieAdapter(Context context, List<Movie> listMovie) {
        this.context = context;
        this.listMovie = listMovie;
    }

    public List<Movie> getListMovie() {
        return listMovie;
    }

    public void setListMovie(List<Movie> listMovie) {
        this.listMovie = listMovie;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_movie, viewGroup, false);

        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, int i) {
        categoryViewHolder.tvTitle.setText(getListMovie().get(i).getTitle());
        categoryViewHolder.tvRating.setText("Rating: "+getListMovie().get(i).getVote_average());
        categoryViewHolder.tvDescription.setText(getListMovie().get(i).getOverview());

        Glide.with(context.getApplicationContext())
                .load("http://image.tmdb.org/t/p/w500"+getListMovie().get(i).getPoster_path())
                .into(categoryViewHolder.imgPoster);
    }

    @Override
    public int getItemCount() {
        return listMovie.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_poster)
        ImageView imgPoster;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_rating)
        TextView tvRating;
        @BindView(R.id.tv_description)
        TextView tvDescription;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
