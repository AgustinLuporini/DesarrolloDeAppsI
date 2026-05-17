package com.example.marketlens.components.market

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.marketlens.domain.models.Asset

@Composable
fun MarketScreen(navController: NavController, viewModel: MarketScreenViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    //Filtrado por ticker
    val filteredCryptos = state.cryptos.filter {
        it.ticker.contains(state.searchQuery, ignoreCase = true)
    }
    val filteredStocks = state.stocks.filter {
        it.ticker.contains(state.searchQuery, ignoreCase = true)
    }

    val accentGreen = Color(0xFF00C853)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Text(
            text = "Mercados",
            color = accentGreen,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Barra de busqueda
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = { viewModel.onSearchQueryChanged(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            placeholder = { Text("Buscar por ticker...", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar", tint = accentGreen) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = accentGreen,
                unfocusedBorderColor = Color.DarkGray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = accentGreen)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                // Criptos
                if (filteredCryptos.isNotEmpty()) {
                    item {
                        Text("Criptomonedas", color = Color.Gray, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    items(filteredCryptos) { crypto ->
                        AssetRow(asset = crypto, onClick = { /* Ir al detalle después */ })
                    }
                }

                // Acciones
                if (filteredStocks.isNotEmpty()) {
                    item {
                        Text("Acciones", color = Color.Gray, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    items(filteredStocks) { stock ->
                        AssetRow(asset = stock, onClick = { /* Ir al detalle después */ })
                    }
                }

                // Sin resultados
                if (filteredCryptos.isEmpty() && filteredStocks.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            Text("No se encontraron activos con ese ticker", color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AssetRow(asset: Asset, onClick: () -> Unit) {
    val priceColor = if (asset.changePercentage >= 0) Color(0xFF00C853) else Color.Red

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(asset.ticker, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(asset.name, color = Color.Gray, fontSize = 12.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("$${String.format("%.2f", asset.currentPrice)}", color = Color.White, fontWeight = FontWeight.SemiBold)
                Text(
                    text = "${if (asset.changePercentage >= 0) "+" else ""}${String.format("%.2f", asset.changePercentage)}%",
                    color = priceColor,
                    fontSize = 12.sp
                )
            }
        }
    }
}