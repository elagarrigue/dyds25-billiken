package edu.dyds.movies.data

import edu.dyds.movies.data.external.TheMoviesDataBase
import edu.dyds.movies.data.local.LocalCache
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.entity.RemoteMovie
import edu.dyds.movies.domain.repository.MovieRepository

private const val MIN_VOTE_AVERAGE = 6.0

class MovieRepositoryImpl( private val cache: LocalCache, private val theMoviesDataBase : TheMoviesDataBase) : MovieRepository {

    override suspend fun getAllMovies() : List<QualifiedMovie> {
       return getPopularMovies().sortAndMap()
    }

    override suspend fun getMovieDetail(id: Int) {
        getMovieDetails(id)?.toDomainMovie()
    }

    private suspend fun getPopularMovies() : List<RemoteMovie>{
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

    private fun List<RemoteMovie>.sortAndMap(): List<QualifiedMovie> {
        return this
            .sortedByDescending { it.voteAverage }
            .map {
                QualifiedMovie(
                    movie = it.toDomainMovie(),
                    isGoodMovie = it.voteAverage >= MIN_VOTE_AVERAGE
                )
            }
    }

    suspend fun getMovieDetails(id: Int) =
        try {
            theMoviesDataBase.getMovieDetails(id)
        } catch (e: Exception) {
            null
        }

}