package com.ekezet.othello.feature.gamesettings

import com.ekezet.hurok.renderState
import com.ekezet.hurok.test.after
import com.ekezet.hurok.test.matches
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.isLight
import com.ekezet.othello.core.game.strategy.HumanPlayer
import com.ekezet.othello.core.game.strategy.RandomStrategy
import com.ekezet.othello.core.game.strategy.Strategy
import com.ekezet.othello.feature.gamesettings.ui.defaultModel
import com.ekezet.othello.feature.gamesettings.ui.showDarkStrategySelectorModel
import com.ekezet.othello.feature.gamesettings.ui.showLightStrategySelectorModel
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import kotlin.test.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(JUnitParamsRunner::class)
internal class GameSettingsActionTest {
    private val renderer = GameSettingsRenderer()

    @Test
    @Parameters(method = "paramsForDisk")
    fun `OnStrategySelectorClicked works correctly`(player: Disk?) {
        val initModel = GameSettingsModel()

        val expectedModel = if (player!!.isDark) {
            showDarkStrategySelectorModel
        } else {
            showLightStrategySelectorModel
        }

        initModel after OnStrategySelectorClicked(player) matches {
            assertModel(expectedModel)
            assertNoEffects()
        }

        val expectedState = renderState(::GameSettingsLoop, expectedModel, renderer)
        assertEquals(expectedModel.selectingStrategyFor, expectedState.selectingStrategyFor)
    }

    @Test
    @Parameters(method = "paramsForDisk")
    fun `OnStrategySelectorDismissed works correctly`(player: Disk?) {
        val initModel = GameSettingsModel().showStrategySelectorFor(player!!)

        val expectedModel = GameSettingsModel()

        initModel after OnStrategySelectorDismissed matches {
            assertModel(expectedModel)
            assertNoEffects()
        }

        val expectedState = renderState(::GameSettingsLoop, expectedModel, renderer)
        assertEquals(expectedModel.selectingStrategyFor, expectedState.selectingStrategyFor)
    }

    @Test
    @Parameters(method = "paramsForDiskAndStrategy")
    fun `OnStrategySelected works correctly`(disk: Disk?, strategy: Strategy?) {
        val initModel = defaultModel

        val expectedModel = defaultModel.setStrategyFor(disk!!, strategy)

        val expectedEffects = setOf(PublishGameSettings(expectedModel))

        initModel after OnStrategySelected(disk, strategy) matches {
            assertModel(expectedModel)
            assertEffects(expectedEffects)
        }

        val expectedState = renderState(::GameSettingsLoop, expectedModel, renderer)
        if (disk.isDark) {
            assertEquals(expectedModel.darkStrategy, expectedState.darkStrategy)
        } else if (disk.isLight) {
            assertEquals(expectedModel.lightStrategy, expectedState.lightStrategy)
        }
    }

    @Suppress("unused")
    private fun paramsForDisk() = arrayOf(
        arrayOf(Disk.Dark),
        arrayOf(Disk.Light),
    )

    @Suppress("unused")
    private fun paramsForDiskAndStrategy() = arrayOf(
        arrayOf(Disk.Dark, RandomStrategy),
        arrayOf(Disk.Dark, HumanPlayer),
        arrayOf(Disk.Light, RandomStrategy),
        arrayOf(Disk.Light, HumanPlayer),
    )
}
