package com.example.omdbsearchmovie.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import androidx.navigation.fragment.navArgs
import com.example.omdbsearchmovie.MovieResult
import com.example.omdbsearchmovie.RetrofitInterfaceClass
import com.example.omdbsearchmovie.databinding.FragmentDetailBinding
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class FragmentDetail : Fragment() {

    private val apikey = "ab906375"
    private val baseUrl = "https://www.omdbapi.com/"

    private lateinit var binding: FragmentDetailBinding

    private val args by navArgs<FragmentDetailArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDetailBinding.inflate(inflater, container, false)

        val retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create()).build()

        val retrofitInterface = retrofit.create(RetrofitInterfaceClass::class.java)

        retrofitInterface.searchMovieByID(apikey, args.imdbID)
            .enqueue(object : Callback<MovieResult> {
                override fun onResponse(
                    call: Call<MovieResult>,
                    response: Response<MovieResult>
                ) {
                    fillMovieDetail(response)
                }

                override fun onFailure(call: Call<MovieResult>, t: Throwable) {
                    Log.d("TAG", "onFailure: ${t.message}")
                }
            })
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun fillMovieDetail(movie: Response<MovieResult>) {
        val yearAndGenreAndRuntime =
            movie.body()!!.Year + " " + movie.body()!!.Genre + " " + movie.body()!!.Runtime
        val rate = movie.body()!!.MetaScore + "/100"
        Picasso.get().load(movie.body()!!.Poster).into(binding.imgPosterView)
        binding.txtFullMovieName.text = movie.body()!!.Title
        binding.txtFullMoviePlot.text = movie.body()!!.Plot
        binding.txtYearTypeTime.text = yearAndGenreAndRuntime
        binding.txtRatingScore.text = rate
    }
}