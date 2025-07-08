package edu.dyds.movies.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.dyds.movies.data.MovieRepositoryImpl
import edu.dyds.movies.data.external.MoviesExternalSourceBroker
import edu.dyds.movies.data.external.MovieDetailExternalSource
import edu.dyds.movies.data.external.omdb.OMDBMoviesExternalSourceImpl
import edu.dyds.movies.data.external.tmdb.TMDBExternalSourceImpl
import edu.dyds.movies.data.local.LocalCache
import edu.dyds.movies.data.local.MoviesLocalSource
import edu.dyds.movies.domain.repository.MovieRepository
import edu.dyds.movies.domain.usecase.detail.GetMovieDetailsUseCase
import edu.dyds.movies.domain.usecase.detail.GetMovieDetailsUseCaseImpl
import edu.dyds.movies.domain.usecase.home.GetPopularMoviesUseCase
import edu.dyds.movies.domain.usecase.home.GetPopularMoviesUseCaseImpl
import edu.dyds.movies.presentation.detail.DetailViewModel
import edu.dyds.movies.presentation.home.HomeViewModel
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

private const val TMDB_API_KEY = "d18da1b5da16397619c688b0263cd281"
private const val OMDB_API_KEY = "a96e7f78"

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
                    parameters.append("api_key", TMDB_API_KEY)
                }
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 5000
            }
        }

    private val omdbHttpClient =
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
            install(DefaultRequest) {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "www.omdbapi.com"
                    parameters.append("apikey", OMDB_API_KEY)
                }
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 5000
            }
        }

    private val tmdbExternalSource = TMDBExternalSourceImpl(tmdbHttpClient)

    private val omdbExternalSource: MovieDetailExternalSource = OMDBMoviesExternalSourceImpl(omdbHttpClient)

    private val moviesExternalSourceBroker: MovieDetailExternalSource =
        MoviesExternalSourceBroker(tmdbExternalSource, omdbExternalSource)

    private val localCache: MoviesLocalSource = LocalCache()

    private val repositoryImpl: MovieRepository =
        MovieRepositoryImpl(localCache, tmdbExternalSource, moviesExternalSourceBroker)

    private val movieDetailsUseCase: GetMovieDetailsUseCase = GetMovieDetailsUseCaseImpl(repositoryImpl)

    private val popularMoviesUseCase: GetPopularMoviesUseCase = GetPopularMoviesUseCaseImpl(repositoryImpl)

    @Composable
    fun getDetailsViewModel(): DetailViewModel {
        return viewModel { DetailViewModel(movieDetailsUseCase) }
    }

    @Composable
    fun getHomeViewModel(): HomeViewModel {
        return viewModel { HomeViewModel(popularMoviesUseCase) }
    }
}