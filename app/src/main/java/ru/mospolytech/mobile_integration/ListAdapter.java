//Вывод списков фильмов
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

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> { //class ListAdapter, который содержит данные и связывает их со списком

    Context context;
    List<MovieDetails> list; //список фильмов

    public ListAdapter(Context context, List<MovieDetails> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //создаем объект ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_detail, parent, false);//создаётся layout строки списка
        return new ViewHolder(view); //возращается объект
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) { // создаем публичный метод, который принимает объект ViewHolder
        MovieDetails movie = list.get(position);//есть список фильмов, мы берем по отдельности каждый
        holder.factIdText.setText(movie.title); // показываю заголовок фильма в текстовом окне factIdText
        holder.releaseDate.setText("Дата выхода фильма: "+ movie.date); //показываю дату выхода фильма в тектовом окне releaseDate

        Log.d(TAG, "onBindViewHolder: " + movie.poster); // показываем постер/картинку фильма
        Glide.with(context).load("https://image.tmdb.org/t/p/w500" + movie.poster).into(holder.factImage); //url карттинки

        holder.item.setOnClickListener(v -> { // далее происходит оработка нажатия на фильм (элемент из списка)
            Intent intent = new Intent(context, MoviesActivity.class);
            intent.putExtra("movieid", movie.id); //передаем id фильма, который передается в get запрос
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
