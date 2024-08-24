
import com.android.build.api.dsl.ApplicationExtension
import com.ekezet.othello.configureHurok
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidAppHurokConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(extensions) {
                configure<ApplicationExtension> {
                    configureHurok(this)
                }
            }
        }
    }
}
