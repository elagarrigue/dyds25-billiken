package edu.dyds.movies.data.external.omdb

import edu.dyds.movies.domain.entity.Movie

interface OMDBMoviesExternalSource {
    suspend fun getMovieDetailsDB(id: Int): Movie
}