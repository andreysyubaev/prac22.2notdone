package com.example.prac22.ui.theme

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert
    suspend fun insertMovie(movie: MovieSearchHistory)

    // Метод для корутин, возвращает список фильмов
    @Query("SELECT * FROM search_history")
    suspend fun getAllMovies(): List<MovieSearchHistory>

    // Метод для потоков данных (Flow)
    @Query("SELECT * FROM search_history")
    fun getAllMoviesFlow(): Flow<List<MovieSearchHistory>>
}

