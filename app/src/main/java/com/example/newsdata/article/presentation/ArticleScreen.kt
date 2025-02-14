package com.example.newsdata.article.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.newsdata.core.domain.Article
import com.example.newsdata.core.presentation.ui.theme.NewsDataTheme
import org.koin.androidx.compose.koinViewModel


@Composable
fun ArticleScreenCore(
    viewModel: ArticleViewModel = koinViewModel(),
    articleId: String
) {
    // Trigger article loading only when articleId changes
    LaunchedEffect(true) {
        viewModel.onAction(ArticleAction.LoadArticle(articleId))
    }

    // Pass the ViewModel state to the UI
    ArticleScreen(
        state = viewModel.state
    )

}

@Composable
private fun ArticleScreen(
    state: ArticleState,
) {
    Scaffold { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            when {
                // Show loading indicator if the article is not loaded yet
                state.isLoading && state.article == null -> {
                    Box(modifier = Modifier.padding(16.dp)) {
                        CircularProgressIndicator()
                    }
                }
                // Show error message if loading failed
                state.isError && state.article == null -> {
                    Text(
                        text = "Can't load articles",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                // Show the article content once loaded
                else -> state.article?.let { article ->
                    ArticleDetails(article = article)

                }
            }
        }
    }
}

@Composable
fun ArticleDetails(
    modifier: Modifier = Modifier,
    article: Article
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(vertical = 22.dp)
    ) {
        // Display article source name, fallback to "Unknown Source" if null
        Text(
            text = article.sourceName ?: "Unknown Source",
            fontSize = 24.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Display publication date
        Text(
            text = article.pubDate ?: "Unknown Date",
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display article title
        Text(
            text = article.title ?: "No Title",
            fontWeight = FontWeight.SemiBold,
            fontSize = 17.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display article image with a placeholder and error fallback
        AsyncImage(
            model = article.imageUrl,
            contentDescription = article.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary.copy(0.4f))
                .height(250.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        //Display article description
        Text(
            text = article.description ?: "No description available.",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider()

        Spacer(modifier = Modifier.height(16.dp))

        // Display full article content
        Text(
            text = article.content,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}


@Preview
@Composable
private fun ArticleScreenPreview() {
    NewsDataTheme {
        ArticleScreen(
            state = ArticleState()
        )
    }
}