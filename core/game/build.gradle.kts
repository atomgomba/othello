plugins {
    id("othello.android.library")
    id("othello.android.library.hurok")
}

android {
    namespace = "com.ekezet.othello.core.game"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:logging"))

    implementation(libs.kotlinx.coroutines.android)

    testImplementation(libs.junit4)
    testImplementation(libs.mockk)
}
