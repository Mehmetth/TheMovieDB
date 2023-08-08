package com.mehmetpetek.themoviedb.domain.usecase

import com.mehmetpetek.themoviedb.R
import com.mehmetpetek.themoviedb.data.remote.model.MovieResponse
import com.mehmetpetek.themoviedb.data.remote.model.Resource
import com.mehmetpetek.themoviedb.domain.repository.TheMovieDBRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class AllMoviesUseCase @Inject constructor(
    private val theMovieDBRepository: TheMovieDBRepository
) {
    operator fun invoke(
        popularMoviesPage: Int,
        topRatedMoviesPage: Int,
        upcomingMoviesPage: Int,
        nowPlayingMoviesPage: Int
    ): Flow<AllMoviesState> = callbackFlow {

        val hasmap: HashMap<MovieType, MovieResponse?> = hashMapOf()

        val getPopularMovies = theMovieDBRepository.getPopularMovies(popularMoviesPage)
        val getTopRatedMovies = theMovieDBRepository.getTopRatedMovies(topRatedMoviesPage)
        val getUpcomingMovies = theMovieDBRepository.getUpcomingMovies(upcomingMoviesPage)
        val getNowPlayingMovies = theMovieDBRepository.getNowPlayingMovies(nowPlayingMoviesPage)

        combine(
            getPopularMovies,
            getTopRatedMovies,
            getUpcomingMovies,
            getNowPlayingMovies
        ) { popularMovies, topRatedMovies, upcomingMovies, nowPlayingMovies ->

            popularMovies.data?.let {
                if (it.results.isNotEmpty()){
                    hasmap[MovieType.POPULAR] = popularMovies.data
                }
                else{
                    AllMoviesState.NotData
                }
            } ?: kotlin.run {
                AllMoviesState.Error(popularMovies.message)
            }

            topRatedMovies.data?.let {
                if (it.results.isNotEmpty()){
                    hasmap[MovieType.TOP_RATED] = topRatedMovies.data
                }
                else{
                    AllMoviesState.NotData
                }
            } ?: kotlin.run {
                AllMoviesState.Error(topRatedMovies.message)
            }

            upcomingMovies.data?.let {
                if (it.results.isNotEmpty()){
                    hasmap[MovieType.UP_COMING] = upcomingMovies.data
                }
                else{
                    AllMoviesState.NotData
                }
            } ?: kotlin.run {
                AllMoviesState.Error(upcomingMovies.message)
            }

            nowPlayingMovies.data?.let {
                if (it.results.isNotEmpty()){
                    hasmap[MovieType.NOW_PLAYING] = nowPlayingMovies.data
                }
                else{
                    AllMoviesState.NotData
                }
            } ?: kotlin.run {
                AllMoviesState.Error(nowPlayingMovies.message)
            }

            if (hasmap.values.isNotEmpty()) {
                AllMoviesState.Success(hasmap)
            } else {
                AllMoviesState.Error(Throwable())
            }

        }.collect {
            trySend(it)
        }
        awaitClose { cancel() }
    }

    sealed interface AllMoviesState {
        data class Success(val allMovies: HashMap<MovieType, MovieResponse?>) : AllMoviesState
        data class Error(val throwable: Throwable?) : AllMoviesState
        object NotData : AllMoviesState
    }

    enum class MovieType(val movieType: Int){
        POPULAR(R.string.popular_movies),
        TOP_RATED(R.string.top_rated_movies),
        UP_COMING(R.string.up_coming_movies),
        NOW_PLAYING(R.string.now_playing_movies)
    }
}