
import com.android.build.api.dsl.LibraryExtension
import com.ekezet.othello.configureAndroidCompose
import com.ekezet.othello.configureAndroidKoinCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
            }
            val extension = extensions.getByType<LibraryExtension>()
            configureAndroidCompose(extension)
            configureAndroidKoinCompose(extension)
        }
    }
}
