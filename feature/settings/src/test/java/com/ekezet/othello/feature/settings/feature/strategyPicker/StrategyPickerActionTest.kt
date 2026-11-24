package com.ekezet.othello.feature.settings.feature.strategyPicker

import com.ekezet.hurok.test.after
import com.ekezet.hurok.test.matches
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.game.strategy.HumanPlayer
import com.ekezet.othello.core.game.strategy.Strategy
import io.mockk.mockk
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.runner.RunWith
import kotlin.test.Test

@RunWith(JUnitParamsRunner::class)
internal class StrategyPickerActionTest {
    private val defaultModel = StrategyPickerModel()

    private val renderer = StrategyPickerRenderer()

    @Test
    @Parameters(method = "paramsForDiskAndStrategy")
    fun `showStrategyPickerFor should work correctly`(disk: Disk?, strategy: Strategy?) {
        val initModel = defaultModel

        val expectedModel = defaultModel.copy(selectFor = disk!!, selected = strategy, visible = true)

        initModel after showStrategyPickerFor(disk, strategy) matches {
            assertModel(expectedModel)
        }
    }

    @Test
    @Parameters(method = "paramsForDiskAndStrategy")
    fun `selectStrategy should work correctly`(disk: Disk?, strategy: Strategy?) {
        val initModel = defaultModel.copy(selectFor = disk!!, visible = true)

        val expectedModel = initModel.copy(visible = false)
        val expectedEffect = PublishSelectedStrategy(disk = expectedModel.selectFor, strategy = strategy)

        initModel after selectStrategy(strategy) matches {
            assertModel(expectedModel)
            assertEffects(setOf(expectedEffect))
        }
    }

    @Test
    fun `hideStrategyPicker should work correctly`() {
        val initModel = defaultModel.copy(visible = true)

        val expectedModel = defaultModel.copy(visible = false)

        initModel after hideStrategyPicker matches {
            assertModel(expectedModel)
        }
    }

    @Suppress("unused")
    private fun paramsForDiskAndStrategy() = arrayOf(
        arrayOf(Disk.Dark, mockk<Strategy>(relaxed = true)),
        arrayOf(Disk.Dark, HumanPlayer),
        arrayOf(Disk.Light, mockk<Strategy>(relaxed = true)),
        arrayOf(Disk.Light, HumanPlayer),
    )
}
