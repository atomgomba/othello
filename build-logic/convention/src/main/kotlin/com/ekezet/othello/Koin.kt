package com.ekezet.othello

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureKoin(
    commonExtension: CommonExtension<*, *, *, *, *>,
) {
    commonExtension.apply {
        dependencies {
            val bom = libs.findLibrary("koin.bom").get()
            add("implementation", platform(bom))
            add("implementation", libs.findLibrary("koin.core").get())
            add("implementation", libs.findLibrary("koin.android").get())
        }
    }
}
