package com.example.dependencyinjection

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.dependencyinjection.model.Movie
import com.example.dependencyinjection.ui.theme.DependencyInjectionTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DependencyInjectionTheme {
                val viewModel: MovieViewModel = hiltViewModel()
                val movies by viewModel.movies.observeAsState(emptyList())
                val selectedTab = remember { mutableStateOf("list") }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "Movie Explorer",
                                    color = Color.White
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color(0xFF1E1E1E) // Dark background
                            ),
                            modifier = Modifier.height(64.dp) // Adjust height
                        )
                    },
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.List, contentDescription = "List") },
                                label = { Text("List") },
                                selected = selectedTab.value == "list",
                                onClick = { selectedTab.value = "list" }
                            )
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.Favorite, contentDescription = "Favourites") },
                                label = { Text("Favourites") },
                                selected = selectedTab.value == "favourites",
                                onClick = { selectedTab.value = "favourites" }
                            )
                        }
                    }
                ) { paddingValues -> // Content area with padding for top bar and bottom nav
                    // Content display based on selected tab
                    when (selectedTab.value) {
                        "list" -> {
                            LaunchedEffect(Unit) {
                                viewModel.fetchPopularMovies(ApiUtils.TMDB_API_KEY)
                            }
                            // Apply paddingValues to ensure the content isn't obscured
                            MovieListScreen(movies = movies, modifier = Modifier.padding(paddingValues))
                        }

                        "favourites" -> {
                            FavouriteMoviesScreen(favourites = viewModel.getFavourites(), modifier = Modifier.padding(paddingValues))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovieListScreen(movies: List<Movie.Result?>, modifier: Modifier = Modifier) {
    val viewModel: MovieViewModel = hiltViewModel()

    // Apply content padding to LazyColumn but NOT on the individual items
    LazyColumn(modifier = modifier) {  // Use modifier with padding here
        items(movies) { movie ->
            MovieItem(movie = movie, onFavouriteClicked = { movie ->
                viewModel.toggleFavourite(movie)
            })
        }
    }
}

@Composable
fun FavouriteMoviesScreen(favourites: List<Movie.Result?>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {  // Use modifier with padding here
        items(favourites) { movie ->
            MovieItem(movie = movie, onFavouriteClicked = {  })
        }
    }
}

@Composable
fun MovieItem(movie: Movie.Result?, onFavouriteClicked: (Movie.Result) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            val painter = rememberAsyncImagePainter(
                model = "https://image.tmdb.org/t/p/w500${movie?.posterPath}",
                placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                error = painterResource(id = R.drawable.ic_launcher_foreground)
            )

            Image(
                painter = painter,
                contentDescription = stringResource(R.string.movie_poster),
                modifier = Modifier.size(100.dp, 150.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .fillMaxWidth()
            ) {
                movie?.title?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                movie?.overview?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Add a Row to display Checkbox and Text
                Row(
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    // Text behind the checkbox
                    Text(
                        text = "Mark as Favourite",
                        modifier = Modifier
                            .weight(1f) // Makes the text take up the space before the checkbox
                            .align(Alignment.CenterVertically)
                    )

                    // Checkbox with onClick event to toggle favourite
                    val isFavourite = movie?.isFavourite ?: false
                    Checkbox(
                        checked = isFavourite,
                        onCheckedChange = { isChecked ->
                            movie?.isFavourite = isChecked
                            movie?.let { onFavouriteClicked(it) }
                        },
                        modifier = Modifier
                            .align(Alignment.CenterVertically) // Aligns checkbox vertically in the row
                    )
                }
            }
        }
    }
}
