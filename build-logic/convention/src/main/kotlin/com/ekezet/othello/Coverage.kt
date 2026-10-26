package com.ekezet.othello

import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureCoverage() {
    with(pluginManager) {
        apply(libs.findPlugin("kotlinx-kover").get().get().pluginId)
    }

    dependencies {
        add("kover", project(path))
    }

    configure<KoverProjectExtension> {
        reports {
            filters {
                excludes {
                    annotatedBy("*Composable", "*ExcludeFromCoverage")
                    classes("*.BuildConfig")
                    packages("*.ui", "*.ui.*", "*.di", "*.di.*")
                }
            }
        }
    }
}
