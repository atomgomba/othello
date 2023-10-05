package com.ekezet.othello.feature.gamesettings.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ekezet.hurok.AnyParentLoop
import com.ekezet.hurok.compose.LoopWrapper
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.ui.R.string
import com.ekezet.othello.feature.gamesettings.GameSettingsLoop
import com.ekezet.othello.feature.gamesettings.GameSettingsState
import com.ekezet.othello.feature.gamesettings.ui.components.SelectedStrategy
import com.ekezet.othello.feature.gamesettings.ui.components.StrategyPicker
import com.ekezet.othello.feature.gamesettings.ui.components.SwitchRow
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameSettingsView(
    args: GameSettings,
    showStrategySelectorFor: Disk?,
    modifier: Modifier = Modifier,
    parentLoop: AnyParentLoop? = null,
) {
    LoopWrapper(
        builder = GameSettingsLoop,
        parentLoop = parentLoop,
        dependency = koinInject(),
        args = args,
    ) {
        GameSettingsViewImpl(showStrategySelectorFor, modifier)
    }
}

@ExperimentalMaterial3Api
@Composable
private fun GameSettingsState.GameSettingsViewImpl(
    showStrategySelectorFor: Disk?,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState()

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Text(
                text = stringResource(id = string.game_settings__header__players),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        item { Spacer(Modifier.height(4.dp)) }

        item {
            SelectedStrategy(
                disk = Disk.Dark,
                name = darkName,
                preferSides = isDarkPreferSides,
            )
        }

        item { Divider() }

        item {
            SelectedStrategy(
                disk = Disk.Light,
                name = lightName,
                preferSides = isLightPreferSides,
            )
        }

        item { Divider() }

        item {
            SwitchRow(
                label = "Show possible moves",
                checked = displayOptions.showPossibleMoves,
                onCheckedChange = { onShowPossibleMovesClick() },
            )
        }

        item { Divider() }

        item {
            SwitchRow(
                label = "Show board positions",
                checked = displayOptions.showBoardPositions,
                onCheckedChange = { onShowBoardPositionsClick() },
            )
        }
    }

    StrategyPicker(
        selectingStrategyFor = selectingStrategyFor,
        showStrategySelectorFor = showStrategySelectorFor,
        sheetState = sheetState,
    )
}
