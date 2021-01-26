package ru.mospolytech.mobile_integration;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    Context context;
    List<MovieDetails> list;

    public ListAdapter(Context context, List<MovieDetails> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MovieDetails movie = list.get(position);
        holder.factIdText.setText(movie.title);
        holder.releaseDate.setText("Дата выхода фильма: "+ movie.date);
        Log.d(TAG, "onBindViewHolder: " + movie.poster);
        Glide.with(context).load("https://image.tmdb.org/t/p/w500" + movie.poster).into(holder.factImage);
        holder.item.setOnClickListener(v -> {
            Intent intent = new Intent(context, MoviesActivity.class);
            intent.putExtra("movieid", movie.id);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView factImage;
        TextView factIdText;
        TextView releaseDate;
        TextView sourceView;
        LinearLayout item;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            factImage = itemView.findViewById(R.id.moviesImage);
            factIdText = itemView.findViewById(R.id.moviesIdText);
            sourceView = itemView.findViewById(R.id.sourceView);
            releaseDate = itemView.findViewById(R.id.dateMovies);
            item = itemView.findViewById(R.id.item);
        }
    }
}
