package com.example.marketlens.components.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileScreenViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val accentGreen = Color(0xFF00C853)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        // --- INFORMACIÓN DEL USUARIO ---
        Text(
            text = state.userName,
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
        )
        Text(
            text = "Usuario MarketLens",
            color = accentGreen,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- CONFIGURACIÓN BIOMÉTRICA ---
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Acceso Biométrico", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Habilitar huella dactilar", color = Color.Gray, fontSize = 12.sp)
                }
                Switch(
                    checked = state.isBiometricEnabled,
                    onCheckedChange = { viewModel.setBiometricEnabled(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.Black,
                        checkedTrackColor = accentGreen,
                        uncheckedThumbColor = Color.Gray,
                        uncheckedTrackColor = Color.DarkGray
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- SECCIÓN ACTIVOS FAVORITOS ---
        Text(
            text = "Mis Activos Favoritos",
            color = Color.Gray,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // LazyColumn para listar
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(state.favoriteAssets) { ticker ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = ticker,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "★",
                            color = accentGreen,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }

        // --- BOTÓN DE LOG OUT ---
        Button(
            onClick = { viewModel.logOut() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)), // Rojo descriptivo de salida
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Cerrar Sesión",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}