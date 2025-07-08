package edu.dyds.movies.data

import edu.dyds.movies.data.external.MovieDetailExternalSource
import edu.dyds.movies.data.external.PopularMoviesExternalSource
import edu.dyds.movies.data.local.MoviesLocalSource
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MovieRepository

class MovieRepositoryImpl(
    private val moviesLocalSource: MoviesLocalSource,
    private val popularMoviesExternalSource: PopularMoviesExternalSource,
    private val movieDetailExternalSource: MovieDetailExternalSource
) : MovieRepository {

    override suspend fun getMovieByTitle(title: String): Movie? {
        return try {
            movieDetailExternalSource.getMovieByTitle(title)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getPopularMovies(): List<Movie> {
        if (!moviesLocalSource.hasMovies()) {
            try {
                moviesLocalSource.clear()
                moviesLocalSource.addMovies(popularMoviesExternalSource.getPopularMovies())
            } catch (e: Exception) {
                emptyList<Movie>()
            }
        }
        return moviesLocalSource.getMovies()
    }
}