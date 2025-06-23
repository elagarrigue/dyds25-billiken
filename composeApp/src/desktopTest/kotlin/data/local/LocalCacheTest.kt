package data.local

import edu.dyds.movies.data.local.LocalCache
import edu.dyds.movies.domain.entity.Movie
import io.mockk.mockk
import kotlin.test.*

class LocalCacheTest {

    private lateinit var cache: LocalCache
    private val fakeMovie: Movie = mockk()

    @BeforeTest
    fun setUp() {
        cache = LocalCache()
    }

    @Test
    fun `getMovies returns empty list`() {
        // Act
        val movies = cache.getMovies()

        // Assert
        assertTrue(movies.isEmpty())
    }

    @Test
    fun `addMovies adds movies to cache`() {
        // Act
        cache.addMovies(listOf(fakeMovie))

        // Assert
        assertEquals(listOf(fakeMovie), cache.getMovies())
    }

    @Test
    fun `hasMovies returns true when cache is not empty`() {
        // Act
        cache.addMovies(listOf(fakeMovie))

        //Assert
        assertTrue(cache.hasMovies())
    }

    @Test
    fun `hasMovies returns false when cache is empty`() {
        // Act & Assert
        assertFalse(cache.hasMovies())
    }

    @Test
    fun `clear removes all movies from cache`() {
        // Arrange
        cache.addMovies(listOf(fakeMovie))

        // Act
        cache.clear()

        // Assert
        assertTrue(cache.getMovies().isEmpty())
    }
}