package com.ekezet.othello.feature.gamesettings.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.game.strategy.Strategies
import com.ekezet.othello.core.game.strategy.Strategy
import com.ekezet.othello.core.ui.R.string
import com.ekezet.othello.core.ui.nameOrHumanPlayer
import com.ekezet.othello.core.ui.stringResource
import com.ekezet.othello.feature.gamesettings.GameSettingsState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GameSettingsState.StrategyPicker(
    // FIXME
    selectingStrategyFor: Disk?,
    // FIXME
    pickStrategyFor: Disk?,
    sheetState: SheetState,
) {
    val scope = rememberCoroutineScope()
    var isFirstTimePicker by rememberSaveable { mutableStateOf(true) }

    if (selectingStrategyFor != null) {
        // open normal selection
        StrategyPickerImpl(selectingStrategyFor, sheetState)
    }

    LaunchedEffect(key1 = selectingStrategyFor) {
        // if picker is dismissed, need to update bottom sheet state too
        if (selectingStrategyFor == null && sheetState.isVisible) {
            scope.launch { sheetState.hide() }
        }
    }

    if (isFirstTimePicker && pickStrategyFor != null) {
        LaunchedEffect(key1 = pickStrategyFor) {
            if (!sheetState.isVisible && selectingStrategyFor == null) {
                // picker isn't already open, open it now
                onShowStrategiesClick(pickStrategyFor)
            }
        }
        isFirstTimePicker = false
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameSettingsState.StrategyPickerImpl(
    disk: Disk,
    sheetState: SheetState,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissStrategies,
        sheetState = sheetState,
    ) {
        Column {
            Text(
                text = stringResource(
                    id = string.game_settings__title__strategy,
                    disk.stringResource,
                ),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
            )

            for (strategy in Strategies) {
                StrategyListItem(
                    strategy = strategy,
                    isSelected = disk.isStrategySelected(strategy),
                    onClick = { onStrategySelect(disk, strategy) },
                )
            }

            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun StrategyListItem(strategy: Strategy?, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            modifier = Modifier.alpha(if (isSelected) 1F else 0F),
        )

        Text(
            text = strategy.nameOrHumanPlayer,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}
