package com.ekezet.othello

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidKoinCompose() {
    dependencies {
        add("implementation", libs.findLibrary("koin.androidx.compose").get())
    }
}
