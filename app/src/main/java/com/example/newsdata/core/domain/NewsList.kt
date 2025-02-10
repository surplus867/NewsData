package com.example.newsdata.core.domain

import kotlinx.serialization.Serializable

@Serializable
data class NewsList(
    val nextPage: String?,
    val articles: List<Article>,
)
