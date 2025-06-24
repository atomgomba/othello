plugins {
    id("othello.android.library")
    id("othello.android.library.compose")
    id("othello.android.library.hurok")
}

android {
    namespace = "com.ekezet.othello.feature.gameboard"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.game)
    implementation(projects.core.logging)
    implementation(projects.core.ui)

    implementation(libs.kotlinx.immutable)
    implementation(libs.konfetti)

    testImplementation(libs.junit4)
    testImplementation(libs.junitParams)
    testImplementation(libs.mockk)
}
