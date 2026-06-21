package com.example.marketlens.components.splash

import androidx.lifecycle.ViewModel
import com.example.marketlens.data.store.SessionDataStore
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val sessionDataStore: SessionDataStore,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    val isUserLoggedIn: Boolean
        get() = firebaseAuth.currentUser != null

    suspend fun isBiometricEnabled(): Boolean {
        return sessionDataStore.isBiometricEnabled.first()
    }
}
