package com.mehmetpetek.themoviedb.data.remote

import com.mehmetpetek.themoviedb.data.remote.model.MovieDetailResponse
import com.mehmetpetek.themoviedb.data.remote.model.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDBService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") pageNumber: Int
    ): Response<MovieResponse>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") pageNumber: Int
    ): Response<MovieResponse>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("page") pageNumber: Int
    ): Response<MovieResponse>

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") pageNumber: Int
    ): Response<MovieResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int
    ): Response<MovieDetailResponse>
}