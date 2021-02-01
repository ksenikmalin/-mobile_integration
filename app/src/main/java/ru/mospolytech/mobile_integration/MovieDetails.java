//здесь сериализую данные json из массива results в данные java

package ru.mospolytech.mobile_integration;

import com.google.gson.annotations.SerializedName;

public class MovieDetails {

    @SerializedName("id")
    String id;

    @SerializedName("title")
    String title;

    @SerializedName("vote_average")
    String voteAverage;

    @SerializedName("release_date")
    String date;

    @SerializedName("overview")
    String overview;

    @SerializedName("poster_path")
    String poster;

}
