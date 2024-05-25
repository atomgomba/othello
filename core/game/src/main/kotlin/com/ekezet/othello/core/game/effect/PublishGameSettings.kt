package com.ekezet.othello.core.game.effect

import com.ekezet.hurok.ActionEmitter
import com.ekezet.hurok.Effect
import com.ekezet.othello.core.game.data.IGameSettings
import com.ekezet.othello.core.game.dependency.GameSettingsPublisher

abstract class PublishGameSettings<TModel : Any, TDependency : GameSettingsPublisher> : Effect<TModel, TDependency> {
    abstract val settings: IGameSettings

    override suspend fun ActionEmitter<TModel, TDependency>.trigger(dependency: TDependency?) = dependency?.run {
        val currentSettings: IGameSettings = gameSettingsStore.settings.value
        gameSettingsStore.update(settings)
        if (currentSettings.containsDifferentStrategy(settings)) {
            moveHistoryStore.reset()
        }
    }
}
