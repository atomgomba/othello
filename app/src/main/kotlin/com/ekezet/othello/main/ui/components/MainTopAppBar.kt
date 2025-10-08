package com.ekezet.othello.main.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.ekezet.othello.core.ui.R
import com.ekezet.othello.main.navigation.MainRoutes
import com.ekezet.othello.main.navigation.MainRoutes.GameBoardRoute
import com.ekezet.othello.main.navigation.MainRoutes.GameHistoryRoute
import com.ekezet.othello.main.navigation.MainRoutes.SettingsRoute

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun MainTopAppBar(
    onRefreshClick: () -> Unit,
    onShowPossibleMovesClick: (Boolean) -> Unit,
    currentDestination: String,
    navController: NavHostController,
    showPossibleMoves: Boolean,
    gameHistorySize: Int,
) {
    val titleString = when (currentDestination) {
        GameHistoryRoute.id ->
            stringResource(R.string.game_history__title__num_of_moves, gameHistorySize)

        SettingsRoute.id ->
            stringResource(R.string.main__nav__settings)

        else ->
            stringResource(R.string.app_name)
    }

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(),
        title = { Text(titleString) },
        navigationIcon = {
            AnimatedVisibility(
                visible = currentDestination != MainRoutes.Start,
            ) {
                IconButton(onClick = navController::popBackStack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.common__back),
                    )
                }
            }
        },
        actions = {
            AnimatedVisibility(
                visible = currentDestination == GameBoardRoute.id,
            ) {
                GameBoardToolbarActions(
                    onRefreshClick = onRefreshClick,
                    onShowPossibleMovesClick = onShowPossibleMovesClick,
                    showPossibleMoves = showPossibleMoves,
                )
            }
        },
    )
}
