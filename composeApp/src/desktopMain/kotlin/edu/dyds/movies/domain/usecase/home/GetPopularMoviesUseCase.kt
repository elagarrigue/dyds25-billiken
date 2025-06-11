package edu.dyds.movies.domain.usecase.home

import edu.dyds.movies.domain.entity.QualifiedMovie

interface GetPopularMoviesUseCase {
    suspend fun getAllMovies(): List<QualifiedMovie>
}