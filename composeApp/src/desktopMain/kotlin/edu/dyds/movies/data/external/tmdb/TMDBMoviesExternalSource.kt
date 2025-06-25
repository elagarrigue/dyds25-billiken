package edu.dyds.movies.data.external.tmdb

import edu.dyds.movies.domain.entity.Movie

interface TMDBMoviesExternalSource {
    suspend fun getPopularMovies(): List<Movie>
}