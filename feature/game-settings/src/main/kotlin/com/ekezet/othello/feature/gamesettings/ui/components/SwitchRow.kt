package com.ekezet.othello.feature.gamesettings.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
internal fun SwitchRow(label: String, checked: Boolean, onCheckedChange: (value: Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label)
        Spacer(Modifier.weight(1F))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
