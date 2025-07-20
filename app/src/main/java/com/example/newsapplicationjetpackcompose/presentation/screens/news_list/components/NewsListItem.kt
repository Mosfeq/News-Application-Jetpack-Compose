package com.example.newsapplicationjetpackcompose.presentation.screens.news_list.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.sharp.PlayArrow
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.newsapplicationjetpackcompose.domain.model.Article
import com.example.newsapplicationjetpackcompose.ui.theme.NewsApplicationJetpackComposeTheme
import org.jetbrains.annotations.Async

@Composable
fun NewsListItem(
    article: Article,
    onItemClick: (Article) -> Unit,
    onT2SClick: (Article) -> Unit,
    onSaveClick: (Article) -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .clickable { onItemClick(article) }
    ) {
        AsyncImage(
            model = article.urlToImage,
            contentDescription = "urlImage",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(10.dp),
            contentScale = ContentScale.Crop
        )
        Row(
            modifier = Modifier
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            article.title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .weight(4f)
                        .padding(end = 10.dp)
                )
            }
            SmallFloatingActionButton(
                onClick = {
                    onT2SClick(article)
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier
                    .weight(1f)
                    .padding(5.dp)
            ) {
                Icon(Icons.Sharp.PlayArrow, "")
            }
            SmallFloatingActionButton(
                onClick = {
                    onSaveClick(article)
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier
                    .weight(1f)
                    .padding(5.dp)
            ) {
                Icon(Icons.Rounded.Favorite, "")
            }
        }
        Divider(modifier = Modifier.padding(bottom = 10.dp))
    }
}

@Preview
@PreviewLightDark
@Composable
private fun NewsListItemPreview() {
    NewsApplicationJetpackComposeTheme {
        NewsListItem(
            article = Article(title = "Very fun new article! "),
            onItemClick = {},
            onT2SClick = {},
            onSaveClick = {}
        )
    }
}

