package com.ekezet.othello.feature.gamesettings.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ekezet.hurok.AnyParentLoop
import com.ekezet.hurok.compose.LoopWrapper
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.game.strategy.Strategies
import com.ekezet.othello.core.game.strategy.Strategy
import com.ekezet.othello.core.ui.R.string
import com.ekezet.othello.core.ui.components.GamePiece
import com.ekezet.othello.core.ui.stringResource
import com.ekezet.othello.feature.gamesettings.GameSettingsLoop
import com.ekezet.othello.feature.gamesettings.GameSettingsState
import org.koin.compose.koinInject

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
        GameSettingsViewImpl(modifier)

        LaunchedEffect(key1 = showStrategySelectorFor) {
            if (showStrategySelectorFor != null) {
                onShowStrategiesClick(showStrategySelectorFor)
            }
        }
    }
}

@Composable
private fun GameSettingsState.GameSettingsViewImpl(
    modifier: Modifier = Modifier,
) {
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

        item {
            Spacer(Modifier.height(4.dp))
        }

        item {
            StrategyValueSelector(
                disk = Disk.Dark,
                name = darkName,
                preferSides = isDarkPreferSides,
            )
        }

        item {
            Divider()
        }

        item {
            StrategyValueSelector(
                disk = Disk.Light,
                name = lightName,
                preferSides = isLightPreferSides,
            )
        }
    }

    if (selectingStrategyFor != null) {
        StrategyPicker(disk = selectingStrategyFor)
    }
}

@Composable
private fun GameSettingsState.StrategyValueSelector(
    disk: Disk,
    name: String?,
    preferSides: Boolean,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onShowStrategiesClick(disk) },
    ) {
        GamePiece(
            disk = disk,
            modifier = Modifier
                .size(48.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = CircleShape,
                ),
        )

        Column {
            Text(
                text = stringResource(
                    id = string.game_settings__title__strategy,
                    disk.stringResource,
                ),
                style = MaterialTheme.typography.labelSmall,
            )

            Spacer(Modifier.height(4.dp))

            Text(
                name ?: stringResource(id = string.common__human_player),
                fontWeight = FontWeight.Bold,
            )
        }
    }

    AnimatedVisibility(visible = disk.isNotHuman) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(stringResource(id = string.game_settings__switch__prefer_sides))
            Spacer(Modifier.weight(1F))
            Switch(checked = preferSides, onCheckedChange = { checked ->
                onPreferSidesClick(disk, checked)
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameSettingsState.StrategyPicker(disk: Disk) {
    ModalBottomSheet(onDismissRequest = onDismissStrategies) {
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
            text = strategy.stringResource,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}
