package com.mehmetpetek.themoviedb.data.repository

import com.mehmetpetek.themoviedb.data.remote.TheMovieDBDataSource
import com.mehmetpetek.themoviedb.data.remote.model.MovieDetailResponse
import com.mehmetpetek.themoviedb.data.remote.model.MovieImageResponse
import com.mehmetpetek.themoviedb.data.remote.model.MovieResponse
import com.mehmetpetek.themoviedb.data.remote.model.Resource
import com.mehmetpetek.themoviedb.domain.repository.TheMovieDBRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class TheMovieDBRepositoryImpl @Inject constructor(private val theMovieDBDataSource: TheMovieDBDataSource) :
    TheMovieDBRepository {
    override fun getDiscoverMovies(page: Int, sortBy: String): Flow<Resource<MovieResponse>> =
        callbackFlow {
            val response = theMovieDBDataSource.getDiscoverMovies(page, sortBy)
            if (response.isSuccessful) {
                response.body()?.let {
                    trySend(Resource.Success(it))
                } ?: kotlin.run {
                    trySend(Resource.Fail(null))
                }
            } else {
                trySend(Resource.Error(null))
            }
            awaitClose { cancel() }
        }

    override fun getMovieDetail(movieId: Int): Flow<Resource<MovieDetailResponse>> = callbackFlow {
        val response = theMovieDBDataSource.getMovieDetail(movieId)
        if (response.isSuccessful) {
            response.body()?.let {
                trySend(Resource.Success(it))
            } ?: kotlin.run {
                trySend(Resource.Fail(null))
            }
        } else {
            trySend(Resource.Error(null))
        }
        awaitClose { cancel() }
    }

    override fun getMovieImageDetail(movieId: Int): Flow<Resource<MovieImageResponse>> =
        callbackFlow {
            val response = theMovieDBDataSource.getMovieImageDetail(movieId)
            if (response.isSuccessful) {
                response.body()?.let {
                    trySend(Resource.Success(it))
                } ?: kotlin.run {
                    trySend(Resource.Fail(null))
                }
            } else {
                trySend(Resource.Error(null))
            }
            awaitClose { cancel() }
        }

}