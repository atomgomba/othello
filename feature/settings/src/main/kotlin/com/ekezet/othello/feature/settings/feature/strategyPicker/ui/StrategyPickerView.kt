package com.ekezet.othello.feature.settings.feature.strategyPicker.ui

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
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ekezet.hurok.AnyActionEmitter
import com.ekezet.hurok.compose.LoopStateCollectors
import com.ekezet.hurok.compose.LoopView
import com.ekezet.hurok.compose.WithLifecycle
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.game.strategy.Strategies
import com.ekezet.othello.core.game.strategy.Strategy
import com.ekezet.othello.core.game.strategy.nameEqualsTo
import com.ekezet.othello.core.ui.R.string
import com.ekezet.othello.core.ui.nameOrHumanPlayer
import com.ekezet.othello.core.ui.stringResource
import com.ekezet.othello.feature.settings.feature.strategyPicker.StrategyPickerAction
import com.ekezet.othello.feature.settings.feature.strategyPicker.StrategyPickerDependency
import com.ekezet.othello.feature.settings.feature.strategyPicker.StrategyPickerLoop
import com.ekezet.othello.feature.settings.feature.strategyPicker.hideStrategyPicker
import com.ekezet.othello.feature.settings.feature.strategyPicker.selectStrategy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

typealias OnStrategySelect = (player: Disk, selected: Strategy?) -> Unit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StrategyPickerView(
    parentEmitter: AnyActionEmitter,
    onSelect: OnStrategySelect,
    dependency: StrategyPickerDependency,
) {
    LoopView(
        StrategyPickerLoop(dependency),
        childOf = setOf(parentEmitter),
        loopStateCollector = LoopStateCollectors.WithLifecycle(),
    ) { emit ->
        if (visible) {
            StrategyPickerViewImpl(
                emit = emit,
                onSelect = onSelect,
                player = pickForDisk,
                defaultName = selectedName,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StrategyPickerViewImpl(
    emit: (StrategyPickerAction) -> Unit,
    onSelect: OnStrategySelect,
    player: Disk,
    defaultName: String?,
    sheetState: SheetState = rememberModalBottomSheetState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) {
    ModalBottomSheet(
        onDismissRequest = {
            coroutineScope.launch {
                sheetState.hide()
            }.invokeOnCompletion {
                emit(hideStrategyPicker)
            }
        },
        sheetState = sheetState,
    ) {
        Column {
            Text(
                text = stringResource(string.settings__title__strategy, player.stringResource),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
            )

            for (strategy in Strategies) {
                StrategyListItem(
                    strategy = strategy,
                    isSelected = defaultName.nameEqualsTo(strategy),
                    onClick = {
                        emit(selectStrategy(strategy))
                        onSelect(player, strategy)
                    },
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
            contentDescription = strategy?.name,
            modifier = Modifier.alpha(if (isSelected) 1F else 0F),
        )

        Text(
            text = strategy.nameOrHumanPlayer,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}
