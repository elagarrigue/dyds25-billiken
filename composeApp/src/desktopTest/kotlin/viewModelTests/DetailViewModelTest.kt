import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.usecase.detail.GetMovieDetailsUseCase
import edu.dyds.movies.presentation.detail.DetailViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DetailViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    val testScope = CoroutineScope(UnconfinedTestDispatcher())
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

    @Before
    fun setUp() {
        useCase = mockk()
        viewModel = DetailViewModel(useCase)
        coEvery { useCase.getMovieDetail(any()) } returns fakeMovie
        testScope.launch {
            viewModel.movieDetailStateFlow.collect { collectedStates.add(it) }
        }
        viewModel.getMovieDetail(1)
    }

    @Test
    fun `getMovieDetail emits loading`() = runTest {
        assertTrue(collectedStates.first().isLoading)
    }

    @Test
    fun `getMovieDetail emits movie detail`() = runTest {
        assertFalse(collectedStates.last().isLoading)
        assertEquals(fakeMovie, collectedStates.last().movie)
    }

    @Test
    fun `getMovieDetail emits null`() = runTest {
        //Arrange
        coEvery { useCase.getMovieDetail(any()) } returns null
        //Act
        viewModel.getMovieDetail(1)
        //Assert
        assertFalse(collectedStates.last().isLoading)
        assertNull(collectedStates.last().movie)
    }
}