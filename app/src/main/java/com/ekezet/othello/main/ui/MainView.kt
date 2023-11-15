package com.ekezet.othello.main.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ekezet.hurok.AnyActionEmitter
import com.ekezet.hurok.compose.LoopWrapper
import com.ekezet.othello.R.string
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.valueOf
import com.ekezet.othello.core.game.data.BoardDisplayOptions
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.game.store.GameSettingsStore
import com.ekezet.othello.core.ui.R
import com.ekezet.othello.feature.gameboard.ui.GameBoardView
import com.ekezet.othello.feature.gamesettings.ui.GameSettingsView
import com.ekezet.othello.main.MainLoop
import com.ekezet.othello.main.MainState
import com.ekezet.othello.main.navigation.MainRoutes
import com.ekezet.othello.main.navigation.MainRoutes.GameBoardRoute
import com.ekezet.othello.main.navigation.MainRoutes.GameSettingsRoute
import com.ekezet.othello.main.navigation.MainRoutes.GameSettingsRoute.PickStrategy
import com.ekezet.othello.main.navigation.stripRoute
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
    ) { mainEmitter ->
        MainViewImpl(
            gameSettings = gameSettings,
            windowSizeClass = windowSizeClass,
            mainEmitter = mainEmitter,
        )
    }
}

@ExperimentalLayoutApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MainState.MainViewImpl(
    gameSettings: GameSettings,
    windowSizeClass: WindowSizeClass,
    mainEmitter: AnyActionEmitter? = null,
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
            route, navOptions.takeUnless { currentDestination == startDestination })
    }

    val shouldShowBottomBar = with(windowSizeClass) {
        widthSizeClass == WindowWidthSizeClass.Compact || heightSizeClass == WindowHeightSizeClass.Compact
    }
    val shouldShowNavRail = !shouldShowBottomBar
    val destinationModifier = Modifier.fillMaxSize()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text(stringResource(string.app_name)) },
                navigationIcon = {
                    AnimatedVisibility(
                        visible = currentDestination == GameSettingsRoute.id,
                    ) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = null,
                            )
                        }
                    }
                },
                actions = {
                    AnimatedVisibility(
                        visible = currentDestination == GameBoardRoute.id,
                    ) {
                        GameBoardToolbarActions(gameSettings.displayOptions)
                    }
                },
            )
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
                        LocalViewModelStoreOwner provides viewModelStoreOwner
                    ) {
                        GameBoardView(
                            args = gameSettings,
                            parentEmitter = mainEmitter,
                            onStrategyClick = { disk ->
                                navigateTo(
                                    GameSettingsRoute.make(mapOf(PickStrategy to "$disk")),
                                )
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
                        LocalViewModelStoreOwner provides viewModelStoreOwner
                    ) {
                        GameSettingsView(
                            args = gameSettings,
                            selectStrategyFor = Disk.valueOf(
                                entry.arguments?.getString(PickStrategy),
                            ),
                            modifier = destinationModifier,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MainState.GameBoardToolbarActions(
    options: BoardDisplayOptions,
) = with(options) {
    Row {
        IconButton(onClick = actions.onNewGameClick) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = stringResource(R.string.main__menu__new_game),
            )
        }

        IconToggleButton(
            checked = showPossibleMoves,
            onCheckedChange = { actions.onToggleIndicatorsClick() },
        ) {
            Icon(
                painter = painterResource(
                    id = if (showPossibleMoves) R.drawable.ic_visibility else R.drawable.ic_visibility_off,
                ),
                contentDescription = stringResource(R.string.main__menu__toggle_indicators),
            )
        }
    }
}

@Composable
private fun NavigationActions(currentDestination: String, onClick: (String) -> Unit) {
    MainRoutes.All.forEach { destination ->
        IconToggleButton(
            checked = currentDestination == destination.id,
            onCheckedChange = { onClick(destination.id) },
        ) {
            Icon(
                imageVector = destination.icon,
                contentDescription = null,
            )
        }
    }
}
