plugins {
    id("othello.android.library")
}

android {
    namespace = "com.ekezet.othello.core.game"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)

    implementation(project(":core:data"))
    implementation(project(":core:logging"))

    testImplementation(libs.junit4)
}
