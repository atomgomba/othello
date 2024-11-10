package com.ekezet.othello.feature.settings

import kotlin.test.Test
import kotlin.test.assertEquals

internal class SettingsRendererTest {
    private val subject = SettingsRenderer()

    @Test
    fun `renderState works correctly`() {
        val initModel = SettingsModel()

        val result = subject.renderState(initModel)

        val expectedState = SettingsState(
            darkStrategy = initModel.darkStrategy,
            lightStrategy = initModel.lightStrategy,
            isDarkPreferSides = false,
            isLightPreferSides = true,
            boardDisplayOptions = initModel.boardDisplayOptions,
            historyDisplayOptions = initModel.historyDisplayOptions,
            selectingStrategyFor = initModel.selectingStrategyFor,
            confirmExit = initModel.confirmExit,
        )

        assertEquals(expectedState, result)
    }
}
