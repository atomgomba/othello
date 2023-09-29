package com.ekezet.othello.di

import org.koin.core.qualifier.named

internal val MainScopeName
    inline get() = named("mainScope")
