package com.example.omdbsearchmovie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.omdbsearchmovie.databinding.MoviesItemsBinding
import com.squareup.picasso.Picasso

class SearchMovieRecyclerAdapter(val clickListener:(String)->Unit) :
    ListAdapter<Search, SearchMovieRecyclerAdapter.SearchMovieViewHolder>(MovieDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchMovieViewHolder =
        SearchMovieViewHolder(
            MoviesItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )

    override fun onBindViewHolder(holder: SearchMovieViewHolder, position: Int) =
        holder.onBind(getItem(position))

    inner class SearchMovieViewHolder(private val binding: MoviesItemsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: Search) {
            Picasso.get().load(item.Poster).into(binding.imageViewMoviePoster)
            binding.textViewMovieName.text = item.Title
            val typeAndYear = item.Type + " " + item.Year
            binding.textViewMovieTypeAndYear.text = typeAndYear
            binding.root.setOnClickListener{
                clickListener(item.imdbID)
            }
        }
    }

    class MovieDiffUtils : DiffUtil.ItemCallback<Search>() {
        override fun areItemsTheSame(oldItem: Search, newItem: Search) =
            oldItem.imdbID == newItem.imdbID

        override fun areContentsTheSame(oldItem: Search, newItem: Search) =
            oldItem.Title == newItem.Title && oldItem.Poster == newItem.Poster &&
                    oldItem.Type == newItem.Type && oldItem.Year == newItem.Year

    }
}