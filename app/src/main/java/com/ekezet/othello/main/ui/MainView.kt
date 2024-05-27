package com.ekezet.othello.main.ui

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
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
import com.ekezet.othello.feature.gameboard.ui.GameBoardView
import com.ekezet.othello.feature.gamehistory.ui.GameHistoryView
import com.ekezet.othello.feature.gamesettings.ui.GameSettingsView
import com.ekezet.othello.main.MainArgs
import com.ekezet.othello.main.MainLoop
import com.ekezet.othello.main.MainState
import com.ekezet.othello.main.navigation.MainRoutes
import com.ekezet.othello.main.navigation.MainRoutes.GameBoardRoute
import com.ekezet.othello.main.navigation.MainRoutes.GameHistoryRoute
import com.ekezet.othello.main.navigation.MainRoutes.GameSettingsRoute
import com.ekezet.othello.main.navigation.stripRoute
import com.ekezet.othello.main.ui.components.MainTopAppBar
import com.ekezet.othello.main.ui.components.navigationActions
import kotlinx.coroutines.CoroutineScope
import org.koin.compose.koinInject

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun MainView(
    parentScope: CoroutineScope = rememberCoroutineScope(),
) {
    val gameSettingsStore: GameSettingsStore = koinInject()
    val gameSettings: GameSettings by gameSettingsStore.settings.collectAsState()

    val gameHistoryStore: GameHistoryStore = koinInject()
    val gameHistory: GameHistory by gameHistoryStore.history.collectAsState()

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
        )
    }
}

@ExperimentalLayoutApi
@Composable
internal fun MainState.MainViewImpl(
    gameSettings: GameSettings,
    gameHistory: GameHistory,
    startDestination: String = MainRoutes.Start,
) {
    val viewModelStoreOwner = requireNotNull(LocalViewModelStoreOwner.current) {
        "ViewModelStoreOwner must be provided"
    }

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
                inclusive = true,
            )
            .build()
    }

    fun navigateTo(route: String) {
        navController.navigate(
            route,
            navOptions.takeUnless { currentDestination == startDestination },
        )
    }

    val destinationModifier = Modifier.fillMaxSize()

    val historyListState = rememberLazyListState()

    Scaffold(
        topBar = {
            MainTopAppBar(currentDestination, navController, gameSettings)
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
            modifier = Modifier.padding(innerPadding),
        ) {
            NavHost(
                navController = navController,
                startDestination = startDestination,
            ) {
                composable(
                    route = GameBoardRoute.spec,
                ) {
                    CompositionLocalProvider(
                        LocalViewModelStoreOwner provides viewModelStoreOwner,
                    ) {
                        GameBoardView(
                            args = gameSettings,
                            parentEmitter = LocalActionEmitter.current,
                            onStrategyClick = { disk ->
                                navigateTo(GameSettingsRoute.make(disk))
                            },
                            modifier = destinationModifier,
                        )
                    }
                }

                composable(
                    route = GameHistoryRoute.spec,
                ) {
                    CompositionLocalProvider(
                        LocalViewModelStoreOwner provides viewModelStoreOwner,
                    ) {
                        GameHistoryView(
                            args = gameHistory,
                            listState = historyListState,
                            modifier = destinationModifier,
                        )
                    }
                }

                composable(
                    route = GameSettingsRoute.spec,
                    arguments = GameSettingsRoute.arguments,
                ) { entry ->
                    CompositionLocalProvider(
                        LocalViewModelStoreOwner provides viewModelStoreOwner,
                    ) {
                        GameSettingsView(
                            args = gameSettings,
                            selectStrategyFor = GameSettingsRoute.findPickStrategy(entry),
                            modifier = destinationModifier,
                        )
                    }
                }
            }
        }
    }
}
