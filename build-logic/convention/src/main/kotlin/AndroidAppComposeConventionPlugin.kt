
import com.android.build.api.dsl.ApplicationExtension
import com.ekezet.othello.configureAndroidCompose
import com.ekezet.othello.configureAndroidKoinCompose
import com.ekezet.othello.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidAppComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("android-application").get().get().pluginId)
            }
            val extension = extensions.getByType<ApplicationExtension>()
            configureAndroidCompose(extension)
            configureAndroidKoinCompose(extension)
        }
    }
}
