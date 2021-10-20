package libetal.gradle

import libetal.gradle.managers.KotlinDependenciesManager
import libetal.gradle.managers.DependenciesManager
import libetal.gradle.managers.GradleDependencyManager
import org.gradle.api.Project
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.artifacts.Dependency
import org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.jetbrains.kotlin.gradle.plugin.mpp.DefaultKotlinDependencyHandler


@Deprecated("Use add instead for consistency")
@Suppress("unused")
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

@Suppress("unused")
fun KotlinDependencyHandler.add(project: Project, action: KotlinDependenciesManager.() -> Unit) =
    dependencyResolver(KotlinDependenciesManager(this, project), action)

private fun <Handler, T, M : DependenciesManager<Handler, T, M>> dependencyResolver(
    dependencyManager: M,
    action: M.() -> Unit
) = action(dependencyManager)


operator fun ConfigurationContainer.get(name: String) = getByName(name)


fun Project.kapt(fullQualifiedGroupDependency: String, version: String = "+"): Dependency {
    val dep = fullQualifiedGroupDependency.split(':')

    val resolvedVersion = try {
        dep[2]
    } catch (e: Exception) {
        ""
    }

    val dependency = DefaultExternalModuleDependency(
        // group
        dep[0],
        // name
        dep[1],
        // version
        if (resolvedVersion == "") version else resolvedVersion
    )

    configurations.getByName("kapt").dependencies.add(dependency)

    return dependency

}