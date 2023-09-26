import com.android.build.api.dsl.ApplicationExtension
import com.ekezet.othello.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidAppConventionPlugin : Plugin<Project> {
    companion object {
        const val compileSdkLevel = 34
        const val minSdkLevel = 24
    }

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            with(extensions) {
                configure<ApplicationExtension> {
                    compileSdk = compileSdkLevel

                    defaultConfig.apply {
                        minSdk = minSdkLevel
                        targetSdk = compileSdkLevel
                    }

                    configureKotlinAndroid(this)
                }
            }
        }
    }
}
