package ru.mospolytech.mobile_integration;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieList {
    @SerializedName("results")
    List<MovieDetails> movies;
}
