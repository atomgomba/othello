package com.ekezet.othello.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ekezet.hurok.compose.LoopWrapper
import com.ekezet.othello.MainLoop
import com.ekezet.othello.MainLoopScope
import com.ekezet.othello.MainState
import com.ekezet.othello.R.string
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.game.data.BoardDisplayOptions
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.game.store.GameSettingsStore
import com.ekezet.othello.core.ui.R
import com.ekezet.othello.feature.gameboard.ui.GameBoardView
import com.ekezet.othello.feature.gamesettings.ui.GameSettingsView
import com.ekezet.othello.navigation.AppDestination
import kotlinx.coroutines.CoroutineScope
import org.koin.compose.koinInject

@Composable
internal fun MainView(
    parentScope: CoroutineScope = rememberCoroutineScope(),
    gameSettingsStore: GameSettingsStore = koinInject(),
    navController: NavHostController = rememberNavController(),
) {
    val gameSettings: GameSettings by gameSettingsStore.settings.collectAsState()

    LoopWrapper(
        builder = MainLoop,
        args = gameSettings,
        parentScope = parentScope,
        dependency = koinInject(),
    ) { loop ->
        MainViewImpl(
            loop = loop,
            gameSettings = gameSettings,
            navController = navController,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainState.MainViewImpl(
    loop: MainLoopScope,
    gameSettings: GameSettings,
    navController: NavHostController,
) {
    val viewModelStoreOwner = requireNotNull(LocalViewModelStoreOwner.current) {
        "ViewModelStoreOwner must be provided"
    }
    val destinationModifier = Modifier
        .padding(16.dp)
        .fillMaxSize()
    var currentDestination: String by remember { mutableStateOf(AppDestination.Start.label) }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text(stringResource(string.app_name)) },
                navigationIcon = {
                    if (currentDestination == AppDestination.GameSettings.label) {
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
                        visible = currentDestination == AppDestination.GameBoard.label,
                    ) {
                        GameBoardToolbarActions(gameSettings.displayOptions)
                    }
                },
            )
        },
        bottomBar = {
            with(navController) {
                BottomAppBar {
                    AppDestination.All.forEach { destination ->
                        IconToggleButton(
                            checked = currentDestination == destination.label,
                            onCheckedChange = { navigate(destination.label) },
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
            startDestination = AppDestination.Start.label,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(AppDestination.GameBoard.label) {
                currentDestination = AppDestination.GameBoard.label
                CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                    GameBoardView(
                        args = gameSettings,
                        modifier = destinationModifier,
                        parentLoop = loop,
                        onStrategyClick = { disk ->
                            navController.navigate(AppDestination.GameSettings.label + "?disk=$disk")
                        },
                    )
                }
            }

            composable(
                AppDestination.GameSettings.label + "?disk={disk}",
                arguments = listOf(
                    navArgument("disk") {
                        nullable = true
                        defaultValue = null
                    },
                ),
            ) { backStackEntry ->
                currentDestination = AppDestination.GameSettings.label
                CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                    GameSettingsView(
                        args = gameSettings,
                        showStrategySelectorFor = Disk.valueOf(backStackEntry.arguments?.getString("disk")),
                        modifier = destinationModifier,
                        parentLoop = loop,
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
        IconButton(onClick = onNewGameClick) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = stringResource(R.string.main__menu__new_game),
            )
        }

        IconToggleButton(
            checked = showPossibleMoves,
            onCheckedChange = { onToggleIndicatorsClick() },
        ) {
            Icon(
                painter = painterResource(
                    id = if (showPossibleMoves) R.drawable.ic_visibility else R.drawable.ic_visibility_off,
                ),
                contentDescription = stringResource(R.string.main__menu__toggle_indicators),
            )
        }

        IconButton(onClick = onShareGameClick) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = stringResource(R.string.main__menu__share_board),
            )
        }
    }
}
