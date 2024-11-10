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

        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/atomgomba/hurok")
            credentials {
                Properties().apply {
                    runCatching {
                        load(file("local.properties").inputStream())
                    }.onFailure {
                        logger.warn("local.properties could not be loaded")
                    }

                    username = find("GPR_USERNAME")
                    password = find("GPR_TOKEN")
                }
            }
        }
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
