package com.example.omdbsearchmovie.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieRoom(
    @PrimaryKey val imdbID: String,
    val Year: String,
    val Type: String,
    var Genre: String,
    val Runtime: String,
    val Metascore: String,
    val Plot: String,
    val Poster: String,
    val Title: String
)