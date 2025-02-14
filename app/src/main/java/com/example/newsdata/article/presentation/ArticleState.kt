package com.example.newsdata.article.presentation

import com.example.newsdata.core.domain.Article

// Data class representing the state of the article screen
data class ArticleState(
    val article: Article? = null,   // The loaded article (if available)
    val isLoading: Boolean = false, // Indicates if data is currently being fetched
    val isError: Boolean = false    // Indicates if an error occurred
)
