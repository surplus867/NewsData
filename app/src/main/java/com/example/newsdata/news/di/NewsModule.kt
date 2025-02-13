package com.example.newsdata.news.di

import com.example.newsdata.news.presentation.NewsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


// Define a module for the news feature
val newsModule = module {
    // Declaring a dependency for NewsViewModel
    viewModel { NewsViewModel(get()) }
}