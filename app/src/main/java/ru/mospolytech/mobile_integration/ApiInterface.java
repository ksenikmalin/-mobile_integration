package ru.mospolytech.mobile_integration;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("3/trending/movie/{time}?api_key=cc870e20173a7d922f25f0c963ff3668&language=ru-RU")
    Observable<MovieList> trendingMovies(@Path("time") String time);

    @GET("3/movie/{movieID}?api_key=cc870e20173a7d922f25f0c963ff3668&language=ru-RU")
    Observable<MovieDetails> movieInfo(@Path("movieID") String id);
}
