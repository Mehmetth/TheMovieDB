package com.mehmetpetek.themoviedb.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mehmetpetek.themoviedb.data.local.model.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDBDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(movies: List<MovieEntity>)

    @Query("Select * from movie where page=:page and sortBy=:sortBy")
    fun getMovies(page: Int, sortBy: String): Flow<List<MovieEntity>>
}