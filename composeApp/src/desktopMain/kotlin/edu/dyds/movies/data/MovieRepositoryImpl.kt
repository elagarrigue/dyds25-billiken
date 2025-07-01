package edu.dyds.movies.data

import edu.dyds.movies.data.external.MoviesExternalSource
import edu.dyds.movies.data.external.omdb.OMDBMoviesExternalSource
import edu.dyds.movies.data.external.tmdb.TMDBMoviesExternalSource
import edu.dyds.movies.data.local.MoviesLocalSource
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MovieRepository

class MovieRepositoryImpl(
    private val moviesLocalSource: MoviesLocalSource,
    private val TMDB: TMDBMoviesExternalSource,
    private val OMDB: OMDBMoviesExternalSource
) : MovieRepository {

    override suspend fun getMovieByTitle( title: String): Movie? {
        return try {
            OMDB.getMovieByTitle(title)
        } catch (e: Exception) { null }
    }

    override suspend fun getPopularMovies(): List<Movie> {
        if (!moviesLocalSource.hasMovies()) {
            try {
                    moviesLocalSource.clear()
                    moviesLocalSource.addMovies(TMDB.getPopularMovies())
            } catch (e: Exception) {
                emptyList<Movie>()
            }
        }
        return moviesLocalSource.getMovies()
    }
}