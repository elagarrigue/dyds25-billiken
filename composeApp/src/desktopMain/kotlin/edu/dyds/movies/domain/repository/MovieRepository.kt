package edu.dyds.movies.domain.repository

import edu.dyds.movies.domain.entity.Movie

interface MovieRepository {
    suspend fun getPopularMovies(): List<Movie>
    suspend fun getMovieByTitle(title: String): Movie?
}