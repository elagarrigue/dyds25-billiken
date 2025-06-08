package edu.dyds.movies.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.dyds.movies.data.MovieRepositoryImpl
import edu.dyds.movies.data.external.MoviesExternalSource
import edu.dyds.movies.data.local.MoviesLocalSource
import edu.dyds.movies.domain.repository.MovieRepository
import edu.dyds.movies.domain.usecase.GetMovieDetailsUseCase
import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCase
import edu.dyds.movies.presentation.detail.DetailViewModel
import edu.dyds.movies.presentation.home.HomeViewModel
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

private const val API_KEY = "d18da1b5da16397619c688b0263cd281"

object MoviesDependencyInjector {

    private val tmdbHttpClient =
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
            install(DefaultRequest) {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "api.themoviedb.org"
                    parameters.append("api_key", API_KEY)
                }
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 5000
            }
        }

    private val moviesExternalSource : MoviesExternalSource = MoviesExternalSource(tmdbHttpClient)

    private val moviesLocalSource : MoviesLocalSource = MoviesLocalSource()

    private val repositoryImpl : MovieRepository =  MovieRepositoryImpl(moviesLocalSource, moviesExternalSource)

    private val movieDetailsUseCase : GetMovieDetailsUseCase = GetMovieDetailsUseCase(repositoryImpl)

    private val popularMoviesUseCase : GetPopularMoviesUseCase = GetPopularMoviesUseCase(repositoryImpl)

    @Composable
    fun getDetailsViewModel(): DetailViewModel {
        return viewModel { DetailViewModel(movieDetailsUseCase) }
    }

    @Composable
    fun getHomeViewModel(): HomeViewModel {
        return viewModel { HomeViewModel(popularMoviesUseCase) }
    }
}