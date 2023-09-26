import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidAppVersioningConventionPlugin : Plugin<Project> {
    companion object {
        const val major = 1
        const val minor = 0
        const val patch = 0
    }

    private val versionCode: Int
        get() = major * 10000 + minor * 1000 + patch

    private val versionName: String
        get() = "$major.$minor.$patch"

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.application")

            extensions.configure<ApplicationExtension> {
                defaultConfig.versionCode = versionCode
                defaultConfig.versionName = versionName
            }
        }
    }
}
