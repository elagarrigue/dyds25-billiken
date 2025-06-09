package edu.dyds.movies.data.external

interface MoviesExternalSource {
    suspend fun getPopularMovies(): RemoteResult
    suspend fun getMovieDetailsDB(id: Int): RemoteMovie
}