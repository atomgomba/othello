plugins {
    id("othello.android.library")
    id("othello.android.library.compose")
}

android {
    namespace = "com.ekezet.othello.core.ui"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:game"))

    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.material3)

    implementation(libs.androidx.navigation.compose)
}
