package com.ekezet.othello

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    with(commonExtension) {
        with(pluginManager) {
            apply(libs.findPlugin("compose-compiler").get().get().pluginId)
        }

        buildFeatures {
            compose = true
        }

        dependencies {
            val bom = libs.findLibrary("androidx-compose-bom").get()
            add("implementation", platform(bom))
            add("androidTestImplementation", platform(bom))
            add("debugImplementation", libs.findLibrary("androidx.compose.ui.testManifest").get())
            add("debugImplementation", libs.findLibrary("androidx.compose.ui.tooling").get())
        }
    }

    configure<ComposeCompilerGradlePluginExtension> {
        enableStrongSkippingMode = true
        includeSourceInformation = true
    }
}
