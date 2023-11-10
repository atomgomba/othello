package com.ekezet.othello.feature.gamesettings

import com.ekezet.hurok.test.EffectTest
import com.ekezet.hurok.test.triggers
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.game.store.GameSettingsStore
import com.ekezet.othello.core.game.strategy.HumanPlayer
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test

internal class GameSettingsEffectTest : EffectTest() {
    @MockK
    private lateinit var mockGameSettingsStore: GameSettingsStore

    private val initGameSettings = GameSettings()

    private lateinit var testLoop: GameSettingsLoop

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        testLoop = GameSettingsLoop(
            model = GameSettingsModel(),
            args = initGameSettings,
            dependency = GameSettingsDependency(
                gameSettingsStore = mockGameSettingsStore,
            ),
        )
            .startTest()
    }

    @Test
    fun `PublishGameSettings works correctly`() {
        val settings = initGameSettings.copy(lightStrategy = HumanPlayer)

        testLoop triggers PublishGameSettings(settings)

        coVerify { mockGameSettingsStore.update(settings) }
    }
}
