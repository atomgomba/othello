package com.ekezet.othello.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ekezet.hurok.compose.LoopWrapper
import com.ekezet.othello.MainAction
import com.ekezet.othello.MainAction.OnNewGameClicked
import com.ekezet.othello.MainAction.OnToggleIndicatorsClicked
import com.ekezet.othello.MainDependency
import com.ekezet.othello.MainModel
import com.ekezet.othello.MainScope
import com.ekezet.othello.MainState
import com.ekezet.othello.R.string
import com.ekezet.othello.core.ui.R
import com.ekezet.othello.di.MainScopeName
import com.ekezet.othello.feature.gameboard.DisplayOptions
import com.ekezet.othello.feature.gameboard.ui.GameBoardView
import org.koin.compose.koinInject

@Composable
internal fun MainView(
    loopScope: MainScope = koinInject(MainScopeName),
) {
    LoopWrapper<MainState, MainModel, Unit, MainDependency, MainAction>(
        builder = { loopScope },
    ) { state ->
        MainViewImpl(state)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScope.MainViewImpl(state: MainState) = with(state) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text(stringResource(string.app_name)) },
                navigationIcon = { },
                actions = { Toolbar(gameSettings.displayOptions) },
            )
        },
        bottomBar = {
            BottomAppBar { }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            GameBoardView(
                args = gameBoardArgs,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Center),
            )
        }
    }
}

@Composable
private fun MainScope.Toolbar(options: DisplayOptions) = with(options) {
    IconButton(onClick = { emit(OnNewGameClicked) }) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = stringResource(R.string.main__menu__new_game),
        )
    }

    IconButton(onClick = { emit(OnToggleIndicatorsClicked) }) {
        Icon(
            imageVector = if (showPossibleMoves) Icons.Filled.LocationOn else Icons.Outlined.LocationOn,
            contentDescription = stringResource(
                R.string.main__menu__toggle_indicators
            ),
        )
    }
}
