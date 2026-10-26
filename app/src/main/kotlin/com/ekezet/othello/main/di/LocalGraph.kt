package com.ekezet.othello.main.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf

val LocalGraph = compositionLocalOf<MainGraph?> { null }

@Suppress("NOTHING_TO_INLINE")
@Composable
inline fun requireMainGraph(): MainGraph = checkNotNull(LocalGraph.current) { "MainGraph must be provided" }
