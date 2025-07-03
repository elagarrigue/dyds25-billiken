package domain.usecase.home

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MovieRepository
import edu.dyds.movies.domain.usecase.home.GetPopularMoviesUseCase
import edu.dyds.movies.domain.usecase.home.GetPopularMoviesUseCaseImpl
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetPopularMoviesUseCaseTest {
    private lateinit var movieRepository: MovieRepository
    private lateinit var useCase: GetPopularMoviesUseCase

    @BeforeTest
    fun setUp() {
        movieRepository = mockk()
        useCase = GetPopularMoviesUseCaseImpl(movieRepository)
    }

    @Test
    fun `getAllMovies should return movies sorted by rating`() = runTest {
        //Arrange
        val fakeMovies = listOf(
            mockk<Movie> {
                every { id } returns 1
                every { title } returns "Bad Movie"
                every { popularity } returns 2.0
                every { voteAverage } returns 2.3
            },
            mockk<Movie> {
                every { id } returns 2
                every { title } returns "Regular Movie"
                every { popularity } returns 5.0
                every { voteAverage } returns 5.3
            },
            mockk<Movie> {
                every { id } returns 3
                every { title } returns "Good Movie"
                every { popularity } returns 9.0
                every { voteAverage } returns 9.3
            }
        )
        coEvery { movieRepository.getPopularMovies() } returns fakeMovies

        //Act
        val result = useCase.getAllMovies()

        //Assert
        assertEquals("Good Movie", result[0].movie.title)
        assertEquals("Regular Movie", result[1].movie.title)
        assertEquals("Bad Movie", result[2].movie.title)
    }

    @Test
    fun `getAllMovies should return empty list when repository returns empty`() = runTest {
        //Arrange
        coEvery { movieRepository.getPopularMovies() } returns emptyList()

        //Act
        val result = useCase.getAllMovies()

        //Assert
        assertEquals(0, result.size)
    }

    @Test
    fun `getAllMovies should keep order for movies with same rating`() = runTest {
        // Arrange
        val fakeMovies = listOf(
            mockk<Movie> {
                every { title } returns "Movie1"
                every { popularity } returns 5.0
                every { voteAverage } returns 5.3
            },
            mockk<Movie> {
                every { title } returns "Movie2"
                every { popularity } returns 5.0
                every { voteAverage } returns 5.3
            },
            mockk<Movie> {
                every { title } returns "Movie3"
                every { popularity } returns 5.0
                every { voteAverage } returns 5.3
            }
        )
        coEvery { movieRepository.getPopularMovies() } returns fakeMovies

        // Act
        val result = useCase.getAllMovies()

        // Assert
        assertEquals("Movie1", result[0].movie.title)
        assertEquals("Movie2", result[1].movie.title)
        assertEquals("Movie3", result[2].movie.title)
    }
}