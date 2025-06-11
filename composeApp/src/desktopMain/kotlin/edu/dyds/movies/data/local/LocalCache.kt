package edu.dyds.movies.data.local

import edu.dyds.movies.domain.entity.Movie

class LocalCache() : MoviesLocalSource {

    private val cacheMovies: MutableList<Movie> = mutableListOf()

    override fun getMovies(): MutableList<Movie> {
        return cacheMovies
    }

    override fun addMovies(movies: Collection<Movie>) {
        cacheMovies.addAll(movies)
    }

    override fun hasMovies(): Boolean {
        return !cacheMovies.isEmpty()
    }

    override fun clear() {
        cacheMovies.clear()
    }
}