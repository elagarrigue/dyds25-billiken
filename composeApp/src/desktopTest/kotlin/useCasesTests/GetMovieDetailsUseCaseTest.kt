package useCasesTests

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MovieRepository
import edu.dyds.movies.domain.usecase.detail.GetMovieDetailsUseCase
import edu.dyds.movies.domain.usecase.detail.GetMovieDetailsUseCaseImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GetMovieDetailsUseCaseTest {
    private lateinit var movieRepository: MovieRepository
    private lateinit var useCase: GetMovieDetailsUseCase

    @Before
    fun setUp() {
        movieRepository = mockk()
        useCase = GetMovieDetailsUseCaseImpl(movieRepository)
    }

    @Test
    fun `getMovieDetail should return movie when repository returns movie`() = runTest {
        // Arrange
        val movie = Movie(
            id = 1,
            title = "Test Movie",
            overview = "Overview",
            releaseDate = "2024-01-01",
            poster = "poster.jpg",
            backdrop = "backdrop.jpg",
            originalTitle = "Original Title",
            originalLanguage = "en",
            popularity = 1.0,
            voteAverage = 2.0
        )
        coEvery { movieRepository.getMovieDetail(1) } returns movie

        // Act
        val result = useCase.getMovieDetail(1)

        // Assert
        assertEquals(movie, result)
    }

    @Test
    fun `getMovieDetail should return null when repository returns null`() = runTest {
        // Arrange
        coEvery { movieRepository.getMovieDetail(1) } returns null

        // Act
        val result = useCase.getMovieDetail(1)

        // Assert
        assertNull(result)
    }
}