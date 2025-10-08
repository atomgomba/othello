
import com.android.build.api.dsl.ApplicationExtension
import com.ekezet.othello.configureKotlinAndroid
import com.ekezet.othello.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidAppConventionPlugin : Plugin<Project> {
    companion object {
        const val compileSdkLevel = 36
        const val minSdkLevel = 26
    }

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("android-application").get().get().pluginId)
            }

            with(extensions) {
                configure<ApplicationExtension> {
                    compileSdk = compileSdkLevel

                    defaultConfig.apply {
                        minSdk = minSdkLevel
                        targetSdk = compileSdkLevel
                    }

                    buildFeatures {
                        buildConfig = true
                    }

                    configureKotlinAndroid(this)
                }
            }

            dependencies {
                add("testImplementation", kotlin("test"))
            }
        }
    }
}
