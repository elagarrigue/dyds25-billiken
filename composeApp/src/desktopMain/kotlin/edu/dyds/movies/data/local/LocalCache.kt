package edu.dyds.movies.data.local

import edu.dyds.movies.domain.entity.RemoteMovie

private val cacheMovies: MutableList<RemoteMovie> = mutableListOf()

class LocalCache() {

    fun getMovies() : MutableList<RemoteMovie>{
        return cacheMovies
    }

    fun addMovies(movies : Collection<RemoteMovie>){
        cacheMovies.addAll(movies)
    }

    fun hasMovies() : Boolean{
        return cacheMovies.isEmpty()
    }

    fun clear() {
        cacheMovies.clear()
    }
}