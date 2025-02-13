package com.example.newsdata.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.newsdata.core.presentation.ui.theme.NewsDataTheme
import com.example.newsdata.news.presentation.NewsScreenCore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewsDataTheme {
                Navigation()
            }
        }
    }

    @Composable
    fun Navigation(modifier: Modifier = Modifier) {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = Screen.News
        ) {

            composable<Screen.News> {
                NewsScreenCore {
                    navController.navigate(Screen.Article(it))
                }

            }

            composable<Screen.Article> { backStackEntry ->
                val article: Screen.Article = backStackEntry.toRoute()
                article.articleId

            }
        }
    }

}
