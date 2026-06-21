package com.example.marketlens.components.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.marketlens.components.Screen

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    vm: LoginScreenViewModel = hiltViewModel(),
    onGoogleLoginClick: () -> Unit
) {
    val marketGreen = Color(0xFF00C853)
    val darkCardColor = Color(0xFF161616)

    LaunchedEffect(Unit) {
        vm.uiEvent.collect { event ->
            if (event == "loginOK") {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Splash.route) {
                        inclusive = true
                    }
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF00150A),
                        Color.Black,
                        Color.Black
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header: Branding
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 48.dp)
            ) {
                AsyncImage(
                    model = "https://cdn-icons-png.flaticon.com/512/2422/2422796.png",
                    contentDescription = "Logo MarketLens",
                    modifier = Modifier.size(120.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "MarketLens",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = marketGreen
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tu ventana al mercado global",
                    fontSize = 14.sp,
                    color = Color.LightGray,
                    fontWeight = FontWeight.Medium
                )
            }

            // Central: Glassmorphism-style Login Card
            Card(
                colors = CardDefaults.cardColors(containerColor = darkCardColor.copy(alpha = 0.8f)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Bienvenido",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Monitorea acciones, criptomonedas y noticias en tiempo real con análisis inteligente de IA.",
                        fontSize = 13.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(28.dp))

                    // Styled Google Login Button
                    Button(
                        onClick = onGoogleLoginClick,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "G",
                                color = Color(0xFF4285F4),
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Iniciar sesión con Google",
                                color = Color.Black,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }

            // Footer: Copyright
            Text(
                text = "MarketLens © 2026 • Todos los derechos reservados",
                fontSize = 11.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}