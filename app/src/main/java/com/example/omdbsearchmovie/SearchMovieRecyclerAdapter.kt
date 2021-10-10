package com.example.omdbsearchmovie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class SearchMovieRecyclerAdapter() :
    ListAdapter<Search, SearchMovieRecyclerAdapter.SearchMovieViewHolder>(MovieDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchMovieViewHolder =
        SearchMovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.movies_items, parent, false)
        )

    override fun onBindViewHolder(holder: SearchMovieViewHolder, position: Int) =
        holder.onBind(getItem(position))

    class SearchMovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imageViewMoviePoster: ImageView =
            itemView.findViewById(R.id.imageViewMoviePoster)
        private val textViewMovieName: TextView = itemView.findViewById(R.id.textViewMovieName)
        private val textViewMovieTypeAndYear: TextView =
            itemView.findViewById(R.id.textViewMovieTypeAndYear)

        fun onBind(item: Search) {
            Picasso.get().load(item.Poster).into(imageViewMoviePoster)
            textViewMovieName.text = item.Title
            val typeAndYear = item.Type + " " + item.Year
            textViewMovieTypeAndYear.text = typeAndYear
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