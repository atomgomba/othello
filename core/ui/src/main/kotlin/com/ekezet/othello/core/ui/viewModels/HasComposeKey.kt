package com.ekezet.othello.core.ui.viewModels

interface HasComposeKey {
    val composeKey: Any?
        get() = javaClass.simpleName
}
