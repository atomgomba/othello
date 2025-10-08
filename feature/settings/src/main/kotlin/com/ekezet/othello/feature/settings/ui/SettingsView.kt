package com.ekezet.othello.feature.settings.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ekezet.hurok.compose.LoopView
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.ui.R.string
import com.ekezet.othello.feature.settings.OnAlwaysScrollToBottomClicked
import com.ekezet.othello.feature.settings.OnConfirmExitClicked
import com.ekezet.othello.feature.settings.OnGrayscaleModeClicked
import com.ekezet.othello.feature.settings.OnResetSettingsClicked
import com.ekezet.othello.feature.settings.OnResetSettingsDialogClicked
import com.ekezet.othello.feature.settings.OnShowBoardPositionsClicked
import com.ekezet.othello.feature.settings.OnShowPossibleMovesClicked
import com.ekezet.othello.feature.settings.SettingsAction
import com.ekezet.othello.feature.settings.SettingsArgs
import com.ekezet.othello.feature.settings.SettingsLoop
import com.ekezet.othello.feature.settings.SettingsState
import com.ekezet.othello.feature.settings.ui.components.ButtonRow
import com.ekezet.othello.feature.settings.ui.components.SelectedStrategy
import com.ekezet.othello.feature.settings.ui.components.StrategySelector
import com.ekezet.othello.feature.settings.ui.components.SwitchRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsView(
    args: SettingsArgs,
    selectStrategyFor: Disk?,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
) {
    LoopView(
        builder = SettingsLoop,
        args = args,
    ) { emit ->
        SettingsViewImpl(
            emit = emit,
            selectStrategyFor = selectStrategyFor,
            modifier = modifier,
            listState = listState,
        )
    }
}

@ExperimentalMaterial3Api
@Composable
internal fun SettingsState.SettingsViewImpl(
    emit: (action: SettingsAction) -> Unit,
    selectStrategyFor: Disk?,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
) {
    val sheetState = rememberModalBottomSheetState()

    LazyColumn(
        modifier = modifier,
        state = listState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
    ) {
        item {
            SettingsHeader(
                text = stringResource(id = string.settings__header__players),
            )
        }

        item {
            SelectedStrategy(
                emit = emit,
                disk = Disk.Dark,
                name = darkName,
                preferSides = isDarkPreferSides,
            )
        }

        item { HorizontalDivider(modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)) }

        item {
            SelectedStrategy(
                emit = emit,
                disk = Disk.Light,
                name = lightName,
                preferSides = isLightPreferSides,
            )
        }

        item {
            SettingsHeader(
                text = stringResource(id = string.settings__header__board),
            )
        }

        item {
            SwitchRow(
                label = stringResource(id = string.settings__switch__show_possible_moves),
                checked = boardDisplayOptions.showPossibleMoves,
                onCheckedChange = { emit(OnShowPossibleMovesClicked) },
            )
        }

        item {
            SwitchRow(
                label = stringResource(id = string.settings__switch__show_positions),
                checked = boardDisplayOptions.showBoardPositions,
                onCheckedChange = { emit(OnShowBoardPositionsClicked) },
            )
        }

        item {
            SwitchRow(
                label = stringResource(id = string.settings__switch__grayscale_mode),
                description = stringResource(id = string.settings__switch__grayscale_mode_description),
                checked = boardDisplayOptions.isGrayscaleMode,
                onCheckedChange = { emit(OnGrayscaleModeClicked) },
            )
        }

        item {
            SettingsHeader(
                text = stringResource(id = string.settings__header__history),
            )
        }

        item {
            SwitchRow(
                label = stringResource(id = string.settings__switch__always_scroll_to_bottom),
                checked = historyDisplayOptions.alwaysScrollToBottom,
                onCheckedChange = { emit(OnAlwaysScrollToBottomClicked) },
            )
        }

        item {
            SettingsHeader(
                text = stringResource(id = string.settings__header__app),
            )
        }

        item {
            SwitchRow(
                label = stringResource(id = string.settings__switch__confirm_exit),
                checked = confirmExit,
                onCheckedChange = { emit(OnConfirmExitClicked) },
            )
        }

        item {
            ButtonRow(
                label = stringResource(id = string.settings__title__reset_settings),
                onClick = { emit(OnResetSettingsClicked) },
            )
        }
    }

    StrategySelector(
        emit = emit,
        selectStrategyFor = selectStrategyFor,
        sheetState = sheetState,
    )

    if (showConfirmResetSettingsDialog) {
        AlertDialog(
            title = { Text(text = stringResource(id = string.settings__title__reset_settings)) },
            text = { Text(text = stringResource(id = string.settings__confirm__reset_settings)) },
            onDismissRequest = { emit(OnResetSettingsDialogClicked(isConfirmed = false)) },
            confirmButton = {
                TextButton(onClick = { emit(OnResetSettingsDialogClicked(isConfirmed = true)) }) {
                    Text(text = stringResource(id = string.common__okay))
                }
            },
            dismissButton = {
                TextButton(onClick = { emit(OnResetSettingsDialogClicked(isConfirmed = false)) }) {
                    Text(text = stringResource(id = string.common__cancel))
                }
            },
        )
    }
}

@Composable
private fun SettingsHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .padding(top = 4.dp)
            .padding(horizontal = 16.dp),
    )
}
