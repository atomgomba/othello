
import com.ekezet.othello.configureHurok
import org.gradle.api.Plugin
import org.gradle.api.Project

class HurokConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            configureHurok()
        }
    }
}
