package com.example.dependencyinjection

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dependencyinjection.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {

    private val _movies = MutableLiveData<List<Movie.Result?>>()
    val movies: LiveData<List<Movie.Result?>> = _movies

    private val _favourites = mutableStateOf<List<Movie.Result>>(emptyList()) // Track favourites

    fun fetchPopularMovies(apiKey: String) {
        viewModelScope.launch {
            try {
                val response = repository.getPopularMovies(apiKey)
                if (response.isSuccessful) {
                    val movieList = response.body()?.results
                    movieList?.let {
                        _movies.value = it
                    }
                } else {
                    // Handle error
                }
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }

    // Call this function when a movie's favourite status is changed
    fun toggleFavourite(movie: Movie.Result) {
        val updatedFavourites = if (movie.isFavourite) {
            _favourites.value + movie // Add to favourites
        } else {
            _favourites.value - movie // Remove from favourites
        }
        _favourites.value = updatedFavourites
    }

    fun getFavourites(): List<Movie.Result> {
        return _favourites.value
    }
}
