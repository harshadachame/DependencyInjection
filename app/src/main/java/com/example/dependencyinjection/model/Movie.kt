package com.example.dependencyinjection.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Movie(
    @SerializedName("page") val page: Int?, // 1
    @SerializedName("results") val results: List<Result?>?,
    @SerializedName("total_pages") val totalPages: Int?, // 50176
    @SerializedName("total_results") val totalResults: Int? // 1003507
) {
    @Keep
    data class Result(
        @SerializedName("adult") val adult: Boolean?, // false
        @SerializedName("backdrop_path") val backdropPath: String?, // /bVm6udIB6iKsRqgMdQh6HywuEBj.jpg
        @SerializedName("genre_ids") val genreIds: List<Int?>?,
        @SerializedName("id") val id: Int?, // 1233069
        @SerializedName("original_language") val originalLanguage: String?, // de
        @SerializedName("original_title") val originalTitle: String?, // Exterritorial
        @SerializedName("overview") val overview: String?, // When her son vanishes inside a US consulate, ex-special forces soldier Sara does everything in her power to find him — and uncovers a dark conspiracy.
        @SerializedName("popularity") val popularity: Double?, // 599.2458
        @SerializedName("poster_path") val posterPath: String?, // /jM2uqCZNKbiyStyzXOERpMqAbdx.jpg
        @SerializedName("release_date") val releaseDate: String?, // 2025-04-29
        @SerializedName("title") val title: String?, // Exterritorial
        @SerializedName("video") val video: Boolean?, // false
        @SerializedName("vote_average") val voteAverage: Double?, // 6.7
        @SerializedName("vote_count") val voteCount: Int? ,// 208
        var isFavourite: Boolean = false // Add this property
    )
}