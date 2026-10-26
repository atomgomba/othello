package com.ekezet.othello.core.game.di

import android.content.Context
import com.ekezet.othello.core.game.store.AppSettingsStore
import com.ekezet.othello.core.game.store.GameHistoryStore
import com.ekezet.othello.core.game.store.GameSettingsStore
import com.ekezet.othello.core.game.store.HistorySettingsStore
import com.ekezet.othello.core.game.store.SharedPreferencesAppSettingsStore
import com.ekezet.othello.core.game.store.SharedPreferencesGameSettingsStore
import com.ekezet.othello.core.game.store.SharedPreferencesHistorySettingsStore
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@BindingContainer
object GameCoreBindings {
    @Provides
    @SingleIn(AppScope::class)
    fun provideGameSettingsStore(context: Context): GameSettingsStore =
        SharedPreferencesGameSettingsStore(context = context)

    @Provides
    @SingleIn(AppScope::class)
    fun provideHistorySettingsStore(context: Context): HistorySettingsStore =
        SharedPreferencesHistorySettingsStore(context = context)

    @Provides
    @SingleIn(AppScope::class)
    fun provideAppSettingsStore(context: Context): AppSettingsStore =
        SharedPreferencesAppSettingsStore(context = context)

    @Provides
    @SingleIn(AppScope::class)
    fun provideGameHistoryStore(): GameHistoryStore =
        GameHistoryStore()
}
