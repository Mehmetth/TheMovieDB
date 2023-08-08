package com.mehmetpetek.themoviedb.data.remote

import com.mehmetpetek.themoviedb.data.remote.model.MovieDetailResponse
import com.mehmetpetek.themoviedb.data.remote.model.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDBService {
    @GET("discover/movie")
    suspend fun getDiscoverMovies(
        @Query("page") pageNumber: Int,
        @Query("sort_by") sortBy: String
    ): Response<MovieResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int
    ): Response<MovieDetailResponse>
}