package edu.dyds.movies.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.usecase.detail.GetMovieDetailsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val useCase: GetMovieDetailsUseCase) : ViewModel() {
    private val movieDetailStateMutableStateFlow = MutableStateFlow(MovieDetailUiState())

    val movieDetailStateFlow: Flow<MovieDetailUiState> = movieDetailStateMutableStateFlow


    fun getMovieDetail(title: String) {
        viewModelScope.launch {
            movieDetailStateMutableStateFlow.emit(
                MovieDetailUiState(
                    isLoading = false,
                    movie = useCase.getMovieDetail(title)
                )
            )
        }
    }

    data class MovieDetailUiState(
        val isLoading: Boolean = true,
        val movie: Movie? = null,
    )
}