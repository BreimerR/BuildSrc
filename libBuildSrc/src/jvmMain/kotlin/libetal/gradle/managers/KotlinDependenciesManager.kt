package libetal.gradle.managers

import libetal.gradle.enums.Sources
import libetal.gradle.enums.Types
import libetal.gradle.kapt
import org.jetbrains.kotlin.gradle.plugin.mpp.DefaultKotlinDependencyHandler

class KotlinDependenciesManager(handler: DefaultKotlinDependencyHandler) :
    DependenciesManager<DefaultKotlinDependencyHandler, KotlinDependenciesManager, KotlinDependenciesManager>(handler) {

    override fun DefaultKotlinDependencyHandler.getSource(dependency: Any): Any = with(project) {
        when (implementationSource) {
            Sources.PROJECT -> project(dependency as String)
            Sources.REMOTE -> dependency
            Sources.JAR -> files(dependency)
            Sources.KOTLIN -> kotlin(dependency as String)
        }
    }

    override fun DefaultKotlinDependencyHandler.apply(dependency: Any) = with(project) {
        when (implementationType) {
            Types.KAPT -> {
                println("""kapt($dependency)""")

                when (dependency) {
                    is Pair<*, *> -> {
                        val (string, version) = dependency

                        kapt(string as String, version = version as String)
                    }
                    else -> throw Exception("Kotlin Dependency has to be a pair of name and version")
                }
            }
            Types.COMPILE -> {
                println("""compileOnly($dependency)""")
                compileOnly(dependency)
            }
            Types.GRADLE -> {
                println("""implementation($dependency)""")
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

