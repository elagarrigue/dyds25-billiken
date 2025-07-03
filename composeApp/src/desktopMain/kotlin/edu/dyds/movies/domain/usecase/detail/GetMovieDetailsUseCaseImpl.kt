package edu.dyds.movies.domain.usecase.detail

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MovieRepository

class GetMovieDetailsUseCaseImpl(private val movieRepository: MovieRepository) : GetMovieDetailsUseCase {

    override suspend fun getMovieDetail(title: String): Movie? {
        return movieRepository.getMovieByTitle(title)
    }

}