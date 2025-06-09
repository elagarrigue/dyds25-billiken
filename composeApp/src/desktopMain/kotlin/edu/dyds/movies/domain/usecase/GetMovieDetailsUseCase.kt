package edu.dyds.movies.domain.usecase

import edu.dyds.movies.domain.repository.MovieRepository
import edu.dyds.movies.domain.entity.Movie

class GetMovieDetailsUseCase(private val movieRepository: MovieRepository) {

    suspend fun getMovieDetail(id: Int): Movie? {
        return movieRepository.getMovieDetail(id)
    }

}