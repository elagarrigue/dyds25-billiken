package edu.dyds.movies.data.external.tmdb

import edu.dyds.movies.data.external.MoviesExternalSource
import edu.dyds.movies.data.external.omdb.OMDBMoviesExternalSource
import edu.dyds.movies.domain.entity.Movie
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


class TMDBExternalSourceImpl(
    private val tmdbHttpClient: HttpClient,
) : OMDBMoviesExternalSource, TMDBMoviesExternalSource {

    override suspend fun getPopularMovies(): List<Movie> {
        val remoteResult: RemoteResult = tmdbHttpClient.get("/3/discover/movie?sort_by=popularity.desc").body()
        return remoteResult.results.map { it.toDomainMovie() }
    }

    override suspend fun getMovieByTitle(title: String): Movie {
        val remoteMovie: RemoteMovie = tmdbHttpClient.get("/3/search/movie?query=$title").body()
        return remoteMovie.toDomainMovie()
    }
}

@Serializable
data class RemoteResult(
    val page: Int,
    val results: List<RemoteMovie>,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_results") val totalResults: Int
)

@Serializable
data class RemoteMovie(
    val id: Int,
    val title: String,
    val overview: String,
    @SerialName("release_date") val releaseDate: String,
    @SerialName("poster_path") val posterPath: String,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("original_title") val originalTitle: String,
    @SerialName("original_language") val originalLanguage: String,
    val popularity: Double,
    @SerialName("vote_average") val voteAverage: Double,

    ) {
    fun toDomainMovie(): Movie {
        return Movie(
            id = id,
            title = title,
            overview = overview,
            releaseDate = releaseDate,
            poster = "https://image.tmdb.org/t/p/w185$posterPath",
            backdrop = backdropPath.let { "https://image.tmdb.org/t/p/w780$it" },
            originalTitle = originalTitle,
            originalLanguage = originalLanguage,
            popularity = popularity,
            voteAverage = voteAverage
        )
    }
}