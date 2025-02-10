package com.example.newsdata.core.data

import com.example.newsdata.BuildConfig
import com.example.newsdata.core.data.local.ArticlesDao
import com.example.newsdata.core.data.remote.NewsListDto
import com.example.newsdata.core.domain.NewsList
import com.example.newsdata.core.domain.NewsRepository
import com.example.newsdata.core.domain.NewsResult
import com.example.newsdata.toArticle
import com.example.newsdata.toArticleEntity
import com.example.newsdata.toNewsList
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Implementation of the new NewsRepository that fetches news articles
 * from both remote(API) and local database.
 */
class NewsRepositoryImpl(
    private val httpClient: HttpClient, // HttP client to fetch remote data
    private val dao: ArticlesDao // Data Access Object (DAO) for local database
) : NewsRepository {

    private val tag = "NewsRepository: "

    // Base URL for fetching news from API
    private val baseUrl = "https://newsdata.io/api/1/latest"

    // API key stored in BuildConfig (to keep it secure)
    private val apiKey = BuildConfig.NEWS_API_KEY

    /**
     * Fetches news articles from the local database.
     * @param nextPage Optional page parameter for pagination (currently unused).
     */
    private suspend fun getLocalNews(nextPage: String?): NewsList {
        val localNews = dao.getArticleList() // Retrieve articles from local database
        println(tag + "getLocalNews" + localNews.size + "nextPage: " + nextPage)

        val newsList = NewsList(
            nextPage = nextPage, // Placeholder for future pagination
            articles = localNews.map { it.toArticle() } // Convert database entities to domain models
        )

        return newsList
    }

    /**
     * Fetches news articles from the remote API.
     * @param nextPage The page number for pagination (null for first page).
     */

    private suspend fun getRemoteNews(nextPage: String?): NewsList {
        val newsListDto: NewsListDto = httpClient.get(baseUrl) {
            parameter("apiKey", apiKey) // API ket for authentication
            parameter("language", "en") // Fetch English names only
            if (nextPage != null) parameter("page", nextPage) // Add page parameter if paginating
        }.body()

        println(tag + "getRemoteNews:" + newsListDto.results?.size + " nextPage: " + nextPage)

        return newsListDto.toNewsList() // Convert API response to domain model
    }

    /**
     * Fetches news, prioritizing remote data but falling back to local data if needed.
     */
    override suspend fun getNews(): Flow<NewsResult<NewsList>> {
        return flow {
            // Attempt to fetch data from remote API
            val remoteNewsList = try {
                getRemoteNews(null) // Fetch first page
            } catch (e: Exception) {
                e.printStackTrace()
                println(tag + "getNews remote exception: " + e.message)
                null // Return null if network request fails
            }

            // If remote data is available, store it in the local database and emit it
            remoteNewsList?.let {
                dao.clearDatabase() // Remove old data to avoid duplicates
                dao.upsertArticleList(remoteNewsList.articles.map { it.toArticleEntity() }) // Save new data
                emit(NewsResult.Success(getLocalNews(remoteNewsList.nextPage))) // Emit local data
                return@flow
            }

            // If remote fetch failed, attempt to use local data
            val localNewsList = getLocalNews(null)
            if(localNewsList.articles.isNotEmpty()) {
                emit(NewsResult.Success(localNewsList))
                return@flow
            }

            // If no data is available, return an error
            emit(NewsResult.Error("No Data"))
        }
    }

    /**
     * Fetches the next page of news articles from the remote API.
     * @param nextPage The page number to fetch.
     */
    override suspend fun paginate(nextPage: String): Flow<NewsResult<NewsList>> {
        return flow {
            // Attempt to fetch data from the remote API for the given page
            val remoteNewsList = try {
                getRemoteNews(nextPage) // Fetch next page instead of null
            } catch (e: Exception) {
                e.printStackTrace()
                println(tag + "getNews remote exception: " + e.message)
                null // Return null if network request fails
            }

            // If remote data is available, store it and emit it
            remoteNewsList?.let {
                dao.clearDatabase()
                dao.upsertArticleList(remoteNewsList.articles.map { it.toArticleEntity()})

                // not getting them from the database like getNews()
                // because we will also get old items that we already have before paginating
                emit(NewsResult.Success(remoteNewsList)) // Emit fresh remote data
                return@flow
            }
        }
    }

    /*override suspend fun getArticle(articleId: String): Flow<NewsResult<NewsList>> {
        TODO("Not yet implemented")
    }*/
}