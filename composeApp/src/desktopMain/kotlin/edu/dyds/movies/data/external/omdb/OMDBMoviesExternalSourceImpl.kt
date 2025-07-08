package edu.dyds.movies.data.external.omdb

import edu.dyds.movies.data.external.MovieDetailExternalSource
import edu.dyds.movies.domain.entity.Movie
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class OMDBMoviesExternalSourceImpl(
    private val omdbHttpClient: HttpClient,
) : MovieDetailExternalSource {

    override suspend fun getMovieByTitle(title: String): Movie? =
        try {
            getOMDBMovieDetails(title).toDomainMovie()
        } catch (e: Exception) {
            null
        }

    private suspend fun getOMDBMovieDetails(title: String): RemoteMovie =
        omdbHttpClient.get("/?t=$title").body()

}

@Serializable
data class RemoteMovie(
    @SerialName(value = "Title") val title: String,
    @SerialName(value = "Plot") val plot: String,
    @SerialName(value = "Released") val released: String,
    @SerialName(value = "Year") val year: String,
    @SerialName(value = "Poster") val poster: String,
    @SerialName(value = "Language") val language: String,
    @SerialName(value = "Metascore") val metaScore: String,
    val imdbRating: Double,
) {
    fun toDomainMovie() = Movie(
        id = title.hashCode(),
        title = title,
        overview = plot,
        releaseDate = if (released.isNotEmpty() && released != "N/A") released else year,
        poster = poster,
        backdrop = poster,
        originalTitle = title,
        originalLanguage = language,
        popularity = imdbRating,
        voteAverage = if (metaScore.isNotEmpty() && metaScore != "N/A") metaScore.toDouble() else 0.0
    )
}