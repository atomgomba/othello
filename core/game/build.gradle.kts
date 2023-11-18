plugins {
    id("othello.android.library")
}

android {
    namespace = "com.ekezet.othello.core.game"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:logging"))

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.hurokBase)

    testImplementation(libs.junit4)
}
