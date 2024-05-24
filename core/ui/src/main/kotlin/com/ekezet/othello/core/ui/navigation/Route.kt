package com.ekezet.othello.core.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NamedNavArgument

abstract class Route {
    abstract val id: String
    abstract val icon: ImageVector
    abstract val labelRes: Int

    open val spec: String
        get() = id

    open val arguments: List<NamedNavArgument>?
        get() = null

    open fun make(params: Map<String, String?> = emptyMap()) =
        id
}
