package com.ekezet.othello.main.di

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import com.ekezet.othello.main.ui.MainActivity
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named

internal val FinishableMainActivity: Qualifier?
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    inline get() = MainActivity::class.simpleName?.let { named(it) }
