package edu.dyds.movies.data

import edu.dyds.movies.data.external.MoviesExternalSource
import edu.dyds.movies.data.local.MoviesLocalSource
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MovieRepository

class MovieRepositoryImpl(
    private val moviesLocalSource: MoviesLocalSource,
    private val moviesExternalSource: MoviesExternalSource
) : MovieRepository {

    override suspend fun getMovieDetail(id: Int): Movie? {
        return try {
            moviesExternalSource.getMovieDetailsDB(id)
        } catch (e: Exception) { null }
    }

    override suspend fun getPopularMovies(): List<Movie> {
        if (!moviesLocalSource.hasMovies()) {
            try {
                    moviesLocalSource.clear()
                    moviesLocalSource.addMovies(moviesExternalSource.getPopularMovies())
            } catch (e: Exception) {
                emptyList<Movie>()
            }
        }
        return moviesLocalSource.getMovies()
    }
}