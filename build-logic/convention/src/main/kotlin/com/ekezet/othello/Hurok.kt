package com.ekezet.othello

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureHurok() {
    dependencies {
        add("implementation", libs.findLibrary("hurok-base").get())
        add("implementation", libs.findLibrary("hurok-compose").get())
        add("testImplementation", libs.findLibrary("hurok-test").get())
    }
}
