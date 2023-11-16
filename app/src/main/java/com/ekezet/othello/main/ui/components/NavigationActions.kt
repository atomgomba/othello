package com.ekezet.othello.main.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import com.ekezet.othello.main.navigation.MainRoutes

@Composable
internal fun NavigationActions(currentDestination: String, onClick: (String) -> Unit) {
    MainRoutes.All.forEach { destination ->
        IconToggleButton(
            checked = currentDestination == destination.id,
            onCheckedChange = { onClick(destination.id) },
        ) {
            Icon(
                imageVector = destination.icon,
                contentDescription = null,
            )
        }
    }
}
