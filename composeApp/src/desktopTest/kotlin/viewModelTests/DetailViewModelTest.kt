package viewModelTests

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.usecase.detail.GetMovieDetailsUseCase
import edu.dyds.movies.presentation.detail.DetailViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

class DetailViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    val testScope = CoroutineScope(UnconfinedTestDispatcher())
    private lateinit var useCase: GetMovieDetailsUseCase
    private lateinit var viewModel: DetailViewModel

    @Before
    fun setUp() {
        useCase = mockk()
        viewModel = DetailViewModel(useCase)
    }
    @Test
    fun `getMovieDetail emits loading then movie`() = runTest {
        //Arrange
        val fakeMovie =
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
        coEvery { useCase.getMovieDetail(any()) } returns fakeMovie

        //Act
        val collectedStates = mutableListOf<DetailViewModel.MovieDetailUiState>()

        val job = launch {
            viewModel.movieDetailStateFlow.collect {
                collectedStates.add(it)
                if (collectedStates.size == 2) cancel()
            }
        }

        viewModel.getMovieDetail(1)
        job.join()

        // Assert
        TestCase.assertTrue(collectedStates.first().isLoading)
        TestCase.assertFalse(collectedStates.last().isLoading)
        assertEquals(fakeMovie, collectedStates.last().movie)
    }

    @Test
    fun `getMovieDetail emits loading then null`() = runTest {
        //Arrange
        coEvery { useCase.getMovieDetail(any()) } returns null

        //Act
        val collectedStates = mutableListOf<DetailViewModel.MovieDetailUiState>()
        val job = launch {
            viewModel.movieDetailStateFlow.collect {
                collectedStates.add(it)
                if (collectedStates.size == 2) cancel()
            }
        }
        viewModel.getMovieDetail(1)
        job.join()

        //Assert
        TestCase.assertTrue(collectedStates.first().isLoading)
        TestCase.assertFalse(collectedStates.last().isLoading)
        assertEquals(null, collectedStates.last().movie)

    }
}