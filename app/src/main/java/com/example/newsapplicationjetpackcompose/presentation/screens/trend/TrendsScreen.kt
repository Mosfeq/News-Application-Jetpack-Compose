package com.example.newsapplicationjetpackcompose.presentation.screens.trend

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.example.newsapplicationjetpackcompose.presentation.screens.common.viewmodel.CounterViewModel

@Composable
fun TrendsScreen(
    viewModel: CounterViewModel = hiltViewModel()
) {

    val readCounter by viewModel.readCounter.collectAsStateWithLifecycle()
    val savedCounter by viewModel.savedCounter.collectAsStateWithLifecycle()

    val totalArticles = readCounter + savedCounter

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Your Activity",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Track your reading habits",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            StatCard(
                title = "Read",
                value = readCounter.toString(),
                backgroundColor = Color(0xFF6366F1),
                textColor = Color.White,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.size(12.dp))
            StatCard(
                title = "Saved",
                value = savedCounter.toString(),
                backgroundColor = Color(0xFFEC4899),
                textColor = Color.White,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (totalArticles > 0) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val pieChartData = PieChartData(
                    slices = listOf(
                        PieChartData.Slice(
                            label = "Read Articles",
                            value = readCounter.toFloat(),
                            color = Color(0xFF6366F1)
                        ),
                        PieChartData.Slice(
                            label = "Saved Articles",
                            value = savedCounter.toFloat(),
                            color = Color(0xFFEC4899)
                        )
                    ),
                    plotType = PlotType.Pie
                )

                val pieChartConfig = PieChartConfig(
                    isAnimationEnable = true,
                    showSliceLabels = true,
                    labelVisible = true,
                    activeSliceAlpha = 0.5f,
                    animationDuration = 600,
                )

                PieChart(
                    modifier = Modifier.fillMaxSize(),
                    pieChartData = pieChartData,
                    pieChartConfig = pieChartConfig
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "📊",
                        style = MaterialTheme.typography.displayLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No Activity Yet",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Start reading and saving articles to see your stats",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(120.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = textColor.copy(alpha = 0.9f),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}