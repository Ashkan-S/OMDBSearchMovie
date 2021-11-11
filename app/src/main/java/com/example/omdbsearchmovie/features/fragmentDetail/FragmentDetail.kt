package com.example.omdbsearchmovie.features.fragmentDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.omdbsearchmovie.models.MovieResult
import com.example.omdbsearchmovie.databinding.FragmentDetailBinding
import com.example.omdbsearchmovie.models.MovieRoom
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentDetail : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel by viewModels<FragmentDetailViewModel>()

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
            viewModel.searchMovieByID(args.imdbID)
            viewModel.getFavoriteMovieImdbID()
            viewModel.liveDataForSearchFavoriteMovie.observe(viewLifecycleOwner) {
                favoriteMovie = it
                fillMovieDetail(it)
            }
        } else if (args.isOffline == "Y") {
            viewModel.getFavoriteMovie(args.imdbID)
            binding.btnRemoveFromFavorite.visibility = View.VISIBLE
            viewModel.liveDataForGetFavoriteMovie.observe(viewLifecycleOwner) {
                favoriteOfflineMovie = it
                fillMovieDetail(it)
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

        viewModel.liveDataForFavoriteMovieImdbIDList.observe(viewLifecycleOwner) {
            if (favoriteMovie.imdbID in it) {
                binding.btnRemoveFromFavorite.visibility = View.VISIBLE
            } else {
                binding.btnAddToFavorite.visibility = View.VISIBLE
            }
        }
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
        viewModel.insertMovie(favoriteMovie)
        binding.btnRemoveFromFavorite.visibility = View.VISIBLE
        binding.btnAddToFavorite.visibility = View.INVISIBLE
    }

    private fun addToFavoriteMovie(movie: MovieRoom) {
        viewModel.insertMovie(movie)
        binding.btnRemoveFromFavorite.visibility = View.VISIBLE
        binding.btnAddToFavorite.visibility = View.INVISIBLE
    }

    private fun removeFromFavoriteMovie(movie: MovieResult) {
        viewModel.removeFavoriteMovie(movie.imdbID)
        binding.btnRemoveFromFavorite.visibility = View.INVISIBLE
        binding.btnAddToFavorite.visibility = View.VISIBLE
    }

    private fun removeFromFavoriteMovie(movie: MovieRoom) {
        viewModel.removeFavoriteMovie(movie.imdbID)
        binding.btnRemoveFromFavorite.visibility = View.INVISIBLE
        binding.btnAddToFavorite.visibility = View.VISIBLE
    }
}