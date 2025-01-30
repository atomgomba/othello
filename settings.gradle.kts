import java.util.Properties

pluginManagement {
    includeBuild("build-logic")

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "othello"

include(
    ":app",
    ":app:baselineprofile",
    ":core:data",
    ":core:game",
    ":core:logging",
    ":core:ui",
    ":feature:game-board",
    ":feature:game-history",
    ":feature:settings",
)

fun Properties.find(key: String): String? =
    getProperty(key) ?: System.getenv(key)
