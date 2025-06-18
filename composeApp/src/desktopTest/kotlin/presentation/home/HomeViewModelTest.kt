package presentation.home

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.usecase.home.GetPopularMoviesUseCase
import edu.dyds.movies.presentation.home.HomeViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
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

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        useCase = mockk()
        viewModel = HomeViewModel(useCase)
        mockMovies = listOf(fakeQualifiedMovie1, fakeQualifiedMovie2)
        coEvery { useCase.getAllMovies() } returns mockMovies
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getAllMovies emits loading then movies`() = runTest {
        //Arrange
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val scope = CoroutineScope(testDispatcher)
        scope.launch {
            viewModel.moviesStateFlow
                .collect { collectedStates.add(it) }
        }
        //Act
        viewModel.getAllMovies()
        advanceUntilIdle()
        //Assert
        assertTrue(collectedStates.first().isLoading)
        assertEquals(emptyList<QualifiedMovie>(),collectedStates.first().movies)
        assertFalse(collectedStates.last().isLoading)
        assertEquals(mockMovies, collectedStates.last().movies)
    }
}