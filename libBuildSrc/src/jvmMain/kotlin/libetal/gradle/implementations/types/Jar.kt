package libetal.gradle.implementations.types

import libetal.gradle.managers.DependenciesManager
import libetal.gradle.implementations.Type
import libetal.gradle.enums.Types

class Jar<Handler, T, Manager : DependenciesManager<Handler, T, Manager>>(
    path: String,
    private val manager: Manager,
    commonLibsPath: String = "",
    val version: String = "",
    val versionSeparator: Char? = null,
    private val directorySeparator: Char = '/',
    var libJarPath: String = "build/libs"
) : Type<Handler, T, Manager, Jar<Handler, T, Manager>, Jar<Handler, T, Manager>>(manager) {

    var path: String = ""
        set(value) {
            field = value.trim(directorySeparator)
        }

    var commonLibsPath: String = ""
        set(value) {
            field = value.trim(directorySeparator)
        }

    init {
        this.commonLibsPath = commonLibsPath
        this.path = path
    }

    operator fun String.invoke(
        commonLibsPath: String = "",
        version: String = context.version,
        libJarPath: String = context.libJarPath,
        sep: Char? = context.versionSeparator,
        action: Jar<Handler, T, Manager>.() -> Unit
    ) {

        val newPath = if (this.isEmpty()) "" else "$directorySeparator$this"

        var commons = if (context.commonLibsPath.isEmpty()) "" else
            "$directorySeparator${context.commonLibsPath}"

        if (commonLibsPath.isNotEmpty())
            commons += "$directorySeparator$commonLibsPath"

        action(
            Jar(
                "$path$newPath",
                manager,
                commons,
                version,
                sep,
                directorySeparator,
                libJarPath
            )
        )
    }

    operator fun String.invoke(
        jarFileName: String = this,
        libraryName: String = "",
        version: String = this@Jar.version,
        sep: Char? = this@Jar.versionSeparator
    ) {
        val commonLibsPath = if (commonLibsPath.isEmpty()) "" else
            "$directorySeparator$commonLibsPath"

        val libJarPath = if (libJarPath.isEmpty()) ""
        else "$directorySeparator$libJarPath"

        val libName = if (libraryName.isEmpty()) "" else "$directorySeparator$libraryName"

        val library = "$path$libName$commonLibsPath$libJarPath$directorySeparator$jarFileName${sep ?: ""}"

        resolve("$library$version.jar")
    }

    operator fun String.invoke(
        rootIsSelf: Boolean = true,
        jarFileName: String = "",
        version: String = this@Jar.version,
        sep: Char? = this@Jar.versionSeparator
    ) = this(jarFileName.ifEmpty { this }, if (rootIsSelf) this else "", version, sep)

    operator fun String.unaryPlus() = this("lib-jvm", this, version, this@Jar.versionSeparator)

    fun implement(
        path: String = "",
        version: String = this.version,
        commonLibsPath: String = "",
        libJarPath: String = "",
        sep: Char? = this.versionSeparator,
        implementationType: Types = manager.implementationType,
        action: Jar<Handler, T, Manager> .() -> Unit
    ) {

        val addPath = if (path.isEmpty()) "" else "$directorySeparator$path"

        val common = if (commonLibsPath.isEmpty()) "" else "$directorySeparator$commonLibsPath"

        val currentImplementationType = manager.implementationType

        manager.implementationType = implementationType

        action(
            Jar(
                this.path,
                manager,
                "${this.commonLibsPath}$addPath$common".trim(directorySeparator),
                version,
                sep,
                directorySeparator,
                libJarPath.ifEmpty { this.libJarPath }
            )
        )

        manager.implementationType = currentImplementationType

    }

    fun compileImplement(
        path: String = "",
        version: String = this.version,
        commonLibsPath: String = "",
        libJarPath: String = "",
        sep: Char? = this.versionSeparator,
        action: Jar<Handler, T, Manager>.() -> Unit
    ) {
        implement(
            path,
            version,
            commonLibsPath,
            libJarPath.ifEmpty { this.libJarPath },
            sep,
            Types.COMPILE,
            action
        )
    }

    fun testImplement(
        path: String = "",
        version: String = this.version,
        commonLibsPath: String = "",
        libJarPath: String = "",
        sep: Char? = this.versionSeparator,
        action: Jar<Handler, T, Manager>.() -> Unit
    ) {
        implement(
            path,
            version,
            commonLibsPath,
            libJarPath.ifEmpty { this.libJarPath },
            sep,
            Types.GRADLE_TEST,
            action
        )
    }

    fun kaptImplement(
        path: String = "",
        version: String = this.version,
        commonLibsPath: String = "",
        libJarPath: String = "",
        sep: Char? = this.versionSeparator,
        action: Jar<Handler, T, Manager>.() -> Unit
    ) {
        implement(
            path,
            version,
            commonLibsPath,
            libJarPath.ifEmpty { this.libJarPath },
            sep,
            Types.KAPT,
            action
        )
    }
}