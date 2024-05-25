package com.ekezet.othello.feature.gamehistory

import com.ekezet.hurok.Effect

internal sealed interface GameHistoryEffect : Effect<GameHistoryModel, GameHistoryDependency>
