package com.ekezet.othello.feature.settings

import com.ekezet.hurok.test.after
import com.ekezet.hurok.test.matches
import com.ekezet.hurok.utils.renderState
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.isDark
import com.ekezet.othello.core.data.models.isLight
import com.ekezet.othello.core.game.strategy.HumanPlayer
import com.ekezet.othello.core.game.strategy.RandomStrategy
import com.ekezet.othello.core.game.strategy.Strategy
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.runner.RunWith
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(JUnitParamsRunner::class)
internal class SettingsActionTest {
    private val defaultModel = SettingsModel()
    private val showDarkStrategySelectorModel = defaultModel.showStrategySelectorFor(Disk.Dark)
    private val showLightStrategySelectorModel = defaultModel.showStrategySelectorFor(Disk.Light)

    private val renderer = SettingsRenderer()

    @Test
    @Parameters(method = "paramsForDisk")
    fun `OnStrategySelectorClicked works correctly`(player: Disk?) {
        val initModel = SettingsModel()

        val expectedModel = if (player!!.isDark) {
            showDarkStrategySelectorModel
        } else {
            showLightStrategySelectorModel
        }

        initModel after OnStrategySelectorClicked(player) matches {
            assertModel(expectedModel)
            assertNoEffects()
        }

        val expectedState = renderState(::SettingsLoop, expectedModel, renderer)
        assertEquals(expectedModel.selectingStrategyFor, expectedState.selectingStrategyFor)
    }

    @Test
    @Parameters(method = "paramsForDisk")
    fun `OnStrategySelectorDismissed works correctly`(player: Disk?) {
        val initModel = SettingsModel().showStrategySelectorFor(player!!)

        val expectedModel = SettingsModel()

        initModel after OnStrategySelectorDismissed matches {
            assertModel(expectedModel)
            assertNoEffects()
        }

        val expectedState = renderState(::SettingsLoop, expectedModel, renderer)
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

        val expectedState = renderState(::SettingsLoop, expectedModel, renderer)
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
