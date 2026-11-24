package com.ekezet.othello.feature.settings

import com.ekezet.hurok.test.after
import com.ekezet.hurok.test.matches
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.isDark
import com.ekezet.othello.core.data.models.isLight
import com.ekezet.othello.core.game.strategy.HumanPlayer
import com.ekezet.othello.core.game.strategy.Strategy
import io.mockk.mockk
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.runner.RunWith
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(JUnitParamsRunner::class)
internal class SettingsActionTest {
    private val defaultModel = SettingsModel()

    private val renderer = SettingsRenderer()

    @Test
    @Parameters(method = "paramsForDiskAndStrategy")
    fun `OnStrategySelected works correctly`(disk: Disk?, strategy: Strategy?) {
        val initModel = defaultModel

        val expectedModel = defaultModel.setStrategyFor(disk!!, strategy)

        initModel after OnStrategySelected(disk, strategy) matches {
            assertModel(expectedModel)
        }

        val expectedState = renderer.renderState(expectedModel)
        if (disk.isDark) {
            assertEquals(expectedModel.darkStrategy, expectedState.darkStrategy)
        } else if (disk.isLight) {
            assertEquals(expectedModel.lightStrategy, expectedState.lightStrategy)
        }
    }

    @Test
    @Parameters(method = "paramsForDiskAndStrategy")
    fun `OnStrategySelectorClicked works correctly`(disk: Disk?, strategy: Strategy?) {
        val initModel = defaultModel.run {
            if (disk!!.isDark) copy(darkStrategy = strategy) else copy(lightStrategy = strategy)
        }

        val expectedEffect = ShowStrategyPicker(player = disk!!, current = strategy)

        initModel after OnStrategySelectorClicked(disk) matches {
            assertEffects(setOf(expectedEffect))
        }
    }

    @Suppress("unused")
    private fun paramsForDisk() = arrayOf(
        arrayOf(Disk.Dark),
        arrayOf(Disk.Light),
    )

    @Suppress("unused")
    private fun paramsForDiskAndStrategy() = arrayOf(
        arrayOf(Disk.Dark, mockk<Strategy>(relaxed = true)),
        arrayOf(Disk.Dark, HumanPlayer),
        arrayOf(Disk.Light, mockk<Strategy>(relaxed = true)),
        arrayOf(Disk.Light, HumanPlayer),
    )
}
