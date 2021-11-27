@file:Suppress("unused")

package libetal.gradle

import libetal.gradle.managers.DependenciesManager
import libetal.gradle.managers.GradleDependencyManager
import libetal.gradle.managers.KotlinDependenciesManager
import org.gradle.api.Project
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency
import org.gradle.kotlin.dsl.support.delegates.ProjectDelegate
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet


@Deprecated("Use add instead for consistency")
fun Project.fetch(action: GradleDependencyManager.() -> Unit) =
    dependencyResolver(GradleDependencyManager(this), action)

fun Project.add(action: GradleDependencyManager.() -> Unit) =
    dependencyResolver(GradleDependencyManager(this), action)

fun Project.compileOnly(dependencyNotation: Any): Dependency? =
    with(dependencies) { add("compileOnly", dependencyNotation) }

fun Project.implementation(dependencyNotation: Any): Dependency? =
    with(dependencies) { add("implementation", dependencyNotation) }

fun Project.testImplementation(dependencyNotation: Any): Dependency? =
    with(dependencies) { add("testImplementation", dependencyNotation) }

fun KotlinDependencyHandler.add(project: Project, action: KotlinDependenciesManager.() -> Unit) =
    dependencyResolver(KotlinDependenciesManager(this, project), action)

private fun <Handler, T, M : DependenciesManager<Handler, T, M>> dependencyResolver(
    dependencyManager: M,
    action: M.() -> Unit
) = action(dependencyManager)


operator fun ConfigurationContainer.get(name: String) = getByName(name)


fun KotlinSourceSet.addGeneratedKotlin(
    kotlinGenerated: String = "build/generated/source/kaptKotlin/main",
    kaptGenerated: String = "build/generated/source/kapt/main"
) = srcDir(
    kotlinGenerated,
    kaptGenerated
)

fun KotlinSourceSet.addGeneratedCodeSource(buildPath:String){
    srcDir()
}

fun KotlinSourceSet.addGeneratedKotlinTest(
    kaptKotlinGeneratedTest: String = "build/generated/source/kaptKotlin/test",
    kaptGeneratedTest: String = "build/generated/source/kapt/test"
) =
    srcDir(
        kaptKotlinGeneratedTest,
        kaptGeneratedTest
    )

fun KotlinSourceSet.srcDir(vararg path: String): SourceDirectorySet = kotlin.srcDir(path)

fun Project.kapt(dependencyAnnotation: String): Dependency {
    val dep = dependencyAnnotation.split(':')

    val dependency = DefaultExternalModuleDependency(
        // group
        dep[0],
        // name
        dep[1],
        // version
        try {
            dep[2]
        } catch (e: Exception) {
            "+"
        }
    )

    configurations.getByName("kapt").dependencies.add(dependency)

    return dependency

}


fun ProjectDelegate.kotlinAdd(action: KotlinDependenciesManager.() -> Unit) =
    dependencyResolver(KotlinDependenciesManager(dependencies as KotlinDependencyHandler, project), action)



