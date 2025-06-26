package com.ekezet.othello.main.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.ReportDrawn
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import com.ekezet.othello.core.ui.navigation.Finishable
import com.ekezet.othello.core.ui.theme.OthelloTheme
import com.ekezet.othello.main.di.FinishableMainActivity
import org.koin.java.KoinJavaComponent.getKoin

@ExperimentalMaterial3WindowSizeClassApi
class MainActivity : ComponentActivity(), Finishable {
    override fun onCreate(savedInstanceState: Bundle?) {
        getKoin().declare<Finishable>(
            instance = this,
            qualifier = FinishableMainActivity,
        )

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            OthelloTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                ) {
                    MainView()
                }
            }

            ReportDrawn()
        }
    }
}
