package com.example.newsdata.article.presentation

/**
 * Represents different actions that can be performed in the Article screen.
 * Sealed class ensures type safety and makes it easy to extend.
 */
sealed class ArticleAction {
    data class LoadArticle(val articleId: String): ArticleAction()
}