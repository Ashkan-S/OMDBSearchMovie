package com.example.omdbsearchmovie.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class FragmentDetail : Fragment() {

    private val apikey = "ab906375"

    @Inject
    lateinit var db: AppDatabase

    @Inject
    lateinit var retrofitInterface: RetrofitInterfaceClass

    private lateinit var binding: FragmentDetailBinding
    private lateinit var favoriteMovie: Response<MovieResult>

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

        retrofitInterface.searchMovieByID(apikey, args.imdbID)
            .enqueue(object : Callback<MovieResult> {
                override fun onResponse(
                    call: Call<MovieResult>,
                    response: Response<MovieResult>
                ) {
                    fillMovieDetail(response)
                    favoriteMovie = response
                }

                override fun onFailure(call: Call<MovieResult>, t: Throwable) {
                    Log.d("TAG", "onFailure: ${t.message}")
                }
            })

        binding.btnAddToFavorite.setOnClickListener {
            addToFavoriteMovie(favoriteMovie)
        }
    }

    private fun fillMovieDetail(movie: Response<MovieResult>) {
        val yearAndGenreAndRuntime =
            movie.body()!!.Year + " - " + movie.body()!!.Genre + " - " + movie.body()!!.Runtime
        val rate = movie.body()!!.Metascore + "/100"
        Picasso.get().load(movie.body()!!.Poster).into(binding.imgPosterView)
        binding.txtFullMovieName.text = movie.body()!!.Title
        binding.txtFullMoviePlot.text = movie.body()!!.Plot
        binding.txtYearTypeTime.text = yearAndGenreAndRuntime
        binding.txtRatingScore.text = rate
    }

    private fun addToFavoriteMovie(movie: Response<MovieResult>) {
        val favoriteMovie =
            MovieRoom(
                movie.body()!!.imdbID,
                movie.body()!!.Year,
                movie.body()!!.Type,
                movie.body()!!.Genre,
                movie.body()!!.Runtime,
                movie.body()!!.Metascore,
                movie.body()!!.Plot,
                movie.body()!!.Poster,
                movie.body()!!.Title
            )
        lifecycleScope.launch(Dispatchers.IO) { db.FavoriteMovieDAO().insertMovie(favoriteMovie) }
    }
}