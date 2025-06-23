package domain.usecase.detail

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MovieRepository
import edu.dyds.movies.domain.usecase.detail.GetMovieDetailsUseCase
import edu.dyds.movies.domain.usecase.detail.GetMovieDetailsUseCaseImpl
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GetMovieDetailsUseCaseTest {
    private lateinit var movieRepository: MovieRepository
    private lateinit var useCase: GetMovieDetailsUseCase

    @BeforeTest
    fun setUp() {
        movieRepository = mockk()
        useCase = GetMovieDetailsUseCaseImpl(movieRepository)
    }

    @Test
    fun `getMovieDetail should return movie when repository returns movie`() = runTest {
        // Arrange
        val fakeMovie = mockk<Movie>{
            every { id } returns 1
        }
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