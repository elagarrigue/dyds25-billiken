package edu.dyds.movies.domain.usecase.detail

import edu.dyds.movies.domain.entity.Movie

interface GetMovieDetailsUseCase {
    suspend fun getMovieDetail(title: String): Movie?
}