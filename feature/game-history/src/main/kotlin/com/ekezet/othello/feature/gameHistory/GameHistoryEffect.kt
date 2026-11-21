package com.ekezet.othello.feature.gameHistory

import com.ekezet.hurok.Effect

internal sealed interface GameHistoryEffect : Effect<GameHistoryModel, GameHistoryDependency>
