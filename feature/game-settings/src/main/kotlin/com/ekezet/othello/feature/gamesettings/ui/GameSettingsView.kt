package com.ekezet.othello.feature.gamesettings.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.ui.R.string
import com.ekezet.othello.feature.gamesettings.GameSettingsLoop
import com.ekezet.othello.feature.gamesettings.GameSettingsState
import com.ekezet.othello.feature.gamesettings.OnGrayscaleModeClicked
import com.ekezet.othello.feature.gamesettings.OnShowBoardPositionsClicked
import com.ekezet.othello.feature.gamesettings.OnShowPossibleMovesClicked
import com.ekezet.othello.feature.gamesettings.ui.components.SelectedStrategy
import com.ekezet.othello.feature.gamesettings.ui.components.StrategySelector
import com.ekezet.othello.feature.gamesettings.ui.components.SwitchRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameSettingsView(
    args: GameSettings,
    selectStrategyFor: Disk?,
    modifier: Modifier = Modifier,
) {
    LoopWrapper(
        builder = GameSettingsLoop,
        args = args,
    ) {
        GameSettingsViewImpl(selectStrategyFor, modifier)
    }
}

@ExperimentalMaterial3Api
@Composable
internal fun GameSettingsState.GameSettingsViewImpl(
    selectStrategyFor: Disk?,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState()

    LazyColumn(
        modifier = Modifier
            .padding(top = 16.dp)
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(24.dp),
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

        item { Spacer(Modifier.height(4.dp)) }

        item {
            SettingsHeader(
                text = stringResource(id = string.game_settings__header__board),
            )
        }

        item {
            SwitchRow(
                label = stringResource(id = string.game_settings__switch__show_possible_moves),
                checked = displayOptions.showPossibleMoves,
                onCheckedChange = { emit(OnShowPossibleMovesClicked) },
            )
        }

        item {
            SwitchRow(
                label = stringResource(id = string.game_settings__switch__show_positions),
                checked = displayOptions.showBoardPositions,
                onCheckedChange = { emit(OnShowBoardPositionsClicked) },
            )
        }

        item {
            SwitchRow(
                label = stringResource(id = string.game_settings__switch__grayscale_mode),
                checked = displayOptions.isGrayscaleMode,
                onCheckedChange = { emit(OnGrayscaleModeClicked) },
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
            .padding(horizontal = 16.dp),
    )
}
