package ru.mospolytech.mobile_integration;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MoviesActivity extends AppCompatActivity {
    TextView movieTitle;
    TextView movieOverview;
    TextView movieDate;
    ImageView moviePoster;
    ApiInterface api;
    private CompositeDisposable disposables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        movieTitle = findViewById(R.id.moviesHeader);
        movieOverview = findViewById(R.id.moviesBody);
        movieDate = findViewById(R.id.moviesDate);
        moviePoster = findViewById(R.id.moviesImageFull);
        api = ApiConfiguration.getApi();
        disposables = new CompositeDisposable();
        if (getIntent().getExtras() != null){
            disposables.add(
                    api.movieInfo(getIntent().getStringExtra("movieid"))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    (movie) -> {
                                        movieTitle.setText(movie.title);
                                        movieOverview.setText(movie.overview);
                                        movieDate.setText("Дата выхода: " + movie.date);

                                        Log.d(TAG, "onBindViewHolder: " + movie.poster);
                                        Glide.with(this).load("https://image.tmdb.org/t/p/w500" + movie.poster).into(moviePoster);
                                    },
                                    (error) -> {
                                        error.printStackTrace();
                                        Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
                                    }));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposables.isDisposed()) {
            disposables.dispose();
        }
    }
}
