package com.ekezet.othello.main.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.ekezet.othello.core.ui.R
import com.ekezet.othello.main.MainState
import com.ekezet.othello.main.navigation.MainRoutes
import com.ekezet.othello.main.navigation.MainRoutes.GameBoardRoute
import com.ekezet.othello.main.navigation.MainRoutes.GameHistoryRoute
import com.ekezet.othello.main.navigation.MainRoutes.GameSettingsRoute

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun MainState.MainTopAppBar(
    currentDestination: String,
    navController: NavHostController,
    showPossibleMoves: Boolean,
    gameHistorySize: Int,
) {
    val titleString = when (currentDestination) {
        GameHistoryRoute.id ->
            stringResource(R.string.game_history__title__num_of_moves, gameHistorySize)

        GameSettingsRoute.id ->
            stringResource(R.string.main__nav__game_settings)

        else ->
            stringResource(R.string.app_name)
    }

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = { Text(titleString) },
        navigationIcon = {
            AnimatedVisibility(
                visible = currentDestination != MainRoutes.Start,
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                    )
                }
            }
        },
        actions = {
            AnimatedVisibility(
                visible = currentDestination == GameBoardRoute.id,
            ) {
                GameBoardToolbarActions(showPossibleMoves)
            }
        },
    )
}
