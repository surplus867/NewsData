package com.example.newsdata.article.di

import com.example.newsdata.article.presentation.ArticleViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val articleModule = module {

    // Provide ArticleViewModel with necessary dependencies
    viewModel { ArticleViewModel(get()) }
}