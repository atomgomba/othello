import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.ekezet.othello.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
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
        register("androidHurokApp") {
            id = "othello.android.app.hurok"
            implementationClass = "AndroidAppHurokConventionPlugin"
        }
        register("androidHurokLibrary") {
            id = "othello.android.library.hurok"
            implementationClass = "AndroidLibraryHurokConventionPlugin"
        }
    }
}
