package com.example.newsdata.core.domain

import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun getNews(): Flow<NewsResult<NewsList>>
    suspend fun paginate(nextPage:String): Flow<NewsResult<NewsList>>
    //suspend fun getArticle(articleId:String): Flow<NewsResult<NewsList>>

}