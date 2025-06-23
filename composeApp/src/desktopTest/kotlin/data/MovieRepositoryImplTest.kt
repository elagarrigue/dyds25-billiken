package data

import edu.dyds.movies.data.MovieRepositoryImpl
import edu.dyds.movies.data.external.MoviesExternalSource
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
    private lateinit var externalSource: MoviesExternalSource
    private lateinit var repository: MovieRepositoryImpl
    private val fakeMovie: Movie = mockk()

    @BeforeTest
    fun setUp() {
        localSource = mockk(relaxUnitFun = true)
        externalSource = mockk(relaxUnitFun = true)
        repository = MovieRepositoryImpl(localSource, externalSource)
    }

    @Test
    fun `getMovieDetail returns movie from external source`() = runTest {
        // Arrange
        coEvery { externalSource.getMovieDetailsDB(1) } returns fakeMovie

        // Act
        val result = repository.getMovieDetail(1)

        // Assert
        assertEquals(fakeMovie, result)
    }

    @Test
    fun `getMovieDetail returns null when no movie with that name`() = runTest {
        // Arrange
        coEvery { externalSource.getMovieDetailsDB(1) } throws Exception("error")

        // Act
        val result = repository.getMovieDetail(1)

        // Assert
        assertNull(result)
    }

    @Test
    fun `getPopularMovies updates local source when it is empty and returns new data from external`() = runTest {
        // Arrange
        coEvery { localSource.hasMovies() } returns false
        val movies = mutableListOf(fakeMovie)
        coEvery { externalSource.getPopularMovies() } returns movies
        coEvery { localSource.getMovies() } returns movies

        // Act
        val result = repository.getPopularMovies()

        // Assert
        assertEquals(movies, result)
        coVerify { localSource.addMovies(movies) }
    }

    @Test
    fun `getPopularMovies returns empty list if external source fails`() = runTest {
        // Arrange
        coEvery { localSource.hasMovies() } returns false
        coEvery { externalSource.getPopularMovies() } throws Exception("error")
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