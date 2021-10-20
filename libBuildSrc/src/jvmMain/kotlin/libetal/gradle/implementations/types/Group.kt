package libetal.gradle.implementations.types

import libetal.gradle.managers.DependenciesManager
import libetal.gradle.enums.Sources
import libetal.gradle.enums.Types
import libetal.gradle.implementations.Type
import libetal.gradle.managers.GradleDependencyManager


class Group<Handler, T, Manager : DependenciesManager<Handler, T, Manager>>(
    private val manager: Manager,
    private val packageNames: MutableList<String>,
    val version: String
) : Type<Handler, T, Manager, Group<Handler, T, Manager>, Group<Handler, T, Manager>>(manager) {

    operator fun String.invoke(
        version: String = this@Group.version,
        action: Group<Handler, T, Manager>.() -> Unit
    ) = action(
        Group(
            manager,
            (packageNames + this).toMutableList(),
            version
        )
    )

    operator fun String.invoke(
        separator: Char,
        version: String = context.version,
        postFix: String = "",
        library: Library<Handler, T, Manager>? = null,
        action: Library<Handler, T, Manager> .() -> Unit
    ) = action(
        library ?: Library(
            manager,
            packageNames,
            mutableListOf(this),
            separator,
            version,
            postFix
        )
    )


    operator fun String.invoke(version: String = context.version, separator: Char = '-', postFix: String = "") {
        Library(
            manager,
            packageNames,
            mutableListOf(),
            separator,
            version,
            postFix
        ).apply {
            +this@invoke
        }
    }

    operator fun String.unaryPlus() = this(version)

    operator fun String.invoke(
        implement: Boolean,
        separator: Char = '-',
        version: String = context.version,
        postFix: String = "",
        action: Library<Handler, T, Manager>.() -> Unit
    ) {

        if (implement) this(version)

        this(separator, version, postFix, null, action)

    }

    private fun implement(
        artifactId: String,
        version: String = this.version,
        implementationType: Types = manager.implementationType,
        implementationSource: Sources = manager.implementationSource
    ) {
        implement(implementationType, implementationSource) {
            artifactId(version)
        }
    }

    fun implement(
        artifactId: String,
        version: String = this.version
    ) = implement(
        artifactId,
        version,
        manager.implementationType,
        manager.implementationSource
    )

    fun GradleDependencyManager.testImplement(artifactId: String, version: String = context.version) =
        implement(artifactId, version, Types.GRADLE_TEST, manager.implementationSource)

    fun kaptImplement(artifactId: String, version: String = this.version) =
        implement(artifactId, version, Types.KAPT)

    fun compileImplement(artifactId: String, version: String = this.version) =
        implement(artifactId, version, Types.COMPILE)


    fun kaptImplement(
        implementationSource: Sources = manager.implementationSource,
        action: Group<Handler, T, Manager>.() -> Unit
    ) = implement(Types.KAPT, implementationSource, action)

}



