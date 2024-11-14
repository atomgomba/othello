package com.ekezet.othello

import com.android.build.api.dsl.CommonExtension
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureDetekt(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        with(pluginManager) {
            apply(libs.findPlugin("detekt").get().get().pluginId)
        }
    }

    configure<DetektExtension> {
        autoCorrect = true
    }

    dependencies {
        add("detektPlugins", libs.findLibrary("detekt-formatting").get())
    }
}
