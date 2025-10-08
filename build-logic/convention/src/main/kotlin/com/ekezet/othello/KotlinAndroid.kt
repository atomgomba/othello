package com.ekezet.othello

import AndroidAppConventionPlugin
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

private val javaVersion = JavaVersion.VERSION_21

/**
 * Configure base Kotlin with Android options
 */
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    with(pluginManager) {
        apply(libs.findPlugin("kotlin-android").get().get().pluginId)
    }

    commonExtension.apply {
        compileSdk = AndroidAppConventionPlugin.compileSdkLevel

        defaultConfig {
            minSdk = AndroidAppConventionPlugin.minSdkLevel
        }

        compileOptions {
            sourceCompatibility = javaVersion
            targetCompatibility = javaVersion
            isCoreLibraryDesugaringEnabled = true
        }
    }

    configureKotlin()
    configureCoverage()
    configureKoin()
    configureDetekt()

    dependencies {
        add("coreLibraryDesugaring", libs.findLibrary("android.desugarJdkLibs").get())
    }
}

private fun Project.configureKotlin() {
    // Use withType to workaround https://youtrack.jetbrains.com/issue/KT-55947
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(javaVersion.toString()))
            val warningsAsErrors: String? by project
            allWarningsAsErrors.set(warningsAsErrors.toBoolean())
            freeCompilerArgs.addAll(
                "-opt-in=kotlin.RequiresOptIn",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.FlowPreview",
            )
        }
    }
}
