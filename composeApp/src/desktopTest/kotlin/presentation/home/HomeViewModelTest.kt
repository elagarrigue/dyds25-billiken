package presentation.home

import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.usecase.home.GetPopularMoviesUseCase
import edu.dyds.movies.presentation.home.HomeViewModel
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertFalse
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    private lateinit var useCase: GetPopularMoviesUseCase
    private lateinit var viewModel: HomeViewModel
    private val fakeQualifiedMovie1: QualifiedMovie = mockk()
    private val fakeQualifiedMovie2: QualifiedMovie = mockk()
    private val mockMovies: List<QualifiedMovie> = listOf(fakeQualifiedMovie1, fakeQualifiedMovie2)
    private val collectedStates = mutableListOf<HomeViewModel.MoviesUiState>()
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        useCase = mockk()
        viewModel = HomeViewModel(useCase)
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
        assertEquals(emptyList(), collectedStates.first().movies)
        assertFalse(collectedStates.last().isLoading)
        assertEquals(mockMovies, collectedStates.last().movies)
    }
}