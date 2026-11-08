package com.ekezet.othello.main.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.ekezet.othello.core.ui.R
import com.ekezet.othello.main.navigation.Screen
import kotlin.reflect.KClass

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun MainTopAppBar(
    onRefreshClick: () -> Unit,
    onShowPossibleMovesClick: (Boolean) -> Unit,
    currentDestinationClass: KClass<out Screen>,
    navController: NavHostController,
    showPossibleMoves: Boolean,
    gameHistorySize: Int,
) {
    val titleString = when (currentDestinationClass) {
        Screen.GameHistory::class ->
            stringResource(R.string.game_history__title__num_of_moves, gameHistorySize)

        Screen.Settings::class ->
            stringResource(R.string.main__nav__settings)

        else ->
            stringResource(R.string.app_name)
    }

    TopAppBar(
        title = { Text(titleString) },
        navigationIcon = {
            AnimatedVisibility(
                visible = currentDestinationClass != Screen.GameBoard::class,
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
                visible = currentDestinationClass == Screen.GameBoard::class,
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
