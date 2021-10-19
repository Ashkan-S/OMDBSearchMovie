package com.example.omdbsearchmovie.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.omdbsearchmovie.models.MovieRoom

@Dao
interface FavoriteMovieDAO {

    @Insert
    suspend fun insertMovie(movie: MovieRoom)

    @Query("SELECT * FROM MovieRoom")
    suspend fun getFavoriteMovie(): List<MovieRoom>
}