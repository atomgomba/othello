package com.ekezet.othello

import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import java.io.File

internal fun Project.configureDetekt() {
    with(pluginManager) {
        apply(libs.findPlugin("detekt").get().get().pluginId)
    }

    dependencies {
        add("detektPlugins", libs.findLibrary("detekt-formatting").get())
    }

    configure<DetektExtension> {
        autoCorrect = true
        config.setFrom(File(rootProject.rootDir, "config/detekt/detekt.yml"))
    }
}
