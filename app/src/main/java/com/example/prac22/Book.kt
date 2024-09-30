package com.example.prac22

import com.google.gson.Gson

data class Book(var title: String,
                val authors: List<String>,
                var isbn: String,
                var pageCount: Int,
                var isFiction: Boolean)

