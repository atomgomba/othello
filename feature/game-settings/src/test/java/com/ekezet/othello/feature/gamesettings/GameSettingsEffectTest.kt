package com.ekezet.othello.feature.gamesettings

import com.ekezet.hurok.test.EffectTest
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.game.store.GameSettingsStore
import com.ekezet.othello.core.game.strategy.HumanPlayer
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import org.junit.Before
import org.junit.Test

internal class GameSettingsEffectTest : EffectTest() {
    @MockK
    private lateinit var mockGameSettingsStore: GameSettingsStore

    private val initGameSettings = GameSettings()

    private lateinit var dependency: GameGameSettingsDependency

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        dependency = GameGameSettingsDependency(
            gameSettingsStore = mockGameSettingsStore,
        )
    }

    @Test
    fun `PublishGameSettings works correctly`() {
        val settings = initGameSettings.copy(lightStrategy = HumanPlayer)

        coEvery { mockGameSettingsStore.update(settings) } just runs

        dependency runWith PublishGameSettings(settings)

        coVerify { mockGameSettingsStore.update(settings) }

        confirmVerified(mockGameSettingsStore)
    }
}
