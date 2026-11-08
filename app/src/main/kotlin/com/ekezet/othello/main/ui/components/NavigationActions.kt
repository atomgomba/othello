package com.ekezet.othello.main.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.ui.res.stringResource
import com.ekezet.othello.main.navigation.Screen
import kotlin.reflect.KClass

internal fun NavigationSuiteScope.navigationActions(
    currentDestinationClass: KClass<out Screen>,
    onClick: (Screen) -> Unit,
    hasGameHistory: Boolean,
) = Screen.All.forEach { destination ->
    item(
        icon = {
            Icon(
                imageVector = destination.icon,
                contentDescription = stringResource(id = destination.labelRes),
            )
        },
        label = { Text(text = stringResource(id = destination.labelRes)) },
        selected = currentDestinationClass == destination::class,
        onClick = { onClick(destination) },
        enabled = when (destination) {
            Screen.GameHistory -> hasGameHistory
            else -> true
        },
    )
}
