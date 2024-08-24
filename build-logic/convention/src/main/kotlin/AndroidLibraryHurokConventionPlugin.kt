import com.android.build.api.dsl.LibraryExtension
import com.ekezet.othello.configureHurok
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryHurokConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(extensions) {
                configure<LibraryExtension> {
                    configureHurok(this)
                }
            }
        }
    }
}
