plugins {
    id("othello.android.library")
    id("othello.android.library.compose")
    id("othello.android.library.hurok")
}

android {
    namespace = "com.ekezet.othello.feature.gamesettings"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:game"))
    implementation(project(":core:ui"))
    implementation(project(":core:logging"))

    testImplementation(libs.junit4)
    testImplementation(libs.junitParams)
    testImplementation(libs.mockk)
}
