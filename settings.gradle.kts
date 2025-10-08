enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

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
