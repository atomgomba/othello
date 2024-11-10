package com.ekezet.othello.core.game.store.sharedprefs

import android.content.SharedPreferences
import com.ekezet.othello.core.game.data.BoardDisplayOptions
import com.ekezet.othello.core.game.data.Default
import com.ekezet.othello.core.game.data.DefaultConfirmExit
import com.ekezet.othello.core.game.data.DefaultDarkStrategy
import com.ekezet.othello.core.game.data.DefaultLightStrategy
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.game.data.IGameSettings
import com.ekezet.othello.core.game.strategy.HumanPlayer
import com.ekezet.othello.core.game.strategy.PreferSidesDecoratorStrategy
import com.ekezet.othello.core.game.strategy.PreferSidesDecoratorStrategy.Companion.preferSides
import com.ekezet.othello.core.game.strategy.Strategy
import com.ekezet.othello.core.game.strategy.ofName
import com.ekezet.othello.core.game.strategy.wrappedName

private const val KEY_LIGHT_NAME = "lightName"
private const val KEY_LIGHT_PREFER_SIDES = "lightPreferSides"
private const val KEY_DARK_NAME = "darkName"
private const val KEY_DARK_PREFER_SIDES = "darkPreferSides"
private const val KEY_SHOW_POSSIBLE_MOVES = "showPossibleMoves"
private const val KEY_SHOW_BOARD_POSITIONS = "showBoardPositions"
private const val KEY_IS_GRAYSCALE_MODE = "isGrayScaleMode"
private const val KEY_CONFIRM_EXIT = "confirmExit"

internal fun SharedPreferences.persist(data: IGameSettings) = with(edit()) {
    putString(KEY_LIGHT_NAME, data.lightStrategy?.wrappedName)
    putBoolean(KEY_LIGHT_PREFER_SIDES, data.lightStrategy is PreferSidesDecoratorStrategy)

    putString(KEY_DARK_NAME, data.darkStrategy?.wrappedName)
    putBoolean(KEY_DARK_PREFER_SIDES, data.darkStrategy is PreferSidesDecoratorStrategy)

    putBoolean(KEY_SHOW_POSSIBLE_MOVES, data.boardDisplayOptions.showPossibleMoves)
    putBoolean(KEY_SHOW_BOARD_POSITIONS, data.boardDisplayOptions.showBoardPositions)
    putBoolean(KEY_IS_GRAYSCALE_MODE, data.boardDisplayOptions.isGrayscaleMode)

    putBoolean(KEY_CONFIRM_EXIT, data.confirmExit)

    apply()
}

internal fun SharedPreferences.load(): GameSettings =
    GameSettings(
        lightStrategy = getStrategy(KEY_LIGHT_NAME, KEY_LIGHT_PREFER_SIDES, DefaultLightStrategy),
        darkStrategy = getStrategy(KEY_DARK_NAME, KEY_DARK_PREFER_SIDES, DefaultDarkStrategy),
        boardDisplayOptions = with(BoardDisplayOptions.Default) {
            BoardDisplayOptions(
                showPossibleMoves = getBoolean(KEY_SHOW_POSSIBLE_MOVES, showPossibleMoves),
                showBoardPositions = getBoolean(KEY_SHOW_BOARD_POSITIONS, showBoardPositions),
                isGrayscaleMode = getBoolean(KEY_IS_GRAYSCALE_MODE, isGrayscaleMode),
            )
        },
        confirmExit = getBoolean(KEY_CONFIRM_EXIT, DefaultConfirmExit),
    )

private fun SharedPreferences.getStrategy(
    keyName: String,
    keyPref: String,
    default: Strategy? = HumanPlayer,
) = getString(keyName, default.wrappedName)
    ?.let { name -> Strategy.ofName(name) }
    ?.run {
        if (getBoolean(keyPref, default is PreferSidesDecoratorStrategy)) {
            preferSides()
        } else {
            this
        }
    }
