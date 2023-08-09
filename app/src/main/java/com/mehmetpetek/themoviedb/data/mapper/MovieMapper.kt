package com.mehmetpetek.themoviedb.data.mapper

import com.mehmetpetek.themoviedb.data.local.model.MovieEntity
import com.mehmetpetek.themoviedb.data.remote.model.Result

fun List<MovieEntity>.toMovieEntities(): List<Result> {
    return map {
        Result(
            backdrop_path = it.movieBackdropUrl.orEmpty(),
            id = it.id,
            poster_path = it.moviePosterUrl.orEmpty(),
            release_date = it.releaseDate,
            title = it.title,
            vote_average = it.rating
        )
    }
}

fun List<Result>.toEntitiesMovie(page: Int, sortBy: String): List<MovieEntity> {
    return map {
        MovieEntity(
            id = it.id,
            title = it.title,
            moviePosterUrl = it.poster_path,
            movieBackdropUrl = it.backdrop_path,
            overview = "",
            rating = it.vote_average,
            releaseDate = it.release_date,
            sortBy = sortBy,
            page = page
        )
    }
}