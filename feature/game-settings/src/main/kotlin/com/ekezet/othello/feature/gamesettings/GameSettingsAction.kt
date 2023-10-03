package com.ekezet.othello.feature.gamesettings

import com.ekezet.hurok.Action

sealed interface GameSettingsAction : Action<GameSettingsModel, GameSettingsDependency>
