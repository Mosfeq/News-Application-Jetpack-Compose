package com.example.newsapplicationjetpackcompose.presentation.screens.saved_news_list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.newsapplicationjetpackcompose.domain.model.Article
import com.example.newsapplicationjetpackcompose.domain.model.Source
import com.example.newsapplicationjetpackcompose.ui.theme.NewsApplicationJetpackComposeTheme

@Composable
fun SavedNewsListItem(
    article: Article,
    onItemClick: (Article) -> Unit,
    onT2SClick: (Article) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onItemClick(article) },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 6.dp
        ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .width(140.dp)
                    .fillMaxWidth()
            ) {
                AsyncImage(
                    model = article.urlToImage,
                    contentDescription = "Article thumbnail",
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(140.dp)
                        .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    article.source?.name?.let { sourceName ->
                        Text(
                            text = sourceName.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    article.title?.let { title ->
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    article.author?.let { author ->
                        Text(
                            text = author,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                    } ?: Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick = { onT2SClick(article) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.PlayArrow,
                            contentDescription = "Play audio",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@PreviewLightDark
@Composable
private fun SavedNewsItemPreview() {
    NewsApplicationJetpackComposeTheme {
        Surface {
            Column {
                SavedNewsListItem(
                    article = Article(
                        title = "Breakthrough in Renewable Energy Technology Could Transform Power Generation",
                        author = "Jane Smith",
                        source = Source(name = "Tech Daily"),
                        urlToImage = "https://example.com/image.jpg"
                    ),
                    onItemClick = {},
                    onT2SClick = {}
                )
                Spacer(modifier = Modifier.height(8.dp))
                SavedNewsListItem(
                    article = Article(
                        title = "Local Community Rallies Together for Annual Charity Event",
                        source = Source(name = "Local News"),
                        urlToImage = "https://example.com/image2.jpg"
                    ),
                    onItemClick = {},
                    onT2SClick = {}
                )
            }
        }
    }
}