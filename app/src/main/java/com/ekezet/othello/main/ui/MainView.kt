package com.ekezet.othello.main.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ekezet.hurok.compose.LocalActionEmitter
import com.ekezet.hurok.compose.LoopWrapper
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.game.store.GameSettingsStore
import com.ekezet.othello.feature.gameboard.ui.GameBoardView
import com.ekezet.othello.feature.gamesettings.ui.GameSettingsView
import com.ekezet.othello.main.MainLoop
import com.ekezet.othello.main.MainState
import com.ekezet.othello.main.navigation.MainRoutes
import com.ekezet.othello.main.navigation.MainRoutes.GameBoardRoute
import com.ekezet.othello.main.navigation.MainRoutes.GameSettingsRoute
import com.ekezet.othello.main.navigation.stripRoute
import com.ekezet.othello.main.ui.components.MainTopAppBar
import com.ekezet.othello.main.ui.components.NavigationActions
import kotlinx.coroutines.CoroutineScope
import org.koin.compose.koinInject

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun MainView(
    windowSizeClass: WindowSizeClass,
    parentScope: CoroutineScope = rememberCoroutineScope(),
) {
    val gameSettingsStore: GameSettingsStore = koinInject()
    val gameSettings: GameSettings by gameSettingsStore.settings.collectAsState()

    LoopWrapper(
        builder = MainLoop,
        args = gameSettings,
        parentScope = parentScope,
    ) {
        MainViewImpl(
            gameSettings = gameSettings,
            windowSizeClass = windowSizeClass,
        )
    }
}

@ExperimentalLayoutApi
@Composable
internal fun MainState.MainViewImpl(
    gameSettings: GameSettings,
    windowSizeClass: WindowSizeClass,
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

    val shouldShowBottomBar = with(windowSizeClass) {
        widthSizeClass == WindowWidthSizeClass.Compact || heightSizeClass == WindowHeightSizeClass.Compact
    }
    val shouldShowNavRail = !shouldShowBottomBar
    val destinationModifier = Modifier.fillMaxSize()

    Scaffold(
        topBar = {
            MainTopAppBar(currentDestination, navController, gameSettings)
        },
        bottomBar = {
            if (shouldShowBottomBar) {
                BottomAppBar { NavigationActions(currentDestination, ::navigateTo) }
            }
        },
    ) { innerPadding ->
        Row(
            modifier = Modifier.padding(innerPadding),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (shouldShowNavRail) {
                NavigationRail { NavigationActions(currentDestination, ::navigateTo) }
            }

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
