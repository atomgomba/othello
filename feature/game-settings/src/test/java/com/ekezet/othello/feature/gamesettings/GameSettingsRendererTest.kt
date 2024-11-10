package com.ekezet.othello.feature.gamesettings

import kotlin.test.Test
import kotlin.test.assertEquals

internal class GameSettingsRendererTest {
    private val subject = GameSettingsRenderer()

    @Test
    fun `renderState works correctly`() {
        val initModel = GameSettingsModel()

        val result = subject.renderState(initModel)

        val expectedState = GameSettingsState(
            darkStrategy = initModel.darkStrategy,
            lightStrategy = initModel.lightStrategy,
            isDarkPreferSides = false,
            isLightPreferSides = true,
            boardDisplayOptions = initModel.boardDisplayOptions,
            selectingStrategyFor = initModel.selectingStrategyFor,
            confirmExit = initModel.confirmExit,
        )

        assertEquals(expectedState, result)
    }
}
