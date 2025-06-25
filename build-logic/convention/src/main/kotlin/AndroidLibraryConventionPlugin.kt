
import AndroidAppConventionPlugin.Companion.compileSdkLevel
import AndroidAppConventionPlugin.Companion.minSdkLevel
import com.android.build.api.dsl.LibraryExtension
import com.ekezet.othello.configureKotlinAndroid
import com.ekezet.othello.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("android-library").get().get().pluginId)
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
                add("testImplementation", libs.findLibrary("kotlinx-coroutines-test").get())
            }
        }
    }
}
