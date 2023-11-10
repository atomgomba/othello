package com.ekezet.othello.feature.gamesettings.ui.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { actions.onStrategySelectorClick(disk) }
                .padding(horizontal = 16.dp),
        ) {
            Column {
                Text(
                    text = stringResource(
                        id = string.game_settings__title__strategy,
                        disk.stringResource,
                    ),
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = name.orHumanPlayer,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline,
                )
            }

            Spacer(modifier = Modifier.weight(1F))

            GamePiece(
                disk = disk,
                modifier = Modifier
                    .size(32.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = CircleShape,
                    ),
            )
        }

        if (disk.isNotHuman) {
            SwitchRow(
                label = stringResource(id = string.game_settings__switch__prefer_sides),
                checked = preferSides,
                onCheckedChange = { checked ->
                    actions.onPreferSidesToggle(disk, checked)
                },
            )
        }
    }
}
