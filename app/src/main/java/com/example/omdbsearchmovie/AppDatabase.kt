package com.example.omdbsearchmovie

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.omdbsearchmovie.repository.local.FavoriteMovieDAO
import com.example.omdbsearchmovie.models.MovieRoom

@Database(entities = [MovieRoom::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun FavoriteMovieDAO(): FavoriteMovieDAO
}