package com.example.dependencyinjection

import com.example.dependencyinjection.model.Movie
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("api_key") apiKey: String): Response<Movie>
}