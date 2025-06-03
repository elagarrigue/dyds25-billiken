package edu.dyds.movies.domain.usecase

import edu.dyds.movies.domain.repository.MovieRepository
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.data.external.RemoteMovie

private const val MIN_VOTE_AVERAGE = 6.0

class GetPopularMoviesUseCase (private val movieRepository: MovieRepository) {

    suspend fun getAllMovies(): List<QualifiedMovie> {
        return movieRepository.getPopularMovies().sortAndMap()
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

}

