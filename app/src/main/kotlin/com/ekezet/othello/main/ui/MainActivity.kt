package com.ekezet.othello.main.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.ReportDrawn
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.CompositionLocalProvider
import com.ekezet.othello.App
import com.ekezet.othello.core.ui.navigation.Finishable
import com.ekezet.othello.core.ui.theme.OthelloTheme
import com.ekezet.othello.main.di.LocalGraph
import com.ekezet.othello.main.di.MainGraph

@ExperimentalMaterial3WindowSizeClassApi
class MainActivity : ComponentActivity(), Finishable {
    private val mainGraph: MainGraph by lazy {
        (applicationContext as App).mainGraph
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            OthelloTheme {
                Surface {
                    CompositionLocalProvider(LocalGraph provides mainGraph) {
                        MainView()
                    }
                }
            }

            ReportDrawn()
        }
    }
}
