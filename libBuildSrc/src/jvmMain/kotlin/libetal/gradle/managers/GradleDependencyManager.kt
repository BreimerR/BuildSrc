@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package libetal.gradle.managers

import libetal.gradle.compileOnly
import libetal.gradle.enums.Sources
import libetal.gradle.enums.Types
import libetal.gradle.implementation
import libetal.gradle.testImplementation
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency

open class GradleDependencyManager(project: Project) :
    DependenciesManager<Project, GradleDependencyManager, GradleDependencyManager>(project) {

    fun Project.kapt(dependencyNotation: Any): Dependency? =
        with(dependencies) { add("kapt", dependencyNotation) }

    fun apply(dependency: Any) {
        with(handler) {
            when (implementationType) {
                libetal.gradle.enums.Types.KAPT -> {
                    kapt(dependency)
                }
                libetal.gradle.enums.Types.COMPILE -> {
                    compileOnly(dependency)
                }
                libetal.gradle.enums.Types.GRADLE -> {

                    implementation(dependency)
                }
                libetal.gradle.enums.Types.GRADLE_TEST -> {

                    testImplementation(dependency)
                }
            }
        }
    }


    fun apply(libString: String, version: String = "") {

        with(handler) {

            val implementation: Any = when (implementationSource) {
                Sources.PROJECT -> {
                    println("""project(":$libString")""")
                    project(":$libString")
                }
                Sources.REMOTE -> {
                    println(""""$libString:$version"""")
                    "$libString:$version"
                }
                Sources.JAR -> {
                    println("""files("$libString$version.jar")""")
                    files("$libString$version.jar")
                }
                Sources.KOTLIN -> {
                    println("""kotlin("$libString,"$version")""")
                    TODO("Add Implementation on this: org.gradle.kotlin.dsl dependency not known by me")
                }
            }

            apply(implementation)
        }
    }

    override fun Project.getSource(dependency: Any) = when (implementationSource) {
        Sources.PROJECT -> project(dependency as String)
        Sources.REMOTE -> dependency
        Sources.JAR -> dependency
        Sources.KOTLIN -> with(project) {
            with(dependencies) {
                TODO("kotlin(dependency as String). org.gradle.kotlin.dsl.kotlin dependency not known")
            }
        }
    }

    override fun Project.apply(dependency: Any): Dependency? = when (implementationType) {
        Types.KAPT -> {
            println("""kapt($dependency)""")
            kapt(dependency)
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
            testImplementation(dependency)
        }
    }


}


