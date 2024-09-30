package com.example.prac22

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.prac22.databinding.ActivityVar6Binding
import com.example.prac22.ui.theme.MainDb
import com.example.prac22.ui.theme.MovieSearchHistory
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.json.JSONObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class var6 : AppCompatActivity() {
    lateinit var binding: ActivityVar6Binding

    private lateinit var search: Button
    private lateinit var searchMovie: EditText
    private lateinit var movieImage: ImageView
    private lateinit var movieName: TextView
    private lateinit var movieYear: TextView
    private lateinit var movieRating: TextView
    private lateinit var editOn: TextView
    private lateinit var changeAccept: TextView

    private var temp: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVar6Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = MainDb.getDb(this)
        val movie = MovieSearchHistory(
            movieName = movieName.toString(),
            movieYear = movieYear.toString(),
            movieRating = movieRating.toString()
        )

        // Вставка фильма в базу данных
        CoroutineScope(Dispatchers.IO).launch {
            val movie = MovieSearchHistory(
                movieName = "Inception",
                movieYear = "2010",
                movieRating = "8.8"
            )
            db.movieDao().insertMovie(movie)
        }

        // Получение всех фильмов из базы данных
        CoroutineScope(Dispatchers.IO).launch {
            val movies = db.movieDao().getAllMovies()
            withContext(Dispatchers.Main) {
                // обновление UI с полученными фильмами
            }
        }



        search = findViewById(R.id.search)
        searchMovie = findViewById(R.id.searchMovie)
        movieImage = findViewById(R.id.movieImage)
        movieName = findViewById(R.id.movieName)
        movieYear = findViewById(R.id.movieYear)
        movieRating = findViewById(R.id.movieRating)
        editOn = findViewById(R.id.editOn)
        changeAccept = findViewById(R.id.changeAccept)

        movieName.visibility = View.INVISIBLE
        movieYear.visibility = View.INVISIBLE
        movieRating.visibility = View.INVISIBLE
        editOn.visibility = View.GONE
        changeAccept.visibility = View.GONE

        movieName.isClickable = false
        movieYear.isClickable = false
        movieRating.isClickable = false
    }

    fun search(view: View) {
        val movie = searchMovie.text.toString()
        if (movie.isNotEmpty()) {
            val key = "9FF39M6-PZ34NZK-Q1MCYNC-M2127Y5"
            val url = "https://api.kinopoisk.dev/v1.4/movie/search?query=$movie&token=$key"
            val queue = Volley.newRequestQueue(this)
            val stringRequest = StringRequest(
                Request.Method.GET,
                url,
                { response ->
                    try {
                        val jsonObject = JSONObject(response)
                        val docs = jsonObject.getJSONArray("docs")
                        if (docs.length() > 0) {
                            val movieObject = docs.getJSONObject(0)
                            val imageUrl = movieObject.getJSONObject("poster").getString("url")
                            val name = movieObject.getString("name")
                            val year = movieObject.getInt("year")
                            val rating = movieObject.getJSONObject("rating").getString("kp")

                            Picasso.get().load(imageUrl).into(movieImage)
                            movieName.text = name
                            movieYear.text = year.toString()
                            movieRating.text = "Рейтинг: $rating"

                            movieImage.visibility = View.VISIBLE
                            movieName.visibility = View.VISIBLE
                            movieYear.visibility = View.VISIBLE
                            movieRating.visibility = View.VISIBLE
                            editOn.visibility = View.VISIBLE

                            search.text = ""
                        } else {
                            Snackbar.make(findViewById(android.R.id.content), "Movie not found", Snackbar.LENGTH_LONG)
                                .setActionTextColor(Color.RED)
                                .show()
                        }
                    } catch (e: Exception) {
                        Log.d("MyLog", "JSON error: $e")
                    }
                },
                { error ->
                    Log.d("MyLog", "Volley error: $error")
                    Snackbar.make(findViewById(android.R.id.content), "Error fetching movie data", Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.RED)
                        .show()
                }
            )
            queue.add(stringRequest)
        } else {
            Snackbar.make(findViewById(android.R.id.content), "Please enter a movie name", Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.RED)
                .show()
        }
    }

    fun editOn(view: View){
        editOn.visibility = View.GONE
        changeAccept.visibility = View.VISIBLE
        search.visibility = View.GONE

        movieName.isClickable = true
        movieYear.isClickable = true
        movieRating.isClickable = true

        movieName.setOnClickListener {
            searchMovie.setText(movieName.text)
            searchMovie.requestFocus()

            temp = 1
        }

        movieYear.setOnClickListener {
            searchMovie.setText(movieYear.text)
            searchMovie.requestFocus()

            temp = 2
        }

        movieRating.setOnClickListener {
            searchMovie.setText(movieRating.text)
            searchMovie.requestFocus()

            temp = 3
        }
    }

    fun changeAccept(view: View){
        editOn.visibility = View.VISIBLE
        changeAccept.visibility = View.GONE
        search.visibility = View.VISIBLE

        movieName.isClickable = false
        movieYear.isClickable = false
        movieRating.isClickable = false

        if (temp == 1)
            movieName.text = searchMovie.text.toString()
        else if (temp == 2)
            movieYear.text = searchMovie.text.toString()
        else if (temp == 3)
            movieRating.text = searchMovie.text.toString()

        temp = 0
        searchMovie.text = null
    }
}