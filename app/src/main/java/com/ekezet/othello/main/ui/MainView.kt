package com.ekezet.othello.main.ui

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
import com.ekezet.othello.main.navigation.MainDestinations
import com.ekezet.othello.main.navigation.MainDestinations.GameBoardDestination
import com.ekezet.othello.main.navigation.MainDestinations.GameSettingsDestination
import com.ekezet.othello.main.navigation.MainDestinations.GameSettingsDestination.PickStrategy
import kotlinx.coroutines.CoroutineScope
import org.koin.compose.koinInject

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun MainView(
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
            mainEmitter = mainEmitter,
        )
    }
}

@ExperimentalLayoutApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MainState.MainViewImpl(
    gameSettings: GameSettings,
    mainEmitter: AnyActionEmitter? = null,
    navController: NavHostController = rememberNavController(),
    startDestination: String = MainDestinations.Start,
) {
    val viewModelStoreOwner = requireNotNull(LocalViewModelStoreOwner.current) {
        "ViewModelStoreOwner must be provided"
    }
    val destinationModifier = Modifier.fillMaxSize()
    var currentDestination: String by remember { mutableStateOf(startDestination) }
    val navOptions = remember {
        NavOptions.Builder()
            .setPopUpTo(
                route = startDestination,
                inclusive = true,
            )
            .build()
    }

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
                        visible = currentDestination == GameSettingsDestination.id,
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
                        visible = currentDestination == GameBoardDestination.id,
                    ) {
                        GameBoardToolbarActions(gameSettings.displayOptions)
                    }
                },
            )
        },
        bottomBar = {
            with(navController) {
                BottomAppBar {
                    MainDestinations.All.forEach { destination ->
                        IconToggleButton(
                            checked = currentDestination == destination.id,
                            onCheckedChange = { navigate(destination.id, navOptions.takeUnless { currentDestination == startDestination }) },
                        ) {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = null,
                            )
                        }
                    }
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(
                route = GameBoardDestination.id,
            ) {
                currentDestination = GameBoardDestination.id
                CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                    GameBoardView(
                        args = gameSettings,
                        parentEmitter = mainEmitter,
                        onStrategyClick = { disk ->
                            navController.navigate(
                                "${GameSettingsDestination.id}?$PickStrategy=$disk",
                                navOptions,
                            )
                        },
                        modifier = destinationModifier,
                    )
                }
            }

            composable(
                route = "${GameSettingsDestination.id}?$PickStrategy={$PickStrategy}",
                arguments = listOf(
                    navArgument(PickStrategy) {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    },
                ),
            ) { entry ->
                currentDestination = GameSettingsDestination.id
                CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
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
