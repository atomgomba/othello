package com.ekezet.othello.main.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.ui.res.stringResource
import com.ekezet.othello.main.navigation.MainRoutes

internal fun NavigationSuiteScope.navigationActions(currentDestination: String, onClick: (String) -> Unit) {
    MainRoutes.All.forEach { destination ->
        item(
            icon = {
                Icon(
                    imageVector = destination.icon,
                    contentDescription = stringResource(id = destination.labelRes)
                ) },
            label = { Text(text = stringResource(id = destination.labelRes)) },
            selected = currentDestination == destination.id,
            onClick = { onClick(destination.id) },
        )
    }
}
