package com.mehmetpetek.themoviedb.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mehmetpetek.themoviedb.data.local.model.MovieEntity

@Database(entities = [MovieEntity::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDBDao(): MovieDBDao
}