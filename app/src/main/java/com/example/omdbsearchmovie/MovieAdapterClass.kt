package com.example.omdbsearchmovie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import retrofit2.Response

internal class MovieAdapterClass(
    private val Inflater: LayoutInflater,
    private val movies: Response<MovieListResult>
) :
    BaseAdapter() {

    private lateinit var imageViewMoviePoster: ImageView
    private lateinit var textViewMovieName: TextView
    private lateinit var textViewMovieTypeAndYear: TextView

    override fun getCount(): Int {
        return movies.body()!!.Search.size
    }

    override fun getItem(position: Int): Any {
        return movies.body()!!.Search[position].imdbID
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView

        if (convertView == null) {
            convertView = Inflater.inflate(R.layout.movies_items, null)
        }

        imageViewMoviePoster = convertView!!.findViewById(R.id.imageViewMoviePoster)
        textViewMovieName = convertView.findViewById(R.id.textViewMovieName)
        textViewMovieTypeAndYear = convertView.findViewById(R.id.textViewMovieTypeAndYear)

        Picasso.get().load(movies.body()!!.Search[position].Poster).into(imageViewMoviePoster)
        textViewMovieName.text = movies.body()!!.Search[position].Title
        textViewMovieTypeAndYear.text =
            movies.body()!!.Search[position].Type + " " + movies.body()!!.Search[position].Year

        return convertView

    }
}