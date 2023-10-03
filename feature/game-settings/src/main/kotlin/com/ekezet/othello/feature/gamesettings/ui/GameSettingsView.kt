package com.ekezet.othello.feature.gamesettings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ekezet.hurok.AnyLoopScope
import com.ekezet.hurok.compose.LoopWrapper
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.feature.gamesettings.GameSettingsLoop
import com.ekezet.othello.feature.gamesettings.GameSettingsState

@Composable
fun GameSettingsView(
    args: GameSettings,
    modifier: Modifier = Modifier,
    parentLoop: AnyLoopScope? = null,
) {
    LoopWrapper(
        builder = GameSettingsLoop,
        parentLoop = parentLoop,
        args = args,
    ) { state ->
        GameSettingsViewImpl(state, modifier)
    }
}

@Composable
private fun GameSettingsViewImpl(
    state: GameSettingsState,
    modifier: Modifier = Modifier,
) = with(state) {
    Column(modifier = modifier) {
        Text("Settings", style = MaterialTheme.typography.headlineSmall)
    }
}
