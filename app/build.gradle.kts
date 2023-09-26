plugins {
    id("othello.android.app")
    id("othello.android.app.versioning")
    id("othello.android.app.compose")
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
    implementation(libs.androidx.activity.compose)

    implementation(libs.hurokBase)
    implementation(libs.hurokCompose)

    implementation(project(":core:ui"))
    implementation(project(":core:data"))
    implementation(project(":core:game"))

    implementation(project(":feature:game-board"))
}
