package com.mehmetpetek.themoviedb.domain.usecase

import com.mehmetpetek.themoviedb.data.remote.model.MovieDetailResponse
import com.mehmetpetek.themoviedb.data.remote.model.Resource
import com.mehmetpetek.themoviedb.domain.repository.TheMovieDBRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class MovieDetailUseCase @Inject constructor(
    private val theMovieDBRepository: TheMovieDBRepository
) {
    operator fun invoke(movieId: Int): Flow<MovieDetailState> = callbackFlow {
        theMovieDBRepository.getMovieDetail(movieId).collect { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        trySend(
                            MovieDetailState.Success(result.data)
                        )
                    } ?: kotlin.run {
                        MovieDetailState.NotData
                    }
                }

                is Resource.Error,
                is Resource.Fail -> {
                    trySend(MovieDetailState.Error(result.message))
                }
            }
        }
        awaitClose { cancel() }
    }

    sealed interface MovieDetailState {
        data class Success(val movieDetailResponse: MovieDetailResponse) : MovieDetailState
        data class Error(val throwable: Throwable?) : MovieDetailState
        object NotData : MovieDetailState
    }
}