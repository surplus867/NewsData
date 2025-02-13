package com.example.newsdata

import android.app.Application
import com.example.newsdata.core.di.coreModule
import com.example.newsdata.news.di.newsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            if (BuildConfig.DEBUG) {
                androidLogger() // Enable Koin logging in debug mode
            }
            androidContext(this@App)
            modules(coreModule, newsModule)
        }
    }
}