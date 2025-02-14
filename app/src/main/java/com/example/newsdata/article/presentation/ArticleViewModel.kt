package com.example.newsdata.article.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsdata.core.domain.NewsRepository
import com.example.newsdata.core.domain.NewsResult
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for handling article data and UI state.
 * Fetches articles from the repository and updates UI state accordingly.
 */
class ArticleViewModel(
    private val newsRepository: NewsRepository // Injected repository for fetching news data
) : ViewModel() {

    // Holds the current UI state (article data, loading state, and error state)
    var state by mutableStateOf(ArticleState())
        private set // Private setter ensures only the ViewModel can modify state

    /**
     * Handles actions triggered by the UI.
     * Currently, only supports loading an articles by ID.
     */
    fun onAction(action: ArticleAction) {
        when (action) {
            is ArticleAction.LoadArticle -> {
                loadArticle(action.articleId)
            }
        }
    }

    /**
     * Fetches an article from the repository based on the given article ID.
     * Updates the UI state based on the result (loading, success, or error).
     */
    private fun loadArticle(articleId: String) {
        // Validate input: If the article ID is blank, set an error state and return early.
        if (articleId.isBlank()) {
            state = state.copy(isError = true)
            return
        }

        // Launch a coroutine to fetch article data asynchronously
        viewModelScope.launch {
            // Set loading state and reset error before fetching
            state = state.copy(isLoading = true, isError = false)

            // Collect the latest emitted value from the repository
            newsRepository.getArticle(articleId).collect { articleResult ->
                state = when (articleResult) {

                    is NewsResult.Success ->
                        state.copy(
                            isLoading = false,
                            isError = false,
                            article = articleResult.data
                        )

                        // If the request fails, update the UI to reflect the error
                    is NewsResult.Error -> state.copy(
                        isLoading = false,
                        isError = true
                    )
                }
            }
        }
    }
}