package com.example.omdbsearchmovie.features.fragmentDetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omdbsearchmovie.models.MovieResult
import com.example.omdbsearchmovie.models.MovieRoom
import com.example.omdbsearchmovie.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentDetailViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {
    val liveDataForSearchFavoriteMovie = MutableLiveData<MovieResult>()
    val liveDataForFavoriteMovieImdbIDList = MutableLiveData<List<String>>()
    val liveDataForGetFavoriteMovie = MutableLiveData<MovieRoom>()

    fun searchMovieByID(imdbID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            liveDataForSearchFavoriteMovie.postValue(repository.searchMovieByID(imdbID))
        }
    }

    fun getFavoriteMovieImdbID() {
        viewModelScope.launch(Dispatchers.IO) {
            liveDataForFavoriteMovieImdbIDList.postValue(repository.getFavoriteMovieImdbID())
        }
    }

    fun getFavoriteMovie(imdbID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            liveDataForGetFavoriteMovie.postValue(
                repository.getFavoriteMovie(
                    imdbID
                )
            )
        }

    }

    fun insertMovie(favoriteMovie: MovieRoom) {
        viewModelScope.launch(Dispatchers.IO) { repository.insertMovie(favoriteMovie) }
    }

    fun removeFavoriteMovie(imdbID: String) {
        viewModelScope.launch(Dispatchers.IO) { repository.removeFavoriteMovie(imdbID) }
    }

}