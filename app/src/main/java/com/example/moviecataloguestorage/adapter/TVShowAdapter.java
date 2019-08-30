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
import com.example.moviecataloguestorage.model.TVShow;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TVShowAdapter extends RecyclerView.Adapter<TVShowAdapter.CategoryViewHolder> {

    private Context context;
    private List<TVShow> listTVShow;

    public TVShowAdapter(Context context, List<TVShow> listTVShow) {
        this.context = context;
        this.listTVShow = listTVShow;
    }

    public List<TVShow> getListTVShow() {
        return listTVShow;
    }

    public void setListTVShow(List<TVShow> listTVShow) {
        this.listTVShow = listTVShow;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_tv_show, viewGroup, false);

        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, int i) {
        categoryViewHolder.tvTitle.setText(getListTVShow().get(i).getName());
        categoryViewHolder.tvRating.setText("Rating: "+getListTVShow().get(i).getVote_average());
        categoryViewHolder.tvDescription.setText(getListTVShow().get(i).getOverview());

        Glide.with(context.getApplicationContext())
                .load("http://image.tmdb.org/t/p/w500"+getListTVShow().get(i).getPoster_path())
                .into(categoryViewHolder.imgPoster);

    }

    @Override
    public int getItemCount() {
        return listTVShow.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_poster_tvshow)
        ImageView imgPoster;
        @BindView(R.id.tv_title_tvshow)
        TextView tvTitle;
        @BindView(R.id.tv_rating_tvshow)
        TextView tvRating;
        @BindView(R.id.tv_description_tvshow)
        TextView tvDescription;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}