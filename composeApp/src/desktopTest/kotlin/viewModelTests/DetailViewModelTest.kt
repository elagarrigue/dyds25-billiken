import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.usecase.detail.GetMovieDetailsUseCase
import edu.dyds.movies.presentation.detail.DetailViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {
    private lateinit var useCase: GetMovieDetailsUseCase
    private lateinit var viewModel: DetailViewModel
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
    private val collectedStates = mutableListOf<DetailViewModel.MovieDetailUiState>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
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
    fun `getMovieDetail emits loading`() = runTest {
        //Arrange
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val scope = CoroutineScope(testDispatcher)
        scope.launch {
            viewModel.movieDetailStateFlow
                .take(1)
                .collect { collectedStates.add(it) }
        }
        //Act
        viewModel.getMovieDetail(1)
        advanceUntilIdle()
        //Asser
        assertTrue(collectedStates.first().isLoading)
    }

    @Test
    fun `getMovieDetail emits loading then detail`() = runTest {
        //Arrange
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val scope = CoroutineScope(testDispatcher)
        scope.launch {
            viewModel.movieDetailStateFlow
                .take(2)
                .collect { collectedStates.add(it) }
        }
        //Act
        viewModel.getMovieDetail(1)
        advanceUntilIdle()
        //Assert
        assertFalse(collectedStates.last().isLoading)
        assertEquals(fakeMovie, collectedStates.last().movie)
    }

    @Test
    fun `getMovieDetail emits null`() = runTest {
        //Arrange
        coEvery { useCase.getMovieDetail(any()) } returns null
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val scope = CoroutineScope(testDispatcher)
        scope.launch {
            viewModel.movieDetailStateFlow
                .take(2)
                .collect { collectedStates.add(it) }
        }
        //Act
        viewModel.getMovieDetail(1)
        advanceUntilIdle()
        //Assert
        assertFalse(collectedStates.last().isLoading)
        assertNull(collectedStates.last().movie)
    }
}