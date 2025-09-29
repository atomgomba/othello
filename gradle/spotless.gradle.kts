import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin

initscript {
    val spotlessVersion = "8.0.0"

    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("com.diffplug.spotless:spotless-plugin-gradle:$spotlessVersion")
    }
}

val ktlintRules = mapOf(
    "klint_max_line_length" to 120,
    "ktlint_code_style" to "intellij_idea",
    "ktlint_function_naming_ignore_when_annotated_with" to "Composable",
    "ktlint_function_signature_body_expression_wrapping" to "default",
    "ktlint_standard_multiline-expression-wrapping" to "disabled",
    "ktlint_standard_property-naming" to "disabled",
)

rootProject {
    subprojects {
        apply<SpotlessPlugin>()

        extensions.configure<SpotlessExtension> {
            kotlin {
                target("**/*.kt")
                targetExclude("**/build/**/*.kt")

                ktlint().editorConfigOverride(ktlintRules)
            }
        }
    }
}
