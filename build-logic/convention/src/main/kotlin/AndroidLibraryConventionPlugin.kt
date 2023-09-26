
import AndroidAppConventionPlugin.Companion.compileSdkLevel
import AndroidAppConventionPlugin.Companion.minSdkLevel
import com.android.build.api.dsl.LibraryExtension
import com.ekezet.othello.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            with(extensions) {
                configure<LibraryExtension> {
                    compileSdk = compileSdkLevel

                    defaultConfig.apply {
                        minSdk = minSdkLevel
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
