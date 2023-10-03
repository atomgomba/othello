package com.ekezet.othello.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ekezet.hurok.compose.LoopWrapper
import com.ekezet.othello.MainLoop
import com.ekezet.othello.MainScope
import com.ekezet.othello.MainState
import com.ekezet.othello.R.string
import com.ekezet.othello.core.game.data.BoardDisplayOptions
import com.ekezet.othello.core.game.provider.GameSettingsProvider
import com.ekezet.othello.core.ui.R
import com.ekezet.othello.feature.gameboard.ui.GameBoardView
import com.ekezet.othello.feature.gamesettings.ui.GameSettingsView
import com.ekezet.othello.navigation.AppDestination
import kotlinx.coroutines.CoroutineScope
import org.koin.compose.koinInject

@Composable
internal fun MainView(
    parentScope: CoroutineScope = rememberCoroutineScope(),
) {
    LoopWrapper(
        builder = MainLoop,
        parentScope = parentScope,
        dependency = koinInject(),
    ) { state ->
        state.MainViewImpl(this)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainState.MainViewImpl(
    mainLoop: MainScope,
    navController: NavHostController = rememberNavController(),
    gameSettingsProvider: GameSettingsProvider = koinInject(),
) {
    val gameSettings by gameSettingsProvider.settings.collectAsState()
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
                navigationIcon = { },
                actions = {
                    if (currentDestination == AppDestination.GameBoard.label) {
                        GameBoardToolbarActions(gameSettings.displayOptions)
                    }
                },
            )
        },
        bottomBar = {
            with(navController) {
                NavigationBar {
                    AppDestination.All.forEach { destination ->
                        NavigationBarItem(
                            selected = currentDestination == destination.label,
                            onClick = {
                                navigate(destination.label)
                                currentDestination = destination.label
                            },
                            icon = {
                                Icon(
                                    imageVector = destination.icon, contentDescription = null
                                )
                            },
                        )
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
                CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                    GameBoardView(
                        args = gameSettings,
                        modifier = destinationModifier,
                        parentLoop = mainLoop,
                    )
                }
            }

            composable(AppDestination.GameSettings.label) {
                CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                    GameSettingsView(
                        args = gameSettings,
                        modifier = destinationModifier,
                        parentLoop = mainLoop,
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
    IconButton(onClick = onNewGameClick) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = stringResource(R.string.main__menu__new_game),
        )
    }

    IconButton(onClick = onToggleIndicatorsClick) {
        Icon(
            imageVector = if (showPossibleMoves) Icons.Filled.LocationOn else Icons.Outlined.LocationOn,
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
