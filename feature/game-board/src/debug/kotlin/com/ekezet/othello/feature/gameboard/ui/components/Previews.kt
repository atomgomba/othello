package com.ekezet.othello.feature.gameboard.ui.components

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import com.ekezet.hurok.renderState
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.game.GameEnd
import com.ekezet.othello.core.ui.components.PreviewBase
import com.ekezet.othello.feature.gameboard.GameBoardLoop
import com.ekezet.othello.feature.gameboard.GameBoardModel
import com.ekezet.othello.feature.gameboard.GameBoardRenderer
import com.ekezet.othello.feature.gameboard.ui.GameBoardViewImpl

private val defaultModel = GameBoardModel()

private val grayscaleModel = defaultModel.copy(
    boardDisplayOptions = defaultModel.boardDisplayOptions.copy(
        isGrayscaleMode = true,
    )
)

private val showPositionsModel = defaultModel.copy(
    boardDisplayOptions = defaultModel.boardDisplayOptions.copy(
        showBoardPositions = true,
    ),
)

private val darkPassedModel = defaultModel.copy(
    passed = true,
)

private val lightPassedModel = defaultModel.copy(
    passed = true,
    gameState = defaultModel.currentGameState.proceed(2 to 3).state,
)

private val darkWinModel = defaultModel.copy(
    ended = GameEnd.EndedWin(Disk.Dark),
)

private val tieGameModel = defaultModel.copy(
    ended = GameEnd.EndedTie,
)

private fun GameBoardModel.toPreviewState() =
    renderState(::GameBoardLoop, this, GameBoardRenderer())

@ExperimentalLayoutApi
@Composable
private fun GameBoardModel.Preview() = toPreviewState()
    .GameBoardViewImpl(
        onStrategyClick = {},
    )

@ExperimentalLayoutApi
@Preview
@Composable
private fun GameBoardPreview_Default() {
    PreviewBase {
        defaultModel.Preview()
    }
}

@ExperimentalLayoutApi
@Preview
@Composable
private fun GameBoardPreview_ShowPositions() {
    PreviewBase {
        showPositionsModel.Preview()
    }
}

@ExperimentalLayoutApi
@Preview
@Composable
private fun GameBoardPreview_DarkPassed() {
    PreviewBase {
        darkPassedModel.Preview()
    }
}

@ExperimentalLayoutApi
@Preview
@Composable
private fun GameBoardPreview_LightPassed() {
    PreviewBase {
        lightPassedModel.Preview()
    }
}

@ExperimentalLayoutApi
@Preview
@Composable
private fun GameBoardPreview_DarkWin() {
    PreviewBase {
        darkWinModel.Preview()
    }
}

@ExperimentalLayoutApi
@Preview
@Composable
private fun GameBoardPreview_TieGame() {
    PreviewBase {
        tieGameModel.Preview()
    }
}

@ExperimentalLayoutApi
@Preview
@Composable
private fun GameBoardPreview_Grayscale() {
    PreviewBase {
        grayscaleModel.Preview()
    }
}

@ExperimentalLayoutApi
@Preview(device = TABLET)
@Composable
private fun GameBoardPreview_Default_Tablet() {
    PreviewBase {
        defaultModel.Preview()
    }
}
