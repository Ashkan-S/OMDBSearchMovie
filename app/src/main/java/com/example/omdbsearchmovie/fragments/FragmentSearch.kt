package com.example.omdbsearchmovie.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentTransaction
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.example.omdbsearchmovie.*
import com.example.omdbsearchmovie.databinding.FragmentSearchBinding

class FragmentSearch : Fragment() {

    private val apikey = "ab906375"
    private val baseUrl = "https://www.omdbapi.com/"

    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSearchBinding.inflate(inflater, container, false)

        binding.btnStartSearch.setOnClickListener {
            it.hideKeyboard()
            startSearchMovie()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun startFragmentDetail(imdbID: Any) {
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("imdbID", imdbID.toString())
        editor.apply()
        val frgDetail = FragmentDetail()
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        ft.replace(R.id.fragmentView, frgDetail)
        ft.commit()
    }

    private fun startSearchMovie() {
        val adapter = SearchMovieRecyclerAdapter()
        binding.movieRecyclerView.adapter = adapter

        val retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create()).build()

        val retrofitInterface = retrofit.create(RetrofitInterfaceClass::class.java)

        retrofitInterface.searchMovieByTitle(apikey, binding.movieNameInput.text.toString())
            .enqueue(object : Callback<MovieListResult> {
                override fun onResponse(
                    call: Call<MovieListResult>,
                    response: Response<MovieListResult>
                ) {
                    adapter.submitList(response.body()?.Search)
                }

                override fun onFailure(call: Call<MovieListResult>, t: Throwable) {
                    Log.d("TAG", "onFailure: ${t.message}")
                }
            })
    }

    private fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

}


