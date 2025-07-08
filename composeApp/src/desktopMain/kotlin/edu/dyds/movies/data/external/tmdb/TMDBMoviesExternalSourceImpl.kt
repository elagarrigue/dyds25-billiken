package edu.dyds.movies.data.external.tmdb

import edu.dyds.movies.data.external.MovieDetailExternalSource
import edu.dyds.movies.data.external.PopularMoviesExternalSource
import edu.dyds.movies.domain.entity.Movie
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


class TMDBExternalSourceImpl(
    private val tmdbHttpClient: HttpClient,
) : MovieDetailExternalSource, PopularMoviesExternalSource {

    override suspend fun getPopularMovies(): List<Movie> {
        val remoteResult: RemoteResult = tmdbHttpClient.get("/3/discover/movie?sort_by=popularity.desc").body()
        return remoteResult.results.map { it.toDomainMovie() }
    }

    override suspend fun getMovieByTitle(title: String): Movie? =
        try {
            getTMDBMovieDetails(title).apply { println(this) }.results.first().toDomainMovie()
        } catch (e: Exception) {
            null
        }

    private suspend fun getTMDBMovieDetails(title: String): RemoteResult =
        tmdbHttpClient.get("/3/search/movie?query=$title").body()
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
    @SerialName("release_date") val releaseDate: String?,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("original_title") val originalTitle: String,
    @SerialName("original_language") val originalLanguage: String,
    val popularity: Double?,
    @SerialName("vote_average") val voteAverage: Double?,

    ) {
    fun toDomainMovie(): Movie {
        return Movie(
            id = id,
            title = title,
            overview = overview,
            releaseDate = releaseDate ?: "",
            poster = "https://image.tmdb.org/t/p/w185$posterPath",
            backdrop = backdropPath.let { "https://image.tmdb.org/t/p/w780$it" },
            originalTitle = originalTitle,
            originalLanguage = originalLanguage,
            popularity = popularity ?: 0.0,
            voteAverage = voteAverage ?: 0.0
        )
    }
}