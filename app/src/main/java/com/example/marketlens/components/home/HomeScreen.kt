package com.example.marketlens.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.marketlens.components.Screen

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeScreenViewModel = viewModel()) {
    // REQUISITO TPO: collectAsStateWithLifecycle
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreenContent(
        state = uiState,
        onMarketClick = { navController.navigate(Screen.Market.route) },
        onProfileClick = { navController.navigate(Screen.Profile.route) }
    )
}

@Composable
fun HomeScreenContent(
    state: HomeScreenState,
    onMarketClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val accentGreen = Color(0xFF00C853) // Verde financiero vibrante

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black) // Fondo Negro de tu propuesta
            .padding(16.dp)
    ) {
        // Título Principal
        Text(
            text = "MarketLens",
            color = accentGreen,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = accentGreen)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Sección Fear & Greed
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Índice de Miedo y Codicia", color = Color.Gray, fontSize = 14.sp)
                            Text(
                                text = state.fearAndGreed?.value ?: "--",
                                color = Color.White,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = state.fearAndGreed?.description ?: "Cargando...",
                                color = accentGreen
                            )
                        }
                    }
                }

                // Botones Temporales (En reemplazo de la BottomBar por ahora)
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onMarketClick,
                            colors = ButtonDefaults.buttonColors(containerColor = accentGreen),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Ver Mercados", color = Color.Black)
                        }
                        Button(
                            onClick = onProfileClick,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Mi Perfil")
                        }
                    }
                }


                // Sección fred
                item {
                    Text("Indicadores Globales", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                }

                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(state.macroIndicators) { indicator ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
                                modifier = Modifier.width(150.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(indicator.description, color = Color.Gray, fontSize = 12.sp)
                                    Text(
                                        text = if(indicator.description.contains("Tasa")) "${indicator.value}%" else indicator.value,
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                // Sección Noticias
                item {
                    Text("Noticias Destacadas", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                }

                items(state.marketNews) { news ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(text = news.headline, color = Color.White, fontWeight = FontWeight.Medium)
                        Text(text = news.source, color = accentGreen, fontSize = 12.sp)
                        Divider(modifier = Modifier.padding(top = 8.dp), color = Color.DarkGray)
                    }
                }
            }
        }
    }
}