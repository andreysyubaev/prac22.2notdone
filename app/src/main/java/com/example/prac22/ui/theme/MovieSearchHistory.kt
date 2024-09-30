package com.example.prac22.ui.theme

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_history")
data class MovieSearchHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val movieName: String,
    val movieYear: String,
    val movieRating: String
)

