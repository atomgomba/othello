package com.ekezet.othello.feature.gamesettings.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.ui.R.string
import com.ekezet.othello.core.ui.components.GamePiece
import com.ekezet.othello.core.ui.orHumanPlayer
import com.ekezet.othello.core.ui.stringResource
import com.ekezet.othello.feature.gamesettings.GameSettingsState

@Composable
internal fun GameSettingsState.SelectedStrategy(
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
                text = name.orHumanPlayer,
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
