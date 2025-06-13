package viewModelTests

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.usecase.home.GetPopularMoviesUseCase
import edu.dyds.movies.presentation.home.HomeViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

class HomeViewModelTest {
    private lateinit var useCase: GetPopularMoviesUseCase
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        useCase = mockk()
        viewModel = HomeViewModel(useCase)
    }

    @Test
    fun `getAllMovies emits loading then movies`() = runTest {
        //Arrange
        val fakeQualifiedMovie1 =
            QualifiedMovie(
                movie = Movie(
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
                ), true
            )
        val fakeQualifiedMovie2 =
            QualifiedMovie(
                movie = Movie(
                    id = 2,
                    title = "Movie 2",
                    overview = "Some overview 2",
                    releaseDate = "2023-02-02",
                    poster = "poster.jpg",
                    backdrop = "backdrop.jpg",
                    originalTitle = "Original Movie 2",
                    originalLanguage = "es",
                    popularity = 4.8,
                    voteAverage = 5.7
                ), false
            )

        val mockMovies = listOf(fakeQualifiedMovie1, fakeQualifiedMovie2)
        coEvery { useCase.getAllMovies() } returns mockMovies

        // Act
        viewModel.getAllMovies()
        val collectedStates = viewModel.moviesStateFlow.take(2).toList()

        // Assert
        TestCase.assertTrue(collectedStates.first().isLoading)
        TestCase.assertFalse(collectedStates.last().isLoading)
        assertEquals(mockMovies, collectedStates.last().movies)
    }
}