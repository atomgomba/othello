package com.ekezet.othello.core.game.effect

import com.ekezet.hurok.ActionEmitter
import com.ekezet.hurok.Effect
import com.ekezet.othello.core.game.data.IGameSettings
import com.ekezet.othello.core.game.store.HasGameSettingsStore

abstract class PublishGameSettings<TModel : Any, TDependency : HasGameSettingsStore> : Effect<TModel, TDependency> {
    abstract val settings: IGameSettings

    override suspend fun ActionEmitter<TModel, TDependency>.trigger(dependency: TDependency?) = dependency?.run {
        gameSettingsStore.update(settings)
    }
}
