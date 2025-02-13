package com.example.newsdata.core.domain

import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for fetching news data.
 * Defines methods for retrieving news articles and handling pagination.
 */
interface NewsRepository {

    /**
     * Fetches the latest news articles
     * @return A [Flow] emitting [NewsResult] containing a list of articles or an error.
     */
    suspend fun getNews(): Flow<NewsResult<NewsList>>

    /**
     * Loads additional news articles (pagination).
     * @param nextPage The token or URL for fetching the next set of articles. Can be null if there's no next page
     * @return A [Flow] emitting [NewsResult] containing the next batch of articles or an error.
     */
    suspend fun paginate(nextPage: String?): Flow<NewsResult<NewsList>>
    //suspend fun getArticle(articleId:String): Flow<NewsResult<NewsList>>

}