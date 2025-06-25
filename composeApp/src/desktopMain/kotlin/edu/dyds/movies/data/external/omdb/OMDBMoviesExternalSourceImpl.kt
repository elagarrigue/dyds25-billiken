package edu.dyds.movies.data.external.omdb

import edu.dyds.movies.domain.entity.Movie
import io.ktor.client.HttpClient

class OMDBMoviesExternalSourceImpl(
    private val omdbHttpClient: HttpClient,
) : OMDBMoviesExternalSource {

    override suspend fun getMovieDetailsDB(id: Int): Movie {
        TODO("Not yet implemented")
    }

}