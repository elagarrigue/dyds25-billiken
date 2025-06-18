package domain.usecase.detail

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MovieRepository
import edu.dyds.movies.domain.usecase.detail.GetMovieDetailsUseCase
import edu.dyds.movies.domain.usecase.detail.GetMovieDetailsUseCaseImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GetMovieDetailsUseCaseTest {
    private lateinit var movieRepository: MovieRepository
    private lateinit var useCase: GetMovieDetailsUseCase
    private val fakeMovie =
        Movie(
            id = 1,
            title = "Movie 1",
            overview = "Some overview",
            releaseDate = "2023-01-01",
            poster = "poster.jpg",
            backdrop = "backdrop.jpg",
            originalTitle = "Original Movie 1",
            originalLanguage = "en",
            popularity = 9.8,
            voteAverage = 8.7
        )

    @BeforeTest
    fun setUp() {
        movieRepository = mockk()
        useCase = GetMovieDetailsUseCaseImpl(movieRepository)
    }

    @Test
    fun `getMovieDetail should return movie when repository returns movie`() = runTest {
        // Arrange
        coEvery { movieRepository.getMovieDetail(1) } returns fakeMovie

        // Act
        val result = useCase.getMovieDetail(1)

        // Assert
        assertEquals(fakeMovie, result)
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