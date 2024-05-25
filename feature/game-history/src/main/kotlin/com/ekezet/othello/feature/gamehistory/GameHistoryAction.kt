package com.ekezet.othello.feature.gamehistory

import com.ekezet.hurok.Action

internal sealed interface GameHistoryAction : Action<GameHistoryModel, GameHistoryDependency>
