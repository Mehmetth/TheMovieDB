package com.mehmetpetek.themoviedb.data.remote.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieResponse(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
) : Parcelable

@Parcelize
data class Result(
    val backdrop_path: String,
    val id: Int,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val vote_average: Double,
) : Parcelable