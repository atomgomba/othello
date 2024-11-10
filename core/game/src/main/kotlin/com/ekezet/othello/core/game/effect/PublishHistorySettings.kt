package com.ekezet.othello.core.game.effect

import com.ekezet.hurok.ActionEmitter
import com.ekezet.hurok.Effect
import com.ekezet.othello.core.game.data.IHistorySettings
import com.ekezet.othello.core.game.dependency.HistorySettingsPublisher

abstract class PublishHistorySettings<TModel : Any, TDependency : HistorySettingsPublisher> : Effect<TModel, TDependency> {
    abstract val settings: IHistorySettings

    override suspend fun ActionEmitter<TModel, TDependency>.trigger(dependency: TDependency?) = dependency?.run {
        historySettingsStore.update(settings)
    }
}
