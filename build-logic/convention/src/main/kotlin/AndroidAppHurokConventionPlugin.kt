
import com.ekezet.othello.configureHurok
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidAppHurokConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            configureHurok()
        }
    }
}
