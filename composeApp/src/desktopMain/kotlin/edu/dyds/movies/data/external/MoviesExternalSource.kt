package edu.dyds.movies.data.external

import edu.dyds.movies.domain.entity.Movie

interface MoviesExternalSource {
    suspend fun getPopularMovies(): List<Movie>
    suspend fun getMovieDetailsDB(id: Int): Movie
}