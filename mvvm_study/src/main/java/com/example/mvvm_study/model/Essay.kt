package com.example.mvvm_study.model

interface Essay {
    val id: Int
    val author: String
    val title: String
    val digest: String
    val content: String
    val wc: Long
}