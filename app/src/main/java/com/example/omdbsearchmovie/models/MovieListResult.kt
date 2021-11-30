package com.example.omdbsearchmovie.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieListResult(
    val Response: String,
    val Search: List<Search>?,
    val totalResults: String?
)

@JsonClass(generateAdapter = true)
data class Search(
    val Poster: String,
    val Title: String,
    val Type: String,
    val Year: String,
    val imdbID: String
)