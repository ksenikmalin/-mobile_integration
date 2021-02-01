//описание запросов к api
package ru.mospolytech.mobile_integration;

import io.reactivex.Observable;
// с помощью библиотки Retrofit мы превращаем HTTP API в интерфейс Java
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Path;

public interface ApiInterface {
    //GET запрос к api, к которому мы подставляем вместо параметра time неделя или день
    @GET("3/trending/movie/{time}?api_key=cc870e20173a7d922f25f0c963ff3668&language=ru-RU")
    //Observable  будет предоставлять данные типа MovieList, функции trendingMovies передается строка time
    Observable<MovieList> trendingMovies(@Path("time") String time);

    //запрос к api по ключу фильма (id) (https://api.themoviedb.org/3/movie/123?api_key=cc870e20173a7d922f25f0c963ff3668&language=ru-RU)
    @GET("3/movie/{movieID}?api_key=cc870e20173a7d922f25f0c963ff3668&language=ru-RU")
    Observable<MovieDetails> movieInfo(@Path("movieID") String id); //функции movieInfo с информацией о фильме
}
