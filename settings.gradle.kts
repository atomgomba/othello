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
        mavenLocal()
        google()
        mavenCentral()
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/atomgomba/hurok")
            credentials {
                Properties().apply {
                    runCatching {
                        load(file("local.properties").inputStream())
                    }

                    username = find("GHP_ACTOR")
                    password = find("GHP_TOKEN")
                }
            }
        }
    }
}

rootProject.name = "othello"

include(":app")

include(":core:data")
include(":core:game")
include(":core:logging")
include(":core:ui")

include(":feature:game-board")
include(":feature:game-settings")

fun Properties.find(key: String): String? =
    getProperty(key) ?: System.getenv(key)
