package viewModelTests

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.usecase.home.GetPopularMoviesUseCase
import edu.dyds.movies.presentation.home.HomeViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HomeViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    val testScope = CoroutineScope(UnconfinedTestDispatcher())
    private lateinit var useCase: GetPopularMoviesUseCase
    private lateinit var viewModel: HomeViewModel

    private val fakeQualifiedMovie1 =
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
    private val fakeQualifiedMovie2 =
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
    private lateinit var mockMovies: List<QualifiedMovie>
    private val collectedStates = mutableListOf<HomeViewModel.MoviesUiState>()

    @Before
    fun setUp() {
        useCase = mockk()
        viewModel = HomeViewModel(useCase)
        mockMovies = listOf(fakeQualifiedMovie1, fakeQualifiedMovie2)
        coEvery { useCase.getAllMovies() } returns mockMovies
        testScope.launch {
            viewModel.moviesStateFlow.collect { collectedStates.add(it) }
        }
        viewModel.getAllMovies()
    }

    @Test
    fun `getAllMovies emits loading`() = runTest {
        assertTrue(collectedStates.first().isLoading)
    }

    @Test
    fun `getAllMovies emits movies`() = runTest {
        assertFalse(collectedStates.last().isLoading)
        assertEquals(mockMovies, collectedStates.last().movies)
    }
}