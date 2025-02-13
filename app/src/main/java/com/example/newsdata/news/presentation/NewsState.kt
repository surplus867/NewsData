package com.example.newsdata.news.presentation

import com.example.newsdata.core.domain.Article

/**
 * Represents the UI state for the news screen.
 * Holds information about the list of articles, pagination, loading status, and errors.
 */
data class NewsState(
    val articleList: List<Article> = emptyList(), // List of articles currently displayed
    val nextPage: String? = null, // URL or token for the next page of results (if available)
    val isLoading: Boolean = false, // Indicates whether data is currently being fetched
    val isError: Boolean = false, // Flags if an error occurred during fetching
    val errorMessage: String? = null // Provides a user-friendly error message if an error occurs
)