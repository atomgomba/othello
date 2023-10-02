plugins {
    id("othello.android.library")
    id("othello.android.library.compose")
}

android {
    namespace = "com.ekezet.othello.feature.gameboard"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:game"))
    implementation(project(":core:logging"))
    implementation(project(":core:ui"))

    implementation(libs.hurokBase)
    implementation(libs.hurokCompose)

    implementation(libs.konfetti)
}
