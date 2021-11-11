package com.example.omdbsearchmovie.repository

import com.example.omdbsearchmovie.AppDatabase
import com.example.omdbsearchmovie.repository.network.RetrofitInterfaceClass
import com.example.omdbsearchmovie.models.MovieListResult
import com.example.omdbsearchmovie.models.MovieResult
import com.example.omdbsearchmovie.models.MovieRoom
import javax.inject.Inject

const val API_KEY = "ab906375"

class Repository @Inject constructor(
    private val db: AppDatabase,
    private val network: RetrofitInterfaceClass
) {

    suspend fun searchMovieByTitle(title: String): MovieListResult {
        return network.searchMovieByTitle(API_KEY, title)
    }

    suspend fun getFavoriteMovieList(): List<MovieRoom> {
        return db.FavoriteMovieDAO().getFavoriteMovieList()
    }

    suspend fun searchMovieByID(imdbID: String): MovieResult {
        return network.searchMovieByID(API_KEY,imdbID)
    }

    suspend fun getFavoriteMovieImdbID(): List<String> {
        return db.FavoriteMovieDAO().getFavoriteMovieImdbID()
    }

    suspend fun getFavoriteMovie(imdbID: String): MovieRoom {
        return db.FavoriteMovieDAO().getFavoriteMovie(imdbID)
    }

    suspend fun insertMovie(favoriteMovie: MovieRoom) {
        db.FavoriteMovieDAO().insertMovie(favoriteMovie)
    }

    suspend fun removeFavoriteMovie(imdbID: String) {
        db.FavoriteMovieDAO().removeFavoriteMovie(imdbID)
    }
}