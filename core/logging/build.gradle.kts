plugins {
    id("othello.android.library")
}

android {
    namespace = "com.ekezet.othello.core.logging"
}

dependencies {
    api(libs.timber)
}
