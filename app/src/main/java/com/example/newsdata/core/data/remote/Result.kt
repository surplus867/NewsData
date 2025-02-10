package com.example.newsdata.core.data.remote

data class Result(
    val ai_org: List<String>,
    val ai_region: List<String>,
    val ai_tag: List<String>,
    val article_id: String,
    val category: List<String>,
    val content: String,
    val country: List<String>,
    val creator: List<String>,
    val description: String,
    val duplicate: Boolean,
    val image_url: String,
    val keywords: List<String>,
    val language: String,
    val link: String,
    val pubDate: String,
    val pubDateTZ: String,
    val sentiment: String,
    val sentiment_stats: SentimentStats,
    val source_icon: String,
    val source_id: String,
    val source_name: String,
    val source_priority: Int,
    val source_url: String,
    val title: String,
    val video_url: Any
)