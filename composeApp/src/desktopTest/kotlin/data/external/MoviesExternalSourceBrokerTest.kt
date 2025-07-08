package data.external

import edu.dyds.movies.data.external.MoviesExternalSourceBroker
import edu.dyds.movies.data.external.MovieDetailExternalSource
import edu.dyds.movies.domain.entity.Movie
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MoviesExternalSourceBrokerTest {

    private lateinit var tmdbExternalSource: MovieDetailExternalSource
    private lateinit var omdbExternalSource: MovieDetailExternalSource
    private lateinit var broker: MoviesExternalSourceBroker

    private val tmdbMovie = Movie(
        id = 1,
        title = "Test Movie",
        overview = "Overview TMDB",
        releaseDate = "2024-01-01",
        poster = "poster.jpg",
        backdrop = "backdrop.jpg",
        originalTitle = "Original Title",
        originalLanguage = "en",
        popularity = 8.0,
        voteAverage = 7.0
    )

    private val omdbMovie = Movie(
        id = 1,
        title = "Test Movie",
        overview = "Overview OMDB",
        releaseDate = "2024-01-01",
        poster = "poster.jpg",
        backdrop = "backdrop.jpg",
        originalTitle = "Original Title",
        originalLanguage = "en",
        popularity = 6.0,
        voteAverage = 5.0
    )

    @BeforeTest
    fun setUp() {
        tmdbExternalSource = mockk()
        omdbExternalSource = mockk()
        broker = MoviesExternalSourceBroker(tmdbExternalSource, omdbExternalSource)
    }

    @Test
    fun `returns merged movie when both sources return movie`() = runTest {
        // Arrange
        coEvery { tmdbExternalSource.getMovieByTitle("Test Movie") } returns tmdbMovie
        coEvery { omdbExternalSource.getMovieByTitle("Test Movie") } returns omdbMovie

        // Act
        val result = broker.getMovieByTitle("Test Movie")

        // Assert
        val expected = Movie(
            id = 1,
            title = "Test Movie",
            overview = "TMDB: Overview TMDB\n\nOMDB: Overview OMDB",
            releaseDate = "2024-01-01",
            poster = "poster.jpg",
            backdrop = "backdrop.jpg",
            originalTitle = "Original Title",
            originalLanguage = "en",
            popularity = 7.0,
            voteAverage = 6.0
        )
        assertEquals(expected, result)
    }

    @Test
    fun `returns tmdb movie when only tmdb returns movie`() = runTest {
        // Arrange
        coEvery { tmdbExternalSource.getMovieByTitle("Test Movie") } returns tmdbMovie
        coEvery { omdbExternalSource.getMovieByTitle("Test Movie") } returns null

        // Act
        val result = broker.getMovieByTitle("Test Movie")

        // Assert
        val expected = tmdbMovie.copy(overview = "TMDB: ${tmdbMovie.overview}")
        assertEquals(expected, result)
    }

    @Test
    fun `returns omdb movie when only omdb returns movie`() = runTest {
        // Arrange
        coEvery { tmdbExternalSource.getMovieByTitle("Test Movie") } returns null
        coEvery { omdbExternalSource.getMovieByTitle("Test Movie") } returns omdbMovie

        // Act
        val result = broker.getMovieByTitle("Test Movie")

        // Assert
        val expected = omdbMovie.copy(overview = "OMDB: ${omdbMovie.overview}")
        assertEquals(expected, result)
    }

    @Test
    fun `returns null when both sources return null`() = runTest {
        // Arrange
        coEvery { tmdbExternalSource.getMovieByTitle("Test Movie") } returns null
        coEvery { omdbExternalSource.getMovieByTitle("Test Movie") } returns null

        // Act
        val result = broker.getMovieByTitle("Test Movie")

        // Assert
        assertNull(result)
    }
}