package com.mehmetpetek.themoviedb.domain.repository

import com.mehmetpetek.themoviedb.data.remote.model.MovieDetailResponse
import com.mehmetpetek.themoviedb.data.remote.model.MovieResponse
import com.mehmetpetek.themoviedb.data.remote.model.Resource
import kotlinx.coroutines.flow.Flow

interface TheMovieDBRepository {
    fun getDiscoverMovies(page: Int, sortBy: String): Flow<Resource<MovieResponse>>
    fun getMovieDetail(movieId: Int): Flow<Resource<MovieDetailResponse>>
}