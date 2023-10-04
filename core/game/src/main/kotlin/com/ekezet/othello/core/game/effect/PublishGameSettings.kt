package com.ekezet.othello.core.game.effect

import com.ekezet.hurok.Effect
import com.ekezet.hurok.ParentLoop
import com.ekezet.othello.core.game.data.IGameSettings
import com.ekezet.othello.core.game.dependency.HasGameSettingsStore

abstract class PublishGameSettings<TModel : Any, TDependency : HasGameSettingsStore> : Effect<TModel, TDependency> {
    abstract val settings: IGameSettings

    override suspend fun ParentLoop<TModel, TDependency>.trigger(dependency: TDependency?) = dependency?.run {
        gameSettingsStore.update(settings)
    }
}
