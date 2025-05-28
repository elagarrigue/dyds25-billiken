package edu.dyds.movies.domain.repository

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.entity.RemoteMovie

interface MovieRepository {
    suspend fun getPopularMovies() : List<RemoteMovie>
    suspend fun getMovieDetail(id: Int): Movie?
}