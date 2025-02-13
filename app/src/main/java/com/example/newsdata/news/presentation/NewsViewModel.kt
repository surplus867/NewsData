package com.example.newsdata.news.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsdata.core.domain.NewsRepository
import com.example.newsdata.core.domain.NewsResult
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing the UI state of the news screen.
 * It interacts with the NewsRepository to load and paginate news articles.
 * and exposes a mutable state for the UI to observe.
 */
class NewsViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

    // Holds the current state of the news UI.
    // Any changes to 'state' trigger recomposition in Compose.
    var state by mutableStateOf(NewsState())
        private set

    // When the ViewModel is created, immediately start loading news,
    init {
        loadNews()
    }

    /**
     * Handles actions from the UI.
     * Currently, it supports only the paginate action.
     */
    fun onAction(action: NewsAction) {
        when (action) {
            NewsAction.Paginate -> {
                paginate()
            }
        }
    }

    /**
     * Loads the initial set of news articles.
     * It updates the UI state to show loading, collects data from the repository.
     * and updates the state based on the result.
     */
    private fun loadNews() {
        viewModelScope.launch {
            // Indicate that loading has started.
            state = state.copy(isLoading = true)

            // Collect results from the repository.
            newsRepository.getNews().collect { newsResult ->
                state = when (newsResult) {
                    // If there's an error, updates the state to reflect that.
                    is NewsResult.Error -> state.copy(isError = true)
                    // On success, update the articles list and pagination info.
                    is NewsResult.Success -> state.copy(
                            isError = false,
                            articleList = newsResult.data?.articles ?: emptyList(),
                            nextPage = newsResult.data?.nextPage
                        )
                    }
                }
            // Indicate that loading has finished.
            state = state.copy(isLoading = false)
        }
    }

    /**
     * Loads additional news articles (pagination).
     * It appends the new articles to the existing list.
     */
    private fun paginate() {
        viewModelScope.launch {
            // Check if there is a next page to load.
            // If there's no next page, exit the coroutine early.
            if (state.nextPage == null) return@launch

            // Update the UI state to indicate the loading has started,
            // and clear any previous error state,
            state = state.copy(isLoading = true, isError = false)

            // Begin collecting the paginated data from the repository.
            newsRepository.paginate(state.nextPage).collect { newsResult ->
                // Update the state based on the type of result we receive:
                state = when (newsResult) {
                    // If the result is an error, update the state to reflect the error.
                    // Stop loading, and provide an error message (using a default if necessary).
                    is NewsResult.Error -> state.copy(
                        isError = true,
                        isLoading = false,
                        errorMessage = newsResult.message ?: "Failed to load more news"
                        )
                    // If the result is successful, append the new articles to the existing list.
                    // update the pagination info (nextPage), and stop the loading indicator.
                    is NewsResult.Success -> {
                        val articles = newsResult.data?.articles ?: emptyList()
                        state.copy(
                            isError = false,
                            articleList = state.articleList + articles,  // Append new articles
                            nextPage = newsResult.data?.nextPage,          // Update next page token/info
                            isLoading = false
                        )
                    }
                }
            }
        }
    }
}