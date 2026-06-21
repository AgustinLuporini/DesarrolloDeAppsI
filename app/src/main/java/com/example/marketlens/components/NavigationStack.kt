package com.example.marketlens.components

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
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
    val sharedHomeViewModel: HomeScreenViewModel = hiltViewModel()

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

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val googleSignInClient = GoogleSignIn.getClient(context, gso)

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    try {
                        val account = task.result
                        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                        FirebaseAuth.getInstance().signInWithCredential(credential)
                            .addOnCompleteListener { authTask ->
                                if (authTask.isSuccessful) {
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
            route = "asset_detail_screen/{ticker}/{name}/{price}/{change}/{isCrypto}"
        ) { backStackEntry ->
            val ticker = backStackEntry.arguments?.getString("ticker") ?: ""
            val encodedName = backStackEntry.arguments?.getString("name") ?: ""
            val name = java.net.URLDecoder.decode(encodedName, "UTF-8")
            val price = backStackEntry.arguments?.getString("price")?.toDoubleOrNull() ?: 0.0
            val change = backStackEntry.arguments?.getString("change")?.toDoubleOrNull() ?: 0.0

            val isCrypto = backStackEntry.arguments?.getString("isCrypto")?.toBoolean() ?: false

            AssetDetailScreen(
                ticker = ticker,
                name = name,
                price = price,
                change = change,
                isCrypto = isCrypto
            )
        }
    }
}