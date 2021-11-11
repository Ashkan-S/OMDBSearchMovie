package com.example.omdbsearchmovie.features.fragmentSearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.omdbsearchmovie.databinding.MoviesItemsBinding
import com.example.omdbsearchmovie.models.MovieRoom
import com.squareup.picasso.Picasso

class FavoriteMovieRecyclerAdapter(val clickListener: (String) -> Unit) :
    ListAdapter<MovieRoom, FavoriteMovieRecyclerAdapter.FavoriteMovieViewHolder>(
        FavoriteMovieDiffUtils()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMovieViewHolder =
        FavoriteMovieViewHolder(
            MoviesItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: FavoriteMovieViewHolder, position: Int) =
        holder.onBind(getItem(position))

    inner class FavoriteMovieViewHolder(private val binding: MoviesItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: MovieRoom) {
            Picasso.get().load(item.Poster).into(binding.imageViewMoviePoster)
            binding.textViewMovieName.text = item.Title
            val typeAndYear = item.Type + " " + item.Year
            binding.textViewMovieTypeAndYear.text = typeAndYear
            binding.root.setOnClickListener {
                clickListener(item.imdbID)
            }
        }
    }

    class FavoriteMovieDiffUtils : DiffUtil.ItemCallback<MovieRoom>() {
        override fun areItemsTheSame(oldItem: MovieRoom, newItem: MovieRoom) =
            oldItem.imdbID == newItem.imdbID

        override fun areContentsTheSame(oldItem: MovieRoom, newItem: MovieRoom) =
            oldItem.Title == newItem.Title && oldItem.Poster == newItem.Poster &&
                    oldItem.Type == newItem.Type && oldItem.Year == newItem.Year

    }
}