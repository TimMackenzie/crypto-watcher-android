package com.simplifynow.cryptowatcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import com.simplifynow.cryptowatcher.ui.AppBar
import com.simplifynow.cryptowatcher.ui.ExpandableFab
import com.simplifynow.cryptowatcher.ui.MainContent
import com.simplifynow.cryptowatcher.ui.theme.CryptowatcherandroidTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * This is the only activity in this app, consisting of an app bar, FAB, and main content section
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CryptowatcherandroidTheme {
                Scaffold(
                    topBar = { AppBar() },
                    floatingActionButton = { ExpandableFab() },
                    content = { padding ->
                        MainContent(padding)
                    }
                )
            }
        }
    }
}
