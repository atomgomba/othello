package com.ekezet.othello.main

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.ekezet.hurok.ActionEmitter
import com.ekezet.hurok.ViewState
import com.ekezet.othello.core.game.data.BoardDisplayOptions
import com.ekezet.othello.core.game.data.Default
import com.ekezet.othello.core.game.data.DefaultConfirmExit
import com.ekezet.othello.core.game.data.DefaultDarkStrategy
import com.ekezet.othello.core.game.data.DefaultLightStrategy
import com.ekezet.othello.core.game.data.HistoryDisplayOptions
import com.ekezet.othello.core.game.data.HistorySettings
import com.ekezet.othello.core.game.data.IAppSettings
import com.ekezet.othello.core.game.data.IGameSettings
import com.ekezet.othello.core.game.data.IHistorySettings
import com.ekezet.othello.core.game.dependency.GameSettingsPublisher
import com.ekezet.othello.core.game.dependency.HistorySettingsPublisher
import com.ekezet.othello.core.game.store.GameHistoryStore
import com.ekezet.othello.core.game.store.GameSettingsStore
import com.ekezet.othello.core.game.store.HistorySettingsStore
import com.ekezet.othello.core.game.strategy.Strategy
import com.ekezet.othello.core.ui.navigation.Finishable
import com.ekezet.othello.feature.gameboard.GameBoardEmitter
import com.ekezet.othello.feature.gamehistory.GameHistoryEmitter
import com.ekezet.othello.main.di.FinishableMainActivity
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal data class MainModel(
    override val darkStrategy: Strategy? = DefaultDarkStrategy,
    override val lightStrategy: Strategy? = DefaultLightStrategy,
    override val boardDisplayOptions: BoardDisplayOptions = BoardDisplayOptions.Default,
    override val confirmExit: Boolean = DefaultConfirmExit,
    override val historyDisplayOptions: HistoryDisplayOptions = HistoryDisplayOptions.Default,
    override val showHistoryAsText: Boolean = HistorySettings.Default.showHistoryAsText,
    val hasGameHistory: Boolean = false,
    val backPressCount: Int = 0,
) : IGameSettings, IHistorySettings, IAppSettings {
    val isExitMessageVisible: Boolean
        inline get() = 0 < backPressCount
}

@Immutable
internal data class MainState(
    val hasGameHistory: Boolean,
    val isExitMessageVisible: Boolean,
) : ViewState<MainModel, MainDependency>()

@Stable
internal data class MainArgs(
    val gameSettings: IGameSettings,
    val hasGameHistory: Boolean,
)

internal class MainDependency(
    gameSettingsStore: GameSettingsStore? = null,
    gameHistoryStore: GameHistoryStore? = null,
    historySettingsStore: HistorySettingsStore? = null,
    activity: Finishable? = null,
) : KoinComponent, GameSettingsPublisher, HistorySettingsPublisher {
    override val gameSettingsStore: GameSettingsStore = gameSettingsStore ?: get()
    override val gameHistoryStore: GameHistoryStore = gameHistoryStore ?: get()
    override val historySettingsStore: HistorySettingsStore = historySettingsStore ?: get()
    val activity: Finishable = activity ?: get(FinishableMainActivity)

    var gameBoardEmitter: GameBoardEmitter? = null
    var gameHistoryEmitter: GameHistoryEmitter? = null
}

internal typealias MainActionEmitter = ActionEmitter<MainModel, MainDependency>
