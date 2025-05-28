package edu.dyds.movies.domain.repository

import edu.dyds.movies.domain.entity.QualifiedMovie

interface MovieRepository {
    suspend fun getAllMovies() : List<QualifiedMovie>
    suspend fun getMovieDetail(id: Int)
}