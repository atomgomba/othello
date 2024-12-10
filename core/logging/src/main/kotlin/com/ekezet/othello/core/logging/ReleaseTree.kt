package com.ekezet.othello.core.logging

import timber.log.Timber.Tree

class ReleaseTree : Tree() {
    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?,
    ) {
        // TODO
    }
}
