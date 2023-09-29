package com.ekezet.othello.feature.gameboard.di

import org.koin.core.qualifier.named

val GameBoardScopeName
    inline get() = named("gameBoardScope")
