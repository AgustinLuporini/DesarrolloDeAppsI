package com.example.marketlens.components

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.marketlens.R
import com.example.marketlens.components.detail.AssetDetailScreen
import com.example.marketlens.components.home.HomeScreen
import com.example.marketlens.components.home.HomeScreenViewModel
import com.example.marketlens.components.login.LoginScreen
import com.example.marketlens.components.market.MarketScreen
import com.example.marketlens.components.splash.SplashScreen
import com.example.marketlens.components.profile.ProfileScreen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


@Composable
fun NavigationStack() {
    val navController = rememberNavController()
    val sharedHomeViewModel: HomeScreenViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // Splash
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        // Login
        composable(Screen.Login.route) {
            val context = LocalContext.current

            //Google Sign-In
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id)) // Clave de Firebase
                .requestEmail()
                .build()
            val googleSignInClient = GoogleSignIn.getClient(context, gso)

            //Redirigir a Google
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    try {
                        val account = task.result
                        // Pedir a firebase que inicie sesión
                        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                        FirebaseAuth.getInstance().signInWithCredential(credential)
                            .addOnCompleteListener { authTask ->
                                if (authTask.isSuccessful) {
                                    // Si todo sale bien, navegamos al Home
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Login.route) { inclusive = true }
                                    }
                                }
                            }
                    } catch (e: Exception) {
                        android.util.Log.e("Auth", "Fallo Google Sign In: ${e.message}")
                    }
                }
            }

            LoginScreen(
                navController = navController,
                onGoogleLoginClick = {
                    launcher.launch(googleSignInClient.signInIntent)
                }
            )
        }
        // Pantalla Principal
        composable(Screen.Home.route) {
            HomeScreen(navController = navController, viewModel = sharedHomeViewModel)
        }

        // Pantalla de Mercados
        composable(Screen.Market.route) {
            MarketScreen(navController)
        }

        // Perfil
        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }

        // Detail
        composable(
            route = "asset_detail_screen/{ticker}/{name}/{price}/{change}"
        ) { backStackEntry ->
            val ticker = backStackEntry.arguments?.getString("ticker") ?: ""
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val price = backStackEntry.arguments?.getString("price")?.toDoubleOrNull() ?: 0.0
            val change = backStackEntry.arguments?.getString("change")?.toDoubleOrNull() ?: 0.0

            AssetDetailScreen(ticker = ticker, name = name, price = price, change = change)
        }
    }
}