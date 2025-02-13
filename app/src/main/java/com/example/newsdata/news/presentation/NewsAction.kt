package com.example.newsdata.news.presentation

sealed interface NewsAction {
    data object Paginate: NewsAction
}