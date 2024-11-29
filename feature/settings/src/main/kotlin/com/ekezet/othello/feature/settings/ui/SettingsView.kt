package com.ekezet.othello.feature.settings.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ekezet.hurok.compose.LoopWrapper
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.ui.R.string
import com.ekezet.othello.feature.settings.OnAlwaysScrollToBottomClicked
import com.ekezet.othello.feature.settings.OnConfirmExitClicked
import com.ekezet.othello.feature.settings.OnGrayscaleModeClicked
import com.ekezet.othello.feature.settings.OnShowBoardPositionsClicked
import com.ekezet.othello.feature.settings.OnShowPossibleMovesClicked
import com.ekezet.othello.feature.settings.SettingsArgs
import com.ekezet.othello.feature.settings.SettingsLoop
import com.ekezet.othello.feature.settings.SettingsState
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
    LoopWrapper(
        builder = SettingsLoop,
        args = args,
    ) {
        SettingsViewImpl(
            selectStrategyFor = selectStrategyFor,
            modifier = modifier,
            listState = listState,
        )
    }
}

@ExperimentalMaterial3Api
@Composable
internal fun SettingsState.SettingsViewImpl(
    selectStrategyFor: Disk?,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
) {
    val sheetState = rememberModalBottomSheetState()

    LazyColumn(
        modifier = modifier,
        state = listState,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
    ) {
        item {
            SettingsHeader(
                text = stringResource(id = string.game_settings__header__players),
            )
        }

        item {
            SelectedStrategy(
                disk = Disk.Dark,
                name = darkName,
                preferSides = isDarkPreferSides,
            )
        }

        item { HorizontalDivider(modifier = Modifier.padding(horizontal = 32.dp)) }

        item {
            SelectedStrategy(
                disk = Disk.Light,
                name = lightName,
                preferSides = isLightPreferSides,
            )
        }

        item {
            SettingsHeader(
                text = stringResource(id = string.game_settings__header__board),
            )
        }

        item {
            SwitchRow(
                label = stringResource(id = string.game_settings__switch__show_possible_moves),
                checked = boardDisplayOptions.showPossibleMoves,
                onCheckedChange = { emit(OnShowPossibleMovesClicked) },
            )
        }

        item {
            SwitchRow(
                label = stringResource(id = string.game_settings__switch__show_positions),
                checked = boardDisplayOptions.showBoardPositions,
                onCheckedChange = { emit(OnShowBoardPositionsClicked) },
            )
        }

        item {
            SwitchRow(
                label = stringResource(id = string.game_settings__switch__grayscale_mode),
                checked = boardDisplayOptions.isGrayscaleMode,
                onCheckedChange = { emit(OnGrayscaleModeClicked) },
            )
        }

        item {
            SettingsHeader(
                text = stringResource(id = string.game_settings__header__history),
            )
        }

        item {
            SwitchRow(
                label = stringResource(id = string.game_settings__switch__always_scroll_to_bottom),
                checked = historyDisplayOptions.alwaysScrollToBottom,
                onCheckedChange = { emit(OnAlwaysScrollToBottomClicked) },
            )
        }

        item {
            SettingsHeader(
                text = stringResource(id = string.game_settings__header__app),
            )
        }

        item {
            SwitchRow(
                label = stringResource(id = string.game_settings__switch__confirm_exit),
                checked = confirmExit,
                onCheckedChange = { emit(OnConfirmExitClicked) },
            )
        }
    }

    StrategySelector(
        selectStrategyFor = selectStrategyFor,
        sheetState = sheetState,
    )
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
