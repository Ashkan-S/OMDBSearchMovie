package com.example.omdbsearchmovie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class SearchMovieRecyclerAdapter(private val list: List<Search>) :
    RecyclerView.Adapter<SearchMovieRecyclerAdapter.SearchMovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchMovieViewHolder =
        SearchMovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.movies_items, parent, false)
        )

    override fun onBindViewHolder(holder: SearchMovieViewHolder, position: Int) =
        holder.onBind(list[position])

    override fun getItemCount(): Int = list.size

    class SearchMovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imageViewMoviePoster: ImageView = itemView.findViewById(R.id.imageViewMoviePoster)
        private val textViewMovieName: TextView = itemView.findViewById(R.id.textViewMovieName)
        private val textViewMovieTypeAndYear: TextView =
            itemView.findViewById(R.id.textViewMovieTypeAndYear)

        fun onBind(item: Search) {

            Picasso.get().load(item.Poster).into(imageViewMoviePoster)
            textViewMovieName.text = item.Title
            textViewMovieTypeAndYear.text = item.Type + " " + item.Year
        }
    }
}