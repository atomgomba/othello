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
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ekezet.hurok.compose.LocalActionEmitter
import com.ekezet.hurok.compose.LoopView
import com.ekezet.othello.core.game.GameHistory
import com.ekezet.othello.core.game.data.AppSettings
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.game.data.HistorySettings
import com.ekezet.othello.core.ui.navigation.Finishable
import com.ekezet.othello.feature.gameboard.GameBoardArgs
import com.ekezet.othello.feature.gameboard.GameBoardDependency
import com.ekezet.othello.feature.gameboard.ui.GameBoardView
import com.ekezet.othello.feature.gamehistory.GameHistoryArgs
import com.ekezet.othello.feature.gamehistory.ui.GameHistoryView
import com.ekezet.othello.feature.settings.SettingsArgs
import com.ekezet.othello.feature.settings.SettingsDependency
import com.ekezet.othello.feature.settings.ui.SettingsView
import com.ekezet.othello.main.MainAction
import com.ekezet.othello.main.MainArgs
import com.ekezet.othello.main.MainDependency
import com.ekezet.othello.main.MainLoop
import com.ekezet.othello.main.MainState
import com.ekezet.othello.main.OnBackPressed
import com.ekezet.othello.main.OnNewGameClicked
import com.ekezet.othello.main.OnToggleIndicatorsClicked
import com.ekezet.othello.main.di.MainGraph
import com.ekezet.othello.main.di.requireMainGraph
import com.ekezet.othello.main.navigation.Screen
import com.ekezet.othello.main.ui.components.ExitConfirmationSnackbar
import com.ekezet.othello.main.ui.components.MainTopAppBar
import com.ekezet.othello.main.ui.components.navigationActions
import kotlinx.coroutines.CoroutineScope
import kotlin.reflect.KClass

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun MainView(
    mainGraph: MainGraph = requireMainGraph(),
    scope: CoroutineScope = rememberCoroutineScope(),
) {
    val gameSettings: GameSettings by mainGraph.gameSettingsStore.settings.collectAsState()
    val historySettings: HistorySettings by mainGraph.historySettingsStore.settings.collectAsState()
    val appSettings: AppSettings by mainGraph.appSettingsStore.settings.collectAsState()
    val gameHistory: GameHistory by mainGraph.gameHistoryStore.history.collectAsState()
    val historyImages by mainGraph.movesRenderer.renderedImages.collectAsState()

    LoopView(
        builder = MainLoop(
            dependency = MainDependency(
                gameSettingsStore = mainGraph.gameSettingsStore,
                gameHistoryStore = mainGraph.gameHistoryStore,
                activity = LocalContext.current as Finishable,
            ),
        ),
        args = MainArgs(
            gameSettings = gameSettings,
            hasGameHistory = gameHistory.history.isNotEmpty(),
        ),
        scope = scope,
    ) { emit ->
        MainViewImpl(
            emit = emit,
            gameSettings = gameSettings,
            historySettings = historySettings,
            appSettings = appSettings,
            gameHistory = gameHistory,
            historyImages = historyImages,
            gameBoardDependency = GameBoardDependency(
                gameHistoryStore = mainGraph.gameHistoryStore,
                movesRenderer = mainGraph.movesRenderer,
            ),
            settingsDependency = SettingsDependency(
                gameSettingsStore = mainGraph.gameSettingsStore,
                historySettingsStore = mainGraph.historySettingsStore,
                appSettingsStore = mainGraph.appSettingsStore,
                gameHistoryStore = mainGraph.gameHistoryStore,
            ),
        )
    }
}

@ExperimentalLayoutApi
@Composable
internal fun MainState.MainViewImpl(
    emit: (action: MainAction) -> Unit,
    gameSettings: GameSettings,
    historySettings: HistorySettings,
    appSettings: AppSettings,
    gameHistory: GameHistory,
    historyImages: Map<String, ImageBitmap>,
    gameBoardDependency: GameBoardDependency,
    settingsDependency: SettingsDependency,
    startDestination: Screen = Screen.Start,
) {
    val navController: NavHostController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestinationClass: KClass<out Screen> by remember(startDestination) {
        derivedStateOf {
            Screen.classOf(currentBackStackEntry?.destination?.route) ?: startDestination::class
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

    fun navigateTo(screen: Screen) {
        navController.navigate(route = screen, navOptions = navOptions)
    }

    val destinationModifier = Modifier.fillMaxSize()

    val historyListState = rememberLazyListState()
    val settingsListState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }

    BackHandler {
        emit(OnBackPressed)
    }

    ExitConfirmationSnackbar(
        emit = emit,
        hostState = snackbarHostState,
        confirmExit = appSettings.confirmExit,
    )

    Scaffold(
        topBar = {
            MainTopAppBar(
                onRefreshClick = { emit(OnNewGameClicked) },
                onShowPossibleMovesClick = { emit(OnToggleIndicatorsClicked) },
                currentDestinationClass = currentDestinationClass,
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
                    currentDestinationClass = currentDestinationClass,
                    onClick = ::navigateTo,
                    hasGameHistory = hasGameHistory,
                )
            },
        ) {
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable<Screen.GameBoard> { entry ->
                    val route: Screen.GameBoard = entry.toRoute()

                    GameBoardView(
                        args = GameBoardArgs(
                            selectedTurn = route.showTurn,
                            boardDisplayOptions = gameSettings.boardDisplayOptions,
                            lightStrategy = gameSettings.lightStrategy,
                            darkStrategy = gameSettings.darkStrategy,
                        ),
                        dependency = gameBoardDependency,
                        parentEmitter = checkNotNull(LocalActionEmitter.current) {
                            "parentEmitter needs to be set to emit GameBoard actions from the parent"
                        },
                        onStrategyClick = { disk ->
                            navigateTo(Screen.Settings(pickStrategyFor = "$disk"))
                        },
                        modifier = destinationModifier,
                    )
                }

                composable<Screen.GameHistory> {
                    if (!hasGameHistory) {
                        navController.popBackStack()
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
                        onTurnClick = { turn -> navigateTo(Screen.GameBoard(showTurn = turn)) },
                        onCurrentTurnClick = {
                            navigateTo(Screen.GameBoard(showTurn = gameHistory.history.size))
                        },
                        modifier = destinationModifier,
                    )
                }

                composable<Screen.Settings> { entry ->
                    val route: Screen.Settings = entry.toRoute()

                    SettingsView(
                        args = SettingsArgs(
                            gameSettings = gameSettings,
                            historySettings = historySettings,
                            appSettings = appSettings,
                            selectingStrategyFor = route.pickStrategyForDisk,
                        ),
                        dependency = settingsDependency,
                        modifier = destinationModifier,
                        listState = settingsListState,
                        afterStrategySelected = route.pickStrategyFor?.let {
                            { navigateTo(Screen.GameBoard()) }
                        },
                    )
                }
            }
        }
    }
}
