package com.ekezet.othello

import com.android.build.api.dsl.CommonExtension
import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureCoverage(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        with(pluginManager) {
            apply(libs.findPlugin("kotlinx-kover").get().get().pluginId)
        }
    }

    dependencies {
        val bom = libs.findLibrary("koin.bom").get()
        add("implementation", platform(bom))
        add("implementation", libs.findLibrary("koin.core").get())
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
