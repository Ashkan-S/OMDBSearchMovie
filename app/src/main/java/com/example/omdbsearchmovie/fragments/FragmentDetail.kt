package com.example.omdbsearchmovie.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.omdbsearchmovie.AppDatabase
import com.example.omdbsearchmovie.models.MovieResult
import com.example.omdbsearchmovie.RetrofitInterfaceClass
import com.example.omdbsearchmovie.databinding.FragmentDetailBinding
import com.example.omdbsearchmovie.models.MovieRoom
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FragmentDetail : Fragment() {

    private val apikey = "ab906375"

    @Inject
    lateinit var db: AppDatabase

    @Inject
    lateinit var retrofitInterface: RetrofitInterfaceClass

    private lateinit var binding: FragmentDetailBinding
    private lateinit var favoriteMovie: MovieResult
    private lateinit var favoriteOfflineMovie: MovieRoom

    private val args by navArgs<FragmentDetailArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.isOffline == "N") {
            lifecycleScope.launch(Dispatchers.IO) {
                favoriteMovie = retrofitInterface.searchMovieByID(apikey, args.imdbID)
                val favoriteMovieImdbIDList = db.FavoriteMovieDAO().getFavoriteMovieImdbID()
                if (favoriteMovie.imdbID in favoriteMovieImdbIDList) {
                    launch(Dispatchers.Main) {
                        binding.btnRemoveFromFavorite.visibility = View.VISIBLE
                    }
                } else {
                    launch(Dispatchers.Main) { binding.btnAddToFavorite.visibility = View.VISIBLE }
                }
                launch(Dispatchers.Main) {
                    fillMovieDetail(favoriteMovie)
                }
            }
        } else if (args.isOffline == "Y") {
            binding.btnRemoveFromFavorite.visibility = View.VISIBLE
            lifecycleScope.launch(Dispatchers.IO) {
                favoriteOfflineMovie = db.FavoriteMovieDAO().getFavoriteMovie(args.imdbID)
                launch(Dispatchers.Main) {
                    fillMovieDetail(favoriteOfflineMovie)
                }
            }
        }

        binding.btnAddToFavorite.setOnClickListener {
            if (args.isOffline == "N")
                addToFavoriteMovie(favoriteMovie)
            else if (args.isOffline == "Y")
                addToFavoriteMovie(favoriteOfflineMovie)
        }

        binding.btnRemoveFromFavorite.setOnClickListener {
            if (args.isOffline == "N")
                removeFromFavoriteMovie(favoriteMovie)
            else if (args.isOffline == "Y")
                removeFromFavoriteMovie(favoriteOfflineMovie)
        }
    }

    private fun fillMovieDetail(movie: MovieResult) {
        val yearAndGenreAndRuntime =
            movie.Year + " - " + movie.Genre + " - " + movie.Runtime
        val rate = movie.Metascore + "/100"
        Picasso.get().load(movie.Poster).into(binding.imgPosterView)
        binding.txtFullMovieName.text = movie.Title
        binding.txtFullMoviePlot.text = movie.Plot
        binding.txtYearTypeTime.text = yearAndGenreAndRuntime
        binding.txtRatingScore.text = rate
    }

    private fun fillMovieDetail(movie: MovieRoom) {
        val yearAndGenreAndRuntime =
            movie.Year + " - " + movie.Genre + " - " + movie.Runtime
        val rate = movie.Metascore + "/100"
        Picasso.get().load(movie.Poster).into(binding.imgPosterView)
        binding.txtFullMovieName.text = movie.Title
        binding.txtFullMoviePlot.text = movie.Plot
        binding.txtYearTypeTime.text = yearAndGenreAndRuntime
        binding.txtRatingScore.text = rate
    }

    private fun addToFavoriteMovie(movie: MovieResult) {
        val favoriteMovie =
            MovieRoom(
                movie.imdbID,
                movie.Year,
                movie.Type,
                movie.Genre,
                movie.Runtime,
                movie.Metascore,
                movie.Plot,
                movie.Poster,
                movie.Title
            )
        lifecycleScope.launch(Dispatchers.IO) {
            db.FavoriteMovieDAO().insertMovie(favoriteMovie)
            launch(Dispatchers.Main) {
                binding.btnRemoveFromFavorite.visibility = View.VISIBLE
                binding.btnAddToFavorite.visibility = View.INVISIBLE
            }
        }
    }

    private fun addToFavoriteMovie(movie: MovieRoom) {
        lifecycleScope.launch(Dispatchers.IO) {
            db.FavoriteMovieDAO().insertMovie(movie)
            launch(Dispatchers.Main) {
                binding.btnRemoveFromFavorite.visibility = View.VISIBLE
                binding.btnAddToFavorite.visibility = View.INVISIBLE
            }
        }
    }

    private fun removeFromFavoriteMovie(movie: MovieResult) {
        lifecycleScope.launch(Dispatchers.IO) {
            db.FavoriteMovieDAO().removeFavoriteMovie(movie.imdbID)
            launch(Dispatchers.Main) {
                binding.btnRemoveFromFavorite.visibility = View.INVISIBLE
                binding.btnAddToFavorite.visibility = View.VISIBLE
            }
        }
    }

    private fun removeFromFavoriteMovie(movie: MovieRoom) {
        lifecycleScope.launch(Dispatchers.IO) {
            db.FavoriteMovieDAO().removeFavoriteMovie(movie.imdbID)
            launch(Dispatchers.Main) {
                binding.btnRemoveFromFavorite.visibility = View.INVISIBLE
                binding.btnAddToFavorite.visibility = View.VISIBLE
            }
        }
    }
}