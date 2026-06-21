package com.example.marketlens

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import com.example.marketlens.components.NavigationStack
import com.example.marketlens.ui.theme.MarketLensTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarketLensTheme {
                NavigationStack()
            }
        }
    }
}