package edu.dyds.movies.data.local

import edu.dyds.movies.domain.entity.Movie

interface MoviesLocalSource {
    fun getMovies(): MutableList<Movie>
    fun addMovies(movies: Collection<Movie>)
    fun hasMovies(): Boolean
    fun clear()
}