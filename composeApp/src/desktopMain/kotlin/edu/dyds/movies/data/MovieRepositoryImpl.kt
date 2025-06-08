package edu.dyds.movies.data

import edu.dyds.movies.data.external.MoviesExternalSource
import edu.dyds.movies.data.local.MoviesLocalSource
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MovieRepository

class MovieRepositoryImpl(private val moviesLocalSource: MoviesLocalSource, private val moviesExternalSource : MoviesExternalSource) : MovieRepository {

    override suspend fun getMovieDetail(id: Int): Movie? {
        return moviesExternalSource.getMovieDetailsDB(id).toDomainMovie()
    }

    override suspend fun getPopularMovies() : List<Movie>{
        if (!moviesLocalSource.hasMovies()) {
            try {
                moviesExternalSource.getPopularMovies().results.apply {
                    moviesLocalSource.clear()
                    val listMovies : MutableList<Movie> = mutableListOf()
                    this.forEach { remoteMovie -> listMovies.add(remoteMovie.toDomainMovie()) }
                    moviesLocalSource.addMovies(listMovies)
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
        return moviesLocalSource.getMovies()
    }
}