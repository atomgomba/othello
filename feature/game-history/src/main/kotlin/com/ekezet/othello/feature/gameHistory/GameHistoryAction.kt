package com.ekezet.othello.feature.gameHistory

import com.ekezet.hurok.Action

internal sealed interface GameHistoryAction : Action<GameHistoryModel, GameHistoryDependency>
