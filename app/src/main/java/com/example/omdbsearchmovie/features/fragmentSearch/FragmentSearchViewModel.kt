package com.example.omdbsearchmovie.features.fragmentSearch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omdbsearchmovie.models.MovieListResult
import com.example.omdbsearchmovie.models.MovieRoom
import com.example.omdbsearchmovie.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentSearchViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {
    val liveDataForMovieListResult = MutableLiveData<MovieListResult>()
    val liveDataForFavoriteMovieList = MutableLiveData<List<MovieRoom>>()

    fun onSearchClicked(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.searchMovieByTitle(title)
            liveDataForMovieListResult.postValue(result)
        }
    }

    fun onFavoriteClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getFavoriteMovieList()
            liveDataForFavoriteMovieList.postValue(result)
        }
    }
}