package com.example.omdbsearchmovie.features.fragmentSearch

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.omdbsearchmovie.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentSearch : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel by viewModels<FragmentSearchViewModel>()

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

        binding.btnFavorite.setBackgroundColor(Color.argb(255, 255, 215, 0))
        binding.btnStartSearch.setBackgroundColor(Color.BLUE)

        val adapterSearchFromInternet = SearchMovieRecyclerAdapter {
            findNavController().navigate(
                FragmentSearchDirections.actionFragmentSearchToFragmentDetail(
                    it, "N"
                )
            )
        }

        val adapterSearchFromDB = FavoriteMovieRecyclerAdapter {
            findNavController().navigate(
                FragmentSearchDirections.actionFragmentSearchToFragmentDetail(
                    it, "Y"
                )
            )
        }

        binding.btnStartSearch.setOnClickListener { it ->
            it.hideKeyboard()
            binding.movieRecyclerView.adapter = adapterSearchFromInternet
            binding.btnFavorite.setBackgroundColor(Color.GRAY)
            binding.btnStartSearch.setBackgroundColor(Color.BLUE)

            if (binding.movieNameInput.text.toString() != "") {
                viewModel.onSearchClicked(binding.movieNameInput.text.toString())
                viewModel.liveDataForMovieListResult.observe(viewLifecycleOwner) {
                    adapterSearchFromInternet.submitList(it.Search)
                }
            }
        }

        binding.btnFavorite.setOnClickListener { it ->
            it.hideKeyboard()
            binding.movieRecyclerView.adapter = adapterSearchFromDB
            binding.btnFavorite.setBackgroundColor(Color.argb(255, 255, 215, 0))
            binding.btnStartSearch.setBackgroundColor(Color.GRAY)
            binding.movieNameInput.text.clear()

            viewModel.onFavoriteClicked()
            viewModel.liveDataForFavoriteMovieList.observe(viewLifecycleOwner) {
                adapterSearchFromDB.submitList(it)
            }
        }

    }

    private fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }
}