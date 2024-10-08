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
import com.ekezet.hurok.compose.LoopWrapper
import com.ekezet.othello.core.game.GameHistory
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.game.store.GameHistoryStore
import com.ekezet.othello.core.game.store.GameSettingsStore
import com.ekezet.othello.core.ui.render.MovesRenderer
import com.ekezet.othello.feature.gameboard.ui.GameBoardView
import com.ekezet.othello.feature.gamehistory.GameHistoryArgs
import com.ekezet.othello.feature.gamehistory.ui.GameHistoryView
import com.ekezet.othello.feature.gamesettings.ui.GameSettingsView
import com.ekezet.othello.main.MainArgs
import com.ekezet.othello.main.MainLoop
import com.ekezet.othello.main.MainState
import com.ekezet.othello.main.OnBackPressed
import com.ekezet.othello.main.navigation.MainRoutes
import com.ekezet.othello.main.navigation.MainRoutes.GameBoardRoute
import com.ekezet.othello.main.navigation.MainRoutes.GameHistoryRoute
import com.ekezet.othello.main.navigation.MainRoutes.GameSettingsRoute
import com.ekezet.othello.main.navigation.stripRoute
import com.ekezet.othello.main.ui.components.ExitConfirmationSnackbar
import com.ekezet.othello.main.ui.components.MainTopAppBar
import com.ekezet.othello.main.ui.components.navigationActions
import kotlinx.coroutines.CoroutineScope
import org.koin.compose.koinInject

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun MainView(
    parentScope: CoroutineScope = rememberCoroutineScope(),
    gameSettingsStore: GameSettingsStore = koinInject(),
    gameHistoryStore: GameHistoryStore = koinInject(),
    historyRenderer: MovesRenderer = koinInject(),
) {
    val gameSettings: GameSettings by gameSettingsStore.settings.collectAsState()
    val gameHistory: GameHistory by gameHistoryStore.history.collectAsState()
    val historyImages by historyRenderer.renderedImages.collectAsState()

    LoopWrapper(
        builder = MainLoop,
        args = MainArgs(
            gameSettings = gameSettings,
            hasGameHistory = gameHistory.history.isNotEmpty(),
        ),
        parentScope = parentScope,
    ) {
        MainViewImpl(
            gameSettings = gameSettings,
            gameHistory = gameHistory,
            historyImages = historyImages,
        )
    }
}

@ExperimentalLayoutApi
@Composable
internal fun MainState.MainViewImpl(
    gameSettings: GameSettings,
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
                saveState = true,
            )
            .setLaunchSingleTop(true)
            .setRestoreState(true)
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

    ExitConfirmationSnackbar(hostState = snackbarHostState, settings = gameSettings)

    Scaffold(
        topBar = {
            MainTopAppBar(currentDestination, navController, gameSettings, gameHistory)
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                navigationActions(
                    currentDestination = currentDestination,
                    onClick = ::navigateTo,
                    hasGameHistory = hasGameHistory,
                )
            },
            modifier = Modifier.padding(innerPadding),
        ) {
            NavHost(
                navController = navController,
                startDestination = startDestination,
            ) {
                composable(
                    route = GameBoardRoute.spec,
                ) {
                    GameBoardView(
                        args = gameSettings,
                        parentEmitter = LocalActionEmitter.current,
                        onStrategyClick = { disk ->
                            navigateTo(GameSettingsRoute.make(pickStrategyFor = disk))
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
                        ),
                        listState = historyListState,
                        modifier = destinationModifier,
                    )
                }

                composable(
                    route = GameSettingsRoute.spec,
                    arguments = GameSettingsRoute.arguments,
                ) { entry ->
                    GameSettingsView(
                        args = gameSettings,
                        selectStrategyFor = GameSettingsRoute.findPickStrategy(entry),
                        modifier = destinationModifier,
                        listState = settingsListState,
                    )
                }
            }
        }
    }
}
