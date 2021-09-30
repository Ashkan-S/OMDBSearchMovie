package com.example.omdbsearchmovie.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.example.omdbsearchmovie.R
import android.content.Context
import android.util.Log
import android.widget.*
import com.example.omdbsearchmovie.MovieResult
import com.example.omdbsearchmovie.RetrofitInterfaceClass
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class FragmentDetail : Fragment() {

    private val apikey = "ab906375"
    private val baseUrl = "https://www.omdbapi.com/"

    private lateinit var txtFullMovieName: TextView
    private lateinit var txtYearTypeTime: TextView
    private lateinit var txtFullMoviePlot: TextView
    private lateinit var txtmovieRating: TextView
    private lateinit var imgPosterView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_detail, container, false)

        txtFullMovieName = view.findViewById(R.id.txtFullMovieName)
        txtYearTypeTime = view.findViewById(R.id.txtYearTypeTime)
        txtFullMoviePlot = view.findViewById(R.id.txtFullMoviePlot)
        txtmovieRating = view.findViewById(R.id.txtRatingScore)
        imgPosterView = view.findViewById(R.id.imgPosterView)

        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val imdbIDSelectedMovie = sharedPref.getString("imdbID", 0.toString())

        val backToSearch: Button = view.findViewById(R.id.btnBack)
        backToSearch.setOnClickListener { startFragmentSearch() }

        val retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create()).build()

        val retrofitInterface = retrofit.create(RetrofitInterfaceClass::class.java)

        retrofitInterface.searchMovieByID(apikey, imdbIDSelectedMovie.toString())
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
        return view
    }

    private fun startFragmentSearch() {
        val frgSearch = FragmentSearch()
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        ft.replace(R.id.fragmentView, frgSearch)
        ft.commit()
    }

    private fun fillMovieDetail(movie: Response<MovieResult>) {
        Picasso.get().load(movie.body()!!.Poster).into(imgPosterView)
        txtFullMovieName.text = movie.body()!!.Title
        txtYearTypeTime.text =
            movie.body()!!.Year + " " + movie.body()!!.Genre + " " + movie.body()!!.Runtime
        txtFullMoviePlot.text = movie.body()!!.Plot
        txtmovieRating.text = movie.body()!!.Metascore + "/100"
    }
}