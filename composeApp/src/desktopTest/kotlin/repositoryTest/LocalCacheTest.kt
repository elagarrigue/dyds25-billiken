package repositoryTest

import edu.dyds.movies.data.local.LocalCache
import edu.dyds.movies.domain.entity.Movie
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LocalCacheTest {

    private lateinit var cache: LocalCache

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
        // Arrange
        val movie = Movie(1, "title", "overview", "2024-01-01", "poster", "backdrop", "originalTitle", "en", 1.0, 2.0)
        // Act
        cache.addMovies(listOf(movie))
        // Assert
        assertEquals(listOf(movie), cache.getMovies())
    }

    @Test
    fun `hasMovies returns true when cache is not empty`() {
        // Arrange
        val movie = Movie(1, "title", "overview", "2024-01-01", "poster", "backdrop", "originalTitle", "en", 1.0, 2.0)
        // Act
        cache.addMovies(listOf(movie))
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
        val movie = Movie(1, "title", "overview", "2024-01-01", "poster", "backdrop", "originalTitle", "en", 1.0, 2.0)
        cache.addMovies(listOf(movie))
        // Act
        cache.clear()
        // Assert
        assertTrue(cache.getMovies().isEmpty())
    }
}