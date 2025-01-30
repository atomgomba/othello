package com.ekezet.othello.main.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ekezet.hurok.compose.LocalActionEmitter
import com.ekezet.hurok.compose.LoopView
import com.ekezet.othello.core.game.GameHistory
import com.ekezet.othello.core.game.data.AppSettings
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.game.data.HistorySettings
import com.ekezet.othello.core.game.store.AppSettingsStore
import com.ekezet.othello.core.game.store.GameHistoryStore
import com.ekezet.othello.core.game.store.GameSettingsStore
import com.ekezet.othello.core.game.store.HistorySettingsStore
import com.ekezet.othello.core.ui.render.MovesRenderer
import com.ekezet.othello.feature.gameboard.GameBoardArgs
import com.ekezet.othello.feature.gameboard.ui.GameBoardView
import com.ekezet.othello.feature.gamehistory.GameHistoryArgs
import com.ekezet.othello.feature.gamehistory.ui.GameHistoryView
import com.ekezet.othello.feature.settings.SettingsArgs
import com.ekezet.othello.feature.settings.ui.SettingsView
import com.ekezet.othello.main.MainArgs
import com.ekezet.othello.main.MainLoop
import com.ekezet.othello.main.MainState
import com.ekezet.othello.main.OnBackPressed
import com.ekezet.othello.main.navigation.MainRoutes
import com.ekezet.othello.main.navigation.MainRoutes.GameBoardRoute
import com.ekezet.othello.main.navigation.MainRoutes.GameHistoryRoute
import com.ekezet.othello.main.navigation.MainRoutes.SettingsRoute
import com.ekezet.othello.main.navigation.stripRoute
import com.ekezet.othello.main.ui.components.ExitConfirmationSnackbar
import com.ekezet.othello.main.ui.components.MainTopAppBar
import com.ekezet.othello.main.ui.components.navigationActions
import kotlinx.coroutines.CoroutineScope
import org.koin.compose.koinInject

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun MainView(
    scope: CoroutineScope = rememberCoroutineScope(),
    gameSettingsStore: GameSettingsStore = koinInject(),
    historySettingsStore: HistorySettingsStore = koinInject(),
    appSettingsStore: AppSettingsStore = koinInject(),
    gameHistoryStore: GameHistoryStore = koinInject(),
    historyRenderer: MovesRenderer = koinInject(),
) {
    val gameSettings: GameSettings by gameSettingsStore.settings.collectAsState()
    val historySettings: HistorySettings by historySettingsStore.settings.collectAsState()
    val appSettings: AppSettings by appSettingsStore.settings.collectAsState()
    val gameHistory: GameHistory by gameHistoryStore.history.collectAsState()
    val historyImages by historyRenderer.renderedImages.collectAsState()

    LoopView(
        builder = MainLoop,
        args = MainArgs(
            gameSettings = gameSettings,
            hasGameHistory = gameHistory.history.isNotEmpty(),
        ),
        scope = scope,
    ) {
        MainViewImpl(
            gameSettings = gameSettings,
            historySettings = historySettings,
            appSettings = appSettings,
            gameHistory = gameHistory,
            historyImages = historyImages,
        )
    }
}

@ExperimentalLayoutApi
@Composable
internal fun MainState.MainViewImpl(
    gameSettings: GameSettings,
    historySettings: HistorySettings,
    appSettings: AppSettings,
    gameHistory: GameHistory,
    historyImages: Map<String, ImageBitmap>,
    startDestination: String = MainRoutes.Start,
) {
    val navController: NavHostController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination by remember {
        derivedStateOf {
            currentBackStackEntry?.destination?.route?.stripRoute() ?: startDestination
        }
    }
    val navOptions = remember {
        NavOptions
            .Builder()
            .setPopUpTo(
                route = startDestination,
                inclusive = false,
            )
            .setLaunchSingleTop(true)
            .setRestoreState(false)
            .build()
    }

    fun navigateTo(route: String) {
        navController.navigate(route = route, navOptions = navOptions)
    }

    val destinationModifier = Modifier.fillMaxSize()

    val historyListState = rememberLazyListState()
    val settingsListState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }

    BackHandler {
        emit(OnBackPressed)
    }

    ExitConfirmationSnackbar(hostState = snackbarHostState, confirmExit = appSettings.confirmExit)

    Scaffold(
        topBar = {
            MainTopAppBar(
                currentDestination = currentDestination,
                navController = navController,
                showPossibleMoves = gameSettings.boardDisplayOptions.showPossibleMoves,
                gameHistorySize = gameHistory.history.size,
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { innerPadding ->
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                navigationActions(
                    currentDestination = currentDestination,
                    onClick = ::navigateTo,
                    hasGameHistory = hasGameHistory,
                )
            },
            modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
        ) {
            NavHost(
                navController = navController,
                startDestination = startDestination,
            ) {
                composable(
                    route = GameBoardRoute.spec,
                    arguments = GameBoardRoute.arguments,
                ) { entry ->
                    GameBoardView(
                        args = GameBoardArgs(
                            selectedTurn = GameBoardRoute.findShowTurn(entry),
                            boardDisplayOptions = gameSettings.boardDisplayOptions,
                            lightStrategy = gameSettings.lightStrategy,
                            darkStrategy = gameSettings.darkStrategy,
                        ),
                        parentEmitter = LocalActionEmitter.current,
                        onStrategyClick = { disk ->
                            navigateTo(SettingsRoute.make(pickStrategyFor = disk))
                        },
                        modifier = destinationModifier,
                    )
                }

                composable(
                    route = GameHistoryRoute.spec,
                ) {
                    if (!hasGameHistory) {
                        navigateTo(MainRoutes.Start)
                        return@composable
                    }

                    GameHistoryView(
                        args = GameHistoryArgs(
                            history = gameHistory,
                            historyImages = historyImages,
                            gameSettings = gameSettings,
                            historySettings = historySettings,
                        ),
                        listState = historyListState,
                        onTurnClick = { turn -> navigateTo(GameBoardRoute.make(showTurn = turn)) },
                        onCurrentTurnClick = {
                            navigateTo(GameBoardRoute.make(showTurn = gameHistory.history.size))
                        },
                        modifier = destinationModifier,
                    )
                }

                composable(
                    route = SettingsRoute.spec,
                    arguments = SettingsRoute.arguments,
                ) { entry ->
                    SettingsView(
                        args = SettingsArgs(
                            gameSettings = gameSettings,
                            historySettings = historySettings,
                            appSettings = appSettings,
                        ),
                        selectStrategyFor = SettingsRoute.findPickStrategy(entry),
                        modifier = destinationModifier,
                        listState = settingsListState,
                    )
                }
            }
        }
    }
}
