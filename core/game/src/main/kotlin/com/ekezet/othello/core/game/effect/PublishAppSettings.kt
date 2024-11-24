package com.ekezet.othello.core.game.effect

import com.ekezet.hurok.ActionEmitter
import com.ekezet.hurok.Effect
import com.ekezet.othello.core.game.data.IAppSettings
import com.ekezet.othello.core.game.dependency.AppSettingsPublisher

abstract class PublishAppSettings<TModel : Any, TDependency : AppSettingsPublisher> : Effect<TModel, TDependency> {
    abstract val settings: IAppSettings

    override suspend fun ActionEmitter<TModel, TDependency>.trigger(dependency: TDependency?) = dependency?.run {
        appSettingsStore.update(settings)
    }
}
