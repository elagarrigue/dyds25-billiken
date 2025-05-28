package edu.dyds.movies.data

import edu.dyds.movies.data.external.TheMoviesDataBase
import edu.dyds.movies.data.local.LocalCache
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.entity.RemoteMovie
import edu.dyds.movies.domain.repository.MovieRepository

class MovieRepositoryImpl( private val cache: LocalCache, private val theMoviesDataBase : TheMoviesDataBase) : MovieRepository {

    override suspend fun getMovieDetail(id: Int): Movie? {
        return theMoviesDataBase.getMovieDetailsDB(id).toDomainMovie()
    }

    override suspend fun getPopularMovies() : List<RemoteMovie>{
        if (!cache.hasMovies()) {
            try {
                theMoviesDataBase.getPopularMovies().results.apply {
                    cache.clear()
                    cache.addMovies(this)
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
        return cache.getMovies()
    }
}