import kotlinx.kover.gradle.plugin.dsl.KoverReportExtension

plugins {
    id("com.android.application") version libs.versions.androidGradlePlugin apply false
    id("com.android.library") version libs.versions.androidGradlePlugin apply false
    id("org.jetbrains.kotlin.android") version libs.versions.kotlin apply false
    id("org.jetbrains.kotlinx.kover") version libs.versions.kotlinxKover apply false
}

allprojects {
    apply(plugin = "org.jetbrains.kotlinx.kover")

    configure<KoverReportExtension> {
        filters {
            excludes {
                annotatedBy("Composable", "Immutable", "Stable", "*ExcludeFromCoverage")
                classes("*.BuildConfig")
                packages("*.ui", "*.ui.*", "*.di", "*.di.*")
            }
        }
    }
}
