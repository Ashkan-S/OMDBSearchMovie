package com.example.omdbsearchmovie

import com.example.omdbsearchmovie.models.MovieListResult
import com.example.omdbsearchmovie.models.MovieResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitInterfaceClass {
    @GET("/")
    fun searchMovieByTitle(
        @Query("apikey") apikey: String,
        @Query("s") name: String
    ): Call<MovieListResult>

    @GET("/")
    fun searchMovieByID(
        @Query("apikey") apikey: String,
        @Query("i") name: String
    ):Call<MovieResult>
}