package presentation.detail

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.usecase.detail.GetMovieDetailsUseCase
import edu.dyds.movies.presentation.detail.DetailViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {
    private lateinit var useCase: GetMovieDetailsUseCase
    private lateinit var viewModel: DetailViewModel
    private val fakeMovie: Movie = mockk()
    private val collectedStates = mutableListOf<DetailViewModel.MovieDetailUiState>()
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        useCase = mockk()
        viewModel = DetailViewModel(useCase)
        coEvery { useCase.getMovieDetail(any()) } returns fakeMovie
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getMovieDetail emits loading then detailMovie`() = runTest {
        //Arrange
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val scope = CoroutineScope(testDispatcher)
        scope.launch {
            viewModel.movieDetailStateFlow
                .collect { collectedStates.add(it) }
        }

        //Act
        viewModel.getMovieDetail(1)
        advanceUntilIdle()

        //Assert
        TestCase.assertTrue(collectedStates.first().isLoading)
        assertNull(collectedStates.first().movie)
        TestCase.assertFalse(collectedStates.last().isLoading)
        assertEquals(fakeMovie, collectedStates.last().movie)
    }

    @Test
    fun `getMovieDetail emits loading then null`() = runTest {
        //Arrange
        coEvery { useCase.getMovieDetail(any()) } returns null
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val scope = CoroutineScope(testDispatcher)
        scope.launch {
            viewModel.movieDetailStateFlow
                .collect { collectedStates.add(it) }
        }

        //Act
        viewModel.getMovieDetail(1)
        advanceUntilIdle()

        //Assert
        TestCase.assertTrue(collectedStates.first().isLoading)
        assertNull(collectedStates.first().movie)
        TestCase.assertFalse(collectedStates.last().isLoading)
        assertNull(collectedStates.last().movie)
    }
}