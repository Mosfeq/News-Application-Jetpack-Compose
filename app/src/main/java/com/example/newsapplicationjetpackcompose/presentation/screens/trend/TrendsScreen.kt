package com.example.newsapplicationjetpackcompose.presentation.screens.trend

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
){

    val readCounter by viewModel.readCounter.collectAsStateWithLifecycle()
    val savedCounter by viewModel.savedCounter.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val pieChartData = PieChartData(
                slices = listOf(
                    PieChartData.Slice(
                        label = "Read News",
                        value = readCounter.toFloat(),
                        color = Color(0xFF333333)
                    ),
                    PieChartData.Slice(
                        label = "Saved News",
                        value = savedCounter.toFloat(),
                        color = Color(0xFF666a86)
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
    }

}