package com.ekezet.othello.feature.settings

import com.ekezet.hurok.test.EffectTest
import com.ekezet.othello.core.game.data.Default
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.game.data.HistorySettings
import com.ekezet.othello.core.game.store.GameHistoryStore
import com.ekezet.othello.core.game.store.GameSettingsStore
import com.ekezet.othello.core.game.store.HistorySettingsStore
import com.ekezet.othello.core.game.strategy.HumanPlayer
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import kotlin.test.Test

internal class SettingsEffectTest : EffectTest() {
    @MockK
    private lateinit var mockGameSettingsStore: GameSettingsStore

    @MockK
    private lateinit var mockHistorySettingsStore: HistorySettingsStore

    @MockK
    private lateinit var mockGameHistoryStore: GameHistoryStore

    private val initGameSettings = GameSettings.Default
    private val initHistorySettings = HistorySettings.Default

    private lateinit var dependency: SettingsDependency

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        dependency = SettingsDependency(
            gameSettingsStore = mockGameSettingsStore,
            historySettingsStore = mockHistorySettingsStore,
            gameHistoryStore = mockGameHistoryStore,
        )
    }

    @Test
    fun `PublishGameSettings works correctly`() {
        val settings = initGameSettings.copy()

        coEvery { mockGameSettingsStore.update(settings) } just runs
        coEvery { mockGameSettingsStore.settings } returns MutableStateFlow(initGameSettings)

        dependency runWith PublishGameSettings(settings)

        coVerify {
            mockGameSettingsStore.update(settings)
            mockGameSettingsStore.settings
        }

        confirmAllVerified()
    }

    @Test
    fun `PublishGameSettings works correctly if strategy changed`() {
        val settings = initGameSettings.copy(lightStrategy = HumanPlayer)

        coEvery { mockGameSettingsStore.update(settings) } just runs
        coEvery { mockGameSettingsStore.settings } returns MutableStateFlow(initGameSettings)
        coEvery { mockGameHistoryStore.reset(any(), any()) } just runs

        dependency runWith PublishGameSettings(settings)

        coVerify {
            mockGameSettingsStore.update(settings)
            mockGameSettingsStore.settings
            mockGameHistoryStore.reset(emptyList(), null)
        }

        confirmAllVerified()
    }
    @Test
    fun `PublishHistorySettings works correctly`() {
        val settings = initHistorySettings.copy()

        coEvery { mockHistorySettingsStore.update(settings) } just runs
        coEvery { mockHistorySettingsStore.settings } returns MutableStateFlow(initHistorySettings)

        dependency runWith PublishHistorySettings(settings)

        coVerify {
            mockHistorySettingsStore.update(settings)
        }

        confirmAllVerified()
    }


    private fun confirmAllVerified() {
        confirmVerified(
            mockGameSettingsStore,
            mockHistorySettingsStore,
            mockGameHistoryStore,
        )
    }
}
