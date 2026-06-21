package com.example.marketlens.components.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketlens.domain.repository.IAssetRepository
import com.example.marketlens.data.network.NetworkConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketScreenViewModel @Inject constructor(
    private val assetRepository: IAssetRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MarketScreenState())
    val uiState: StateFlow<MarketScreenState> = _uiState.asStateFlow()

    init {
        loadMarketData()
    }

    fun loadMarketData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val cryptoList = assetRepository.getCryptos()

                val stockList = assetRepository.getStocks(
                    tickers = "AAPL,MSFT,GOOGL,AMZN,TSLA,NVDA,META,AVGO,ORCL,ADBE,CRM,AMD,NFLX," +
                            "CSCO,INTC,TXN,QCOM,IBM,JPM,BAC,WFC,GS,MS,V,MA,PYPL,WMT,COST,PG,KO," +
                            "PEP,NKE,MCD,DIS,SBUX,XOM,CVX,CAT,GE,HON,BA,JNJ,LLY,UNH,PFE,ABBV,MRK," +
                            "GLOB,VZ,UPS",
                    token = NetworkConfig.TIINGO_KEY
                )

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    cryptos = cryptoList,
                    stocks = stockList
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al cargar activos"
                )
            }
        }
    }

    // Busqueda con la barra
    fun onSearchQueryChanged(newQuery: String) {
        _uiState.value = _uiState.value.copy(searchQuery = newQuery)
    }
}