plugins {
    `kotlin-dsl`
}

group = "com.ekezet.othello.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.detekt.gradlePlugin)
    compileOnly(libs.kotlin.composeCompiler.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.kover.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApp") {
            id = "othello.android.app"
            implementationClass = "AndroidAppConventionPlugin"
        }
        register("androidAppVersioning") {
            id = "othello.android.app.versioning"
            implementationClass = "AndroidAppVersioningConventionPlugin"
        }
        register("androidLibrary") {
            id = "othello.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidAppCompose") {
            id = "othello.android.app.compose"
            implementationClass = "AndroidAppComposeConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "othello.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("hurok") {
            id = "othello.hurok"
            implementationClass = "HurokConventionPlugin"
        }
    }
}
