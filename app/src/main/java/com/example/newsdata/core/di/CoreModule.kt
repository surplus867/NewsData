package com.example.newsdata.core.di

import androidx.room.Room
import com.example.newsdata.core.data.NewsRepositoryImpl
import com.example.newsdata.core.data.local.ArticleDatabase
import com.example.newsdata.core.domain.NewsRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

// Define a koin module that groups related dependency declarations.
val coreModule = module {

    // Declare a singleton instance of NewsRepositoryImpl that also implements NewsRepository.
    // The 'singleOf' function creates a singleton (one shared instance) for the repository.
    singleOf(::NewsRepositoryImpl).bind<NewsRepository>()

    // Declare a singleton for the Room database.
    // This builds the database using the application's context.
    single {
        Room.databaseBuilder(
            androidApplication(),           // Provides the Android application context.
            ArticleDatabase::class.java,       // Specifies the Room database class.
            "article_db.db"             // Names the database file.
        ).build()
    }

    // Declare a singleton for the DAO (Data Access Object) retrieved from the ArticleDatabase.
    single {
        get<ArticleDatabase>().dao
    }

    // Declare a singleton for the Ktor HttpClient.
    single {
        HttpClient(CIO) {
            // Expect successful responses (2xx status code)
            expectSuccess = true

            // Configure the HTTP Client engine.
            engine {
                endpoint {
                    keepAliveTime = 5000        // Keep-alive time in milliseconds.
                    connectTimeout = 30_000    // Connection timeout in milliseconds.
                }
            }
            // Install ContentNegotiation to handle JSON serialization/deserialization.
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true      // Format JSON output in a human0readable way.
                        isLenient = true       // Allow lenient parsing of JSON.
                        ignoreUnknownKeys = true// Ignore unknown keys in JSON responses.
                    }
                )
            }

            // Set up a default request header for all HTTP requests.
            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }

            // Install a logging features to print out network request details
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        // Use Android's Log for better debugging in an Android  environment.
                        android.util.Log.d("KtorLogger", message) // Use Android logging
                    }
                }
                level = LogLevel.ALL // Log all information about the requests/responses.
            }
        }
    }
}