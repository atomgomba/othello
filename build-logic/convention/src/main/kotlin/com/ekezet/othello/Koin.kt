package com.ekezet.othello

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureKoin() {
    dependencies {
        val bom = libs.findLibrary("koin.bom").get()
        add("implementation", platform(bom))
        add("implementation", libs.findLibrary("koin.core").get())
    }
}
