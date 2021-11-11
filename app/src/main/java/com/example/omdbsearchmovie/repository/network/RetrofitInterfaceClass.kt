package com.example.omdbsearchmovie.repository.network

import com.example.omdbsearchmovie.models.MovieListResult
import com.example.omdbsearchmovie.models.MovieResult
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitInterfaceClass {
    @GET("/")
    suspend fun searchMovieByTitle(
        @Query("apikey") apikey: String,
        @Query("s") name: String
    ): MovieListResult

    @GET("/")
    suspend fun searchMovieByID(
        @Query("apikey") apikey: String,
        @Query("i") name: String
    ): MovieResult
}