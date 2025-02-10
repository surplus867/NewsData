package com.example.newsdata.core.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ArticlesDao {

    @Query("SELECT * FROM articleentity")
    suspend fun getArticleList(): List<ArticleEntity>

    @Upsert
    suspend fun upsertArticleList(articleList: List<ArticleEntity>)

    @Query("SELECT * FROM articleentity WHERE articleID = :articleId")
    suspend fun getArticle(articleId: String): ArticleEntity?

    @Query("DELETE FROM articleentity")
    suspend fun clearDatabase()
}