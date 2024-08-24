package com.ekezet.othello

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureHurok(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        dependencies {
            add("implementation", libs.findLibrary("hurokBase").get())
            add("implementation", libs.findLibrary("hurokCompose").get())
            add("testImplementation", libs.findLibrary("hurokTest").get())
        }
    }
}
