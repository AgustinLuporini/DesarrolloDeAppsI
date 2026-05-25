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
            .systemBarsPadding() // FIX: Mantiene el título principal lejos de la barra de estado superior
            .padding(horizontal = 20.dp, vertical = 16.dp) // Márgenes laterales más amplios y modernos
    ) {
        Text(
            text = "Mercados",
            color = accentGreen,
            fontSize = 28.sp, // Un toque más grande para que parezca un header real
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Barra de busqueda
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = { viewModel.onSearchQueryChanged(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp), // Un poco más de aire antes de la lista
            placeholder = { Text("Buscar por ticker...", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar", tint = accentGreen) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = accentGreen,
                unfocusedBorderColor = Color.DarkGray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color(0xFF1A1A1A), // Hace que el input se vea mejor al seleccionarlo
                unfocusedContainerColor = Color(0xFF1A1A1A)
            ),
            shape = MaterialTheme.shapes.medium
        )

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = accentGreen)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp) // FIX: Evita que el último elemento quede cortado por la navegación del celu
            ) {

                // Criptos
                if (filteredCryptos.isNotEmpty()) {
                    item {
                        Text("Criptomonedas", color = Color.Gray, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    items(filteredCryptos) { crypto ->
                        AssetRow(asset = crypto, onClick = {
                            // FIX: Ruta simplificada sin el isCrypto final (vuelve al diseño original seguro)
                            navController.navigate("asset_detail_screen/${crypto.ticker}/${crypto.name}/${crypto.currentPrice}/${crypto.changePercentage}")
                        })
                    }
                }

                // Acciones
                if (filteredStocks.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Acciones", color = Color.Gray, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    items(filteredStocks) { stock ->
                        AssetRow(asset = stock, onClick = {
                            // FIX: Ruta simplificada
                            navController.navigate("asset_detail_screen/${stock.ticker}/${stock.name}/${stock.currentPrice}/${stock.changePercentage}")
                        })
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