package com.example.marketlens.components.profile

import androidx.lifecycle.ViewModel
<<<<<<< Updated upstream
=======
import androidx.lifecycle.viewModelScope
import com.example.marketlens.data.store.SessionDataStore
import com.example.marketlens.domain.repository.IAssetRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
>>>>>>> Stashed changes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

<<<<<<< Updated upstream
class ProfileScreenViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileScreenState())
    val uiState: StateFlow<ProfileScreenState> = _uiState.asStateFlow()

    fun logOut() {
        // TODO: Firebase
=======
@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val assetRepository: IAssetRepository,
    private val firebaseAuth: FirebaseAuth,
    private val sessionDataStore: SessionDataStore
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileScreenState())
    val uiState: StateFlow<ProfileScreenState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        val currentUser = firebaseAuth.currentUser
        val displayName = currentUser?.displayName ?: currentUser?.email ?: "Usuario"

        viewModelScope.launch {
            assetRepository.getFavoritesStream().collect { favorites ->
                _uiState.value = _uiState.value.copy(
                    userName = displayName,
                    favoriteAssets = favorites.map { it.ticker }
                )
            }
        }

        viewModelScope.launch {
            sessionDataStore.isBiometricEnabled.collect { enabled ->
                _uiState.value = _uiState.value.copy(
                    isBiometricEnabled = enabled
                )
            }
        }
    }

    fun setBiometricEnabled(enabled: Boolean) {
        viewModelScope.launch {
            sessionDataStore.setBiometricEnabled(enabled)
        }
    }

    fun logOut(onSuccess: () -> Unit) {
        firebaseAuth.signOut()
        onSuccess()
>>>>>>> Stashed changes
    }
}