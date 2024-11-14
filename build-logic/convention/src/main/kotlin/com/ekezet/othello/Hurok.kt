package com.ekezet.othello

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureHurok() {
    dependencies {
        add("implementation", libs.findLibrary("hurokBase").get())
        add("implementation", libs.findLibrary("hurokCompose").get())
        add("testImplementation", libs.findLibrary("hurokTest").get())
    }
}
