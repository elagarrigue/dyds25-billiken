package edu.dyds.movies.data.local

import edu.dyds.movies.domain.entity.Movie

class MoviesLocalSource() {

    private val cacheMovies: MutableList<Movie> = mutableListOf()

    fun getMovies() : MutableList<Movie>{
        return cacheMovies
    }

    fun addMovies(movies : Collection<Movie>){
        cacheMovies.addAll(movies)
    }

    fun hasMovies() : Boolean{
        return !cacheMovies.isEmpty()
    }

    fun clear() {
        cacheMovies.clear()
    }
}