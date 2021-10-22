package libetal.gradle.implementations.types

import libetal.gradle.managers.DependenciesManager
import libetal.gradle.implementations.Type
import libetal.gradle.enums.Sources
import libetal.gradle.enums.Types
import libetal.gradle.implementations.VersionedType

class Library<Handler, T, Manager : DependenciesManager<Handler, T, Manager>>(
    manager: Manager,
    private val group: MutableList<String>,
    private var identifiers: MutableList<String> = mutableListOf(),
    var separator: Char = '-',
    version: String,
    var postFix: String = ""
) : VersionedType<Handler, T, Manager, Library<Handler, T, Manager>, Library<Handler, T, Manager>>(version, manager) {

    operator fun String.invoke(version: String = context.version) {
        val prefix = if (identifiers.isEmpty()) ""
        else identifiers.joinToString("$separator") + "$separator"

        var postFix = this@Library.postFix

        postFix = if (postFix.isEmpty()) "" else "$separator$postFix"

        val group = group.joinToString(".")

        val library = "$group:$prefix$this$postFix"

        val version = if (version == "") throw Exception(
            """|
               | Version not provided for $library
               | Provide on by either 
               |     artifactId(version)
               | OR 
               |     libraryGroup(version){
               |         ...add artifactID's here
               |     }
            """.trimMargin()
        )
        else version

        resolve("$library:$version")
    }

    operator fun String.invoke(
        postFix: String = this@Library.postFix,
        version: String = this@Library.version,
        action: Library<Handler, T, Manager>.() -> Unit
    ) = action(
        Library(
            manager,
            group,
            (identifiers + this).toMutableList(),
            separator,
            version,
            postFix
        )
    )


    operator fun String.unaryPlus() = this(version)

    operator fun String.invoke(implement: Boolean, action: Library<Handler, T, Manager>.() -> Unit) {
        if (implement) this(version)

        this(this@Library.postFix, this@Library.version, action)

    }


    fun implement(
        implementationType: Types = manager.implementationType,
        artifactId: String,
        version: String = this.version
    ) {
        implement(implementationType) {
            artifactId(version)
        }
    }

    fun testImplement(artifactId: String, version: String = this.version) =
        implement(Types.GRADLE_TEST, artifactId, version)

    fun kaptImplement(
        implementationSource: Sources = manager.implementationSource,
        action: Library<Handler, T, Manager>.() -> Unit
    ) =
        implement(Types.KAPT, implementationSource, action)

    fun compileImplement(artifactId: String, version: String = this.version) =
        implement(Types.COMPILE, artifactId, version)


}
