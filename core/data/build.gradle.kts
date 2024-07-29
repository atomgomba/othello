plugins {
    id("othello.android.library")
}

android {
    namespace = "com.ekezet.othello.core.data"
}

dependencies {
    testImplementation(libs.junit4)
}
