package ru.mospolytech.mobile_integration;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieList {
    //Сериализатор для получения объекта из json в java
    @SerializedName("results") // имя массива results (https://api.themoviedb.org/3/trending/movie/week?api_key=cc870e20173a7d922f25f0c963ff3668)
    List<MovieDetails> movies; //преобразуем список в список объектов типа MovieDetails
}
