package com.example.marketlens.components.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.marketlens.components.Screen
import coil.compose.AsyncImage
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val marketGreen = Color(0xFF00C853) // El verde característico de tu propuesta

    // COPIA EXACTA DE LA LÓGICA DEL PROFE: Espera 2 segundos y navega al Home
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate(Screen.Home.route) {
            // El profe lo dejó comentado, pero si querés evitar que el usuario vuelva
            // al splash al apretar el botón "atrás", podés descomentar estas líneas:
            popUpTo(Screen.Splash.route) {
                inclusive = true
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black), // Forzamos el Negro Puro de MarketLens
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen central / Logo financiero (Podés cambiar la URL por cualquier icon posterior)
            AsyncImage(
                model = "https://cdn-icons-png.flaticon.com/512/2422/2422796.png", // Icono analítico/lente
                contentDescription = "Logo MarketLens",
                modifier = Modifier
                    .size(200.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Título de tu aplicación
            Text(
                text = "MarketLens",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                ),
                color = marketGreen // Resaltado verde financiero
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Eslogan alineado con tu propuesta de valor del TPO
            Text(
                text = "Tu ventana al mercado global",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.LightGray
            )
        }
    }
}