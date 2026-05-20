package com.example.marketlens.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.marketlens.components.Screen

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeScreenViewModel = viewModel()) {
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
    val accentGreen = Color(0xFF00C853)
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .systemBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "MarketLens",
            color = accentGreen,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
        )

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = accentGreen)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
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

                item {
                    Text("Noticias Destacadas", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                }

                items(state.marketNews) { news ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (news.url.isNotEmpty()) {
                                    uriHandler.openUri(news.url)
                                }
                            }
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AsyncImage(
                            model = news.imageUrl,
                            contentDescription = "Noticia",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.DarkGray)
                        )

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = news.headline,
                                color = Color.White,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = news.source, color = accentGreen, fontSize = 12.sp)
                                Text(text = news.date, color = Color.Gray, fontSize = 12.sp)
                            }
                        }
                    }
                    HorizontalDivider(color = Color.DarkGray)
                }
            }
        }
    }
}