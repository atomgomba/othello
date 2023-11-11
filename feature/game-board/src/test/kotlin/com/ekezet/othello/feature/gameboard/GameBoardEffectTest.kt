package com.ekezet.othello.feature.gameboard

import com.ekezet.hurok.test.EffectTest
import com.ekezet.othello.core.game.data.GameSettings
import io.mockk.MockKAnnotations
import org.junit.Before

class GameBoardEffectTest : EffectTest() {
    private lateinit var testLoop: GameBoardLoop

    private val initGameSettings = GameSettings()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        testLoop = GameBoardLoop(
            model = GameBoardModel(),
            args = initGameSettings,
        )
            .startTest()
    }
}
