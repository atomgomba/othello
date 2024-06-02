plugins {
    id("othello.android.app")
    id("othello.android.app.versioning")
    id("othello.android.app.compose")
    alias(libs.plugins.android.application)
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "com.ekezet.othello"

    defaultConfig {
        applicationId = "com.ekezet.othello"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:game"))
    implementation(project(":core:logging"))
    implementation(project(":core:ui"))

    implementation(project(":feature:game-board"))
    implementation(project(":feature:game-history"))
    implementation(project(":feature:game-settings"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.compose.material3.adaptiveNavigationSuite)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.hurokBase)
    implementation(libs.hurokCompose)
    implementation(libs.androidx.profileinstaller)

    testImplementation(libs.junit4)
    testImplementation(libs.mockk)
    testImplementation(libs.hurokTest)
    "baselineProfile"(project(":app:baselineprofile"))
}
