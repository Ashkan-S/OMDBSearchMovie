package com.example.omdbsearchmovie.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.omdbsearchmovie.*
import com.example.omdbsearchmovie.adapters.FavoriteMovieRecyclerAdapter
import com.example.omdbsearchmovie.adapters.SearchMovieRecyclerAdapter
import com.example.omdbsearchmovie.databinding.FragmentSearchBinding
import com.example.omdbsearchmovie.models.MovieListResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FragmentSearch : Fragment() {

    private val apikey = "ab906375"

    @Inject
    lateinit var db: AppDatabase

    @Inject
    lateinit var retrofitInterface: RetrofitInterfaceClass

    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapterSearchFromInternet = SearchMovieRecyclerAdapter {
            findNavController().navigate(
                FragmentSearchDirections.actionFragmentSearchToFragmentDetail(
                    it
                )
            )
        }

        val adapterSearchFromDB = FavoriteMovieRecyclerAdapter {
            findNavController().navigate(
                FragmentSearchDirections.actionFragmentSearchToFragmentDetail(
                    it
                )
            )
        }

        binding.btnStartSearch.setOnClickListener {
            it.hideKeyboard()
            binding.movieRecyclerView.adapter = adapterSearchFromInternet

            retrofitInterface.searchMovieByTitle(apikey, binding.movieNameInput.text.toString())
                .enqueue(object : Callback<MovieListResult> {
                    override fun onResponse(
                        call: Call<MovieListResult>,
                        response: Response<MovieListResult>
                    ) {
                        adapterSearchFromInternet.submitList(response.body()?.Search)
                    }

                    override fun onFailure(call: Call<MovieListResult>, t: Throwable) {
                        Toast.makeText(
                            requireContext(),
                            "Please check you favorite movie",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.d("TAG", "onFailure: ${t.message}")
                    }
                })
        }

        binding.btnFavorite.setOnClickListener {
            binding.movieRecyclerView.adapter = adapterSearchFromDB
            lifecycleScope.launch(Dispatchers.IO) {
                adapterSearchFromDB.submitList(
                    db.FavoriteMovieDAO().getFavoriteMovie()
                )
            }
        }

    }

    private fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }
}