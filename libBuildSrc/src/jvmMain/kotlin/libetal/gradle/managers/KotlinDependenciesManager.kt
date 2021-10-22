package libetal.gradle.managers

import libetal.gradle.enums.Sources
import libetal.gradle.enums.Types
import libetal.gradle.kapt
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

class KotlinDependenciesManager(handler: KotlinDependencyHandler, private val project: Project) :
    DependenciesManager<KotlinDependencyHandler, KotlinDependenciesManager, KotlinDependenciesManager>(handler) {

    override fun KotlinDependencyHandler.getSource(dependency: Any): Any = with(project) {
        when (implementationSource) {
            Sources.PROJECT -> project(dependency as String)
            Sources.REMOTE -> dependency
            Sources.JAR -> files(dependency)
            Sources.KOTLIN -> kotlin(dependency as String)
        }
    }

    override fun KotlinDependencyHandler.apply(dependency: Any) = with(project) {
        when (implementationType) {
            Types.KAPT -> {
                println("""kapt($dependency)""")
                kapt(dependency as String)
            }
            Types.COMPILE -> {
                println("""compileOnly($dependency)""")
                compileOnly(dependency)
            }
            Types.GRADLE -> {
                println("""kotlin implementation($dependency)""")
                implementation(dependency)
            }
            Types.GRADLE_TEST -> {
                println("""testImplementation($dependency)""")
                throw RuntimeException(
                    """|
                | Test Implementations in kotlin multiplatform need to be in sourceTest Source set i.e
                | val jvmMain by getting{
                |     dependencies{
                |        // project dependencies added here
                |     }
                | }
                | 
                | val jvmTest by getting{
                |     dependencies{
                |         // Test dependencies added here
                |     }
                | }
            """.trimMargin()
                )
            }
        }
    }


}


