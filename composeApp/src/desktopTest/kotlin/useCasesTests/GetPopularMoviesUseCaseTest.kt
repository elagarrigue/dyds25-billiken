package useCasesTests

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.repository.MovieRepository
import edu.dyds.movies.domain.usecase.home.GetPopularMoviesUseCase
import edu.dyds.movies.domain.usecase.home.GetPopularMoviesUseCaseImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

class GetPopularMoviesUseCaseTest {
    private lateinit var movieRepository: MovieRepository
    private lateinit var useCase : GetPopularMoviesUseCase

    @Before
    fun setUp() {
        movieRepository = mockk()
        useCase = GetPopularMoviesUseCaseImpl(movieRepository)
    }

    @Test
    fun `getAllMovies should return movies sorted by rating`() = runTest {
        //Arrange
        val fakeMovies = listOf(
            Movie(
                id = 1,
                title = "Bad Movie",
                overview = "Some overview",
                releaseDate = "2023-01-01",
                poster = "poster.jpg",
                backdrop = "backdrop.jpg",
                originalTitle = "Original Movie 1",
                originalLanguage = "en",
                popularity = 3.2,
                voteAverage = 3.3
            ),
            Movie(
                id = 2,
                title = "Regular Movie",
                overview = "Some overview 2",
                releaseDate = "2023-02-02",
                poster = "poster.jpg",
                backdrop = "backdrop.jpg",
                originalTitle = "Original Movie 2",
                originalLanguage = "es",
                popularity = 5.2,
                voteAverage = 5.7
            ),
            Movie(
                id = 3,
                title = "Good Movie",
                overview = "Some overview 3",
                releaseDate = "2023-03-03",
                poster = "poster.jpg",
                backdrop = "backdrop.jpg",
                originalTitle = "Original Movie 3",
                originalLanguage = "fr",
                popularity = 8.5,
                voteAverage = 9.0
            )
        )
        coEvery { movieRepository.getPopularMovies() } returns fakeMovies

        //Act
        val result: List<QualifiedMovie> = useCase.getAllMovies()

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
        val result: List<QualifiedMovie> = useCase.getAllMovies()

        //Assert
        assertEquals(0, result.size)
    }

    @Test
    fun `getAllMovies should keep order for movies with same rating`() = runTest {
        // Arrange
        val fakeMovies = listOf(
            Movie(
                id = 1,
                title = "Movie1",
                overview = "Some overview",
                releaseDate = "2023-01-01",
                poster = "poster.jpg",
                backdrop = "backdrop.jpg",
                originalTitle = "Original Movie 1",
                originalLanguage = "en",
                popularity = 5.0,
                voteAverage = 5.0
            ),
            Movie(
                id = 2,
                title = "Movie2",
                overview = "Some overview 2",
                releaseDate = "2023-02-02",
                poster = "poster.jpg",
                backdrop = "backdrop.jpg",
                originalTitle = "Original Movie 2",
                originalLanguage = "es",
                popularity = 5.0,
                voteAverage = 5.0
            ),
            Movie(
                id = 3,
                title = "Movie3",
                overview = "Some overview 3",
                releaseDate = "2023-03-03",
                poster = "poster.jpg",
                backdrop = "backdrop.jpg",
                originalTitle = "Original Movie 3",
                originalLanguage = "fr",
                popularity = 5.0,
                voteAverage = 5.0
            )
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