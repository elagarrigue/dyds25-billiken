package data

import edu.dyds.movies.data.MovieRepositoryImpl
import edu.dyds.movies.data.external.MovieDetailExternalSource
import edu.dyds.movies.data.external.tmdb.TMDBMoviesExternalSource
import edu.dyds.movies.data.local.MoviesLocalSource
import edu.dyds.movies.domain.entity.Movie
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MovieRepositoryImplTest {

    private lateinit var localSource: MoviesLocalSource
    private lateinit var popularMoviesExternalSource: TMDBMoviesExternalSource
    private lateinit var movieDetailExternalSource: MovieDetailExternalSource
    private lateinit var repository: MovieRepositoryImpl
    private val fakeMovie: Movie = mockk()

    @BeforeTest
    fun setUp() {
        localSource = mockk(relaxUnitFun = true)
        popularMoviesExternalSource = mockk(relaxUnitFun = true)
        movieDetailExternalSource = mockk(relaxUnitFun = true)
        repository = MovieRepositoryImpl(localSource, popularMoviesExternalSource, movieDetailExternalSource)
    }

    @Test
    fun `getMovieByTitle returns movie from Broker`() = runTest {
        // Arrange
        coEvery { movieDetailExternalSource.getMovieByTitle("MovieEjemplo") } returns fakeMovie

        // Act
        val result = repository.getMovieByTitle("MovieEjemplo")

        // Assert
        assertEquals(fakeMovie, result)
    }

    @Test
    fun `getMovieByTitle returns null when Broker throws exception`() = runTest {
        // Arrange
        coEvery { movieDetailExternalSource.getMovieByTitle("MovieEjemplo") } throws Exception("error")

        // Act
        val result = repository.getMovieByTitle("MovieEjemplo")

        // Assert
        assertNull(result)
    }

    @Test
    fun `getPopularMovies updates local source when it is empty and returns new data from TMDB`() = runTest {
        // Arrange
        coEvery { localSource.hasMovies() } returns false
        val movies = mutableListOf(fakeMovie)
        coEvery { popularMoviesExternalSource.getPopularMovies() } returns movies
        coEvery { localSource.getMovies() } returns movies

        // Act
        val result = repository.getPopularMovies()

        // Assert
        assertEquals(movies, result)
        coVerify { localSource.addMovies(movies) }
    }

    @Test
    fun `getPopularMovies returns empty list if TMDB source fails`() = runTest {
        // Arrange
        coEvery { localSource.hasMovies() } returns false
        coEvery { popularMoviesExternalSource.getPopularMovies() } throws Exception("error")
        coEvery { localSource.getMovies() } returns mutableListOf()

        // Act
        val result = repository.getPopularMovies()

        // Assert
        assertEquals(emptyList(), result)
    }

    @Test
    fun `getPopularMovies returns local movies when local is not empty`() = runTest {
        // Arrange
        coEvery { localSource.hasMovies() } returns true
        val movies = mutableListOf(fakeMovie)
        coEvery { localSource.getMovies() } returns movies

        // Act
        val result = repository.getPopularMovies()

        // Assert
        assertEquals(movies, result)
    }
}