package com.example.marketlens.components.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.marketlens.domain.models.MarketNews

@Composable
fun AssetDetailScreen(
    ticker: String,
    name: String,
    price: Double,
    change: Double,
    isCrypto: Boolean,
    viewModel: AssetDetailScreenViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val accentGreen = Color(0xFF00C853)

    LaunchedEffect(ticker) {
        viewModel.loadAssetDetails(ticker, name, price, change, isCrypto)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .systemBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // --- Header ---
        HeaderSection(state, accentGreen)

        Spacer(modifier = Modifier.height(24.dp))
        Spacer(modifier = Modifier.height(20.dp))

        // --- Insight IA ---
        IASummaryCard(state.aiSummary, accentGreen)

        Spacer(modifier = Modifier.height(20.dp))

        // --- Contenedor de Noticias ---
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = accentGreen)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                // Sentimiento (Stockdata)
                item { SectionHeader("Análisis de Sentimiento (Stockdata)") }
                if (state.assetNews.isEmpty()) {
                    item { Text("No hay análisis de sentimiento reciente.", color = Color.DarkGray, fontSize = 13.sp) }
                }
                items(state.assetNews) { news -> NewsSentimentItem(news, accentGreen) }

                // Corporativas (Finnhub)
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    SectionHeader("Flujo de Noticias Corporativas (Finnhub)")
                }
                if (state.finnhubNews.isEmpty()) {
                    item { Text("No se encontraron eventos corporativos registrados.", color = Color.DarkGray, fontSize = 13.sp) }
                }
                items(state.finnhubNews) { news -> NewsCorporateItem(news, accentGreen) }
            }
        }
    }
}

// ==========================================
// COMPONENTES DE APOYO (REUSABLES)
// ==========================================

@Composable
private fun HeaderSection(state: AssetDetailScreenState, accentGreen: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(state.ticker, color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Text(state.assetName, color = Color.Gray, fontSize = 16.sp)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text("$${String.format("%.2f", state.currentPrice)}", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.SemiBold)
            Text(
                text = "${if (state.changePercentage >= 0) "+" else ""}${String.format("%.2f", state.changePercentage)}%",
                color = if (state.changePercentage >= 0) accentGreen else Color.Red,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun InfoCard(label: String, value: String, modifier: Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, color = Color.Gray, fontSize = 12.sp)
            Text(value, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun IASummaryCard(summary: String?, accentGreen: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Insight de Inteligencia Artificial", color = accentGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = summary ?: "Generando resumen inteligente...", color = Color.White, fontSize = 14.sp)
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(title, color = Color.Gray, fontSize = 14.sp, fontWeight = FontWeight.Bold)
}

@Composable
private fun NewsSentimentItem(news: MarketNews, accentGreen: Color) {
    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(news.headline, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(news.source, color = Color.Gray, fontSize = 12.sp)
                news.sentimentScore?.let { score ->
                    val scoreColor = if (score > 0) accentGreen else if (score < 0) Color.Red else Color.Gray
                    Text("Sentimiento: ${String.format("%.2f", score)}", color = scoreColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun NewsCorporateItem(news: MarketNews, accentGreen: Color) {
    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(news.headline, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = news.summary, color = Color.LightGray, fontSize = 12.sp, maxLines = 2)
            Text("Fuente: ${news.source}", color = accentGreen, fontSize = 11.sp, modifier = Modifier.padding(top = 6.dp))
        }
    }
}

// Utilidad para acortar números grandes
private fun formatCompact(value: Double?): String {
    if (value == null) return "--"
    return when {
        value >= 1_000_000_000_000 -> String.format("%.2fT", value / 1_000_000_000_000)
        value >= 1_000_000_000 -> String.format("%.2fB", value / 1_000_000_000)
        value >= 1_000_000 -> String.format("%.2fM", value / 1_000_000)
        else -> String.format("%.2f", value)
    }
}