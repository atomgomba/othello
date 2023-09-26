plugins {
    id("othello.android.library")
}

android {
    namespace = "com.ekezet.othello.core.game"
}

dependencies {
    implementation(project(":core:data"))
    implementation(libs.timber)

    testImplementation(libs.junit4)
}
