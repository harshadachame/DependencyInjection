package com.example.dependencyinjection

import com.example.dependencyinjection.model.Movie
import retrofit2.Response
import javax.inject.Inject

class MovieRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getPopularMovies(apiKey: String): Response<Movie> {
        return apiService.getPopularMovies(apiKey)
    }
}