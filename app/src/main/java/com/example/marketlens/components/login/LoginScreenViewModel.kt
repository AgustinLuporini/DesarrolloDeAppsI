package com.example.marketlens.components.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketlens.domain.repository.IAssetRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val assetRepository: IAssetRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiEvent = Channel<String>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        checkAuthStatus()
    }

    // Verificar si hay sesión activa
    fun checkAuthStatus() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            viewModelScope.launch {
                try {
                    assetRepository.syncUserSession(currentUser)
                } catch (e: Exception) {
                    android.util.Log.e("LoginViewModel", "Error sincronizando sesión: ${e.message}")
                }
                _uiEvent.send("loginOK")
            }
        }
    }

    fun onLoginSuccess(user: FirebaseUser, onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                assetRepository.syncUserSession(user)
            } catch (e: Exception) {
                android.util.Log.e("LoginViewModel", "Error al sincronizar tras login: ${e.message}")
            }
            onComplete()
        }
    }
}