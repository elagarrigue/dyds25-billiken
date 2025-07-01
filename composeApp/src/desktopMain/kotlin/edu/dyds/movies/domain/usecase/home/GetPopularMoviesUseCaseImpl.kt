package edu.dyds.movies.domain.usecase.home

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.repository.MovieRepository

private const val MIN_VOTE_AVERAGE = 6.0

class GetPopularMoviesUseCaseImpl(private val movieRepository: MovieRepository) : GetPopularMoviesUseCase {

    override suspend fun getAllMovies(): List<QualifiedMovie> {
        return movieRepository.getPopularMovies().sortAndMap()
    }

    private fun List<Movie>.sortAndMap(): List<QualifiedMovie> {
        return this
            .sortedByDescending { it.voteAverage }
            .map {
                QualifiedMovie(
                    movie = it,
                    isGoodMovie = it.voteAverage >= MIN_VOTE_AVERAGE
                )
            }
    }

}

