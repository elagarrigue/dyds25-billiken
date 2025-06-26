package edu.dyds.movies.data.external

import edu.dyds.movies.data.external.omdb.OMDBMoviesExternalSource
import edu.dyds.movies.data.external.tmdb.TMDBMoviesExternalSource
import edu.dyds.movies.domain.entity.Movie

class MoviesExternalSourceBroker (
    tmdb: TMDBMoviesExternalSource,
    omdb: OMDBMoviesExternalSource
) : OMDBMoviesExternalSource {

    override suspend fun getMovieDetailsDB(id: Int): Movie {
        TODO("Not yet implemented")
    }
}