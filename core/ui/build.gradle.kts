plugins {
    id("othello.android.library")
    id("othello.android.library.compose")
}

android {
    namespace = "com.ekezet.othello.core.ui"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.game)

    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.material3)
    api(libs.kotlinx.immutable)

    implementation(libs.androidx.navigation.compose)

    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
}
