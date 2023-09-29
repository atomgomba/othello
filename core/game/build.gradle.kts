plugins {
    id("othello.android.library")
}

android {
    namespace = "com.ekezet.othello.core.game"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:logging"))

    testImplementation(libs.junit4)
}
