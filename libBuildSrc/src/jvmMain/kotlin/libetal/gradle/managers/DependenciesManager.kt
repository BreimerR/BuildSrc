package libetal.gradle.managers

import libetal.gradle.enums.Types
import libetal.gradle.enums.Sources
import org.gradle.api.artifacts.Dependency
import libetal.gradle.implementations.Type
import libetal.gradle.implementations.types.Jar
import libetal.gradle.implementations.types.Group
import libetal.gradle.implementations.types.Library
import libetal.gradle.implementations.types.Kotlin as KotlinImplementationType
import libetal.gradle.implementations.types.Project as ProjectImplementationType

abstract class DependenciesManager<Handler, ManagerType, Manager : DependenciesManager<Handler, ManagerType, Manager>>(
    val handler: Handler
) {

    @Suppress("UNCHECKED_CAST")
    val manager by lazy {
        this as Manager
    }

    var implementationType: Types = Types.GRADLE
    var implementationSource: Sources = Sources.REMOTE

    private var state: State? = null

    fun updateState(action: Manager.() -> Unit) {
        @Suppress("UNCHECKED_CAST")
        with(this as Manager) {
            internalUpdateState(action)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun Manager.internalUpdateState(action: Manager.() -> Unit) {
        saveState()
        action(this)
    }

    fun saveState() {
        state = State(
            implementationSource,
            implementationType
        )
    }

    internal data class State(
        val implementationSource: Sources? = null,
        val implementationType: Types? = null
    )

    fun restoreState() {

        state?.let {
            it.implementationType?.let {
                implementationType = it
            }
            it.implementationSource?.let {
                implementationSource = it
            }
        }

        state = null
    }

    internal fun implement(
        implementationType: Types = this.implementationType,
        implementationSource: Sources = this.implementationSource,
        action: Manager.() -> Unit
    ) {
        updateState {
            this.implementationType = implementationType
            this.implementationSource = implementationSource
        }
        @Suppress("UNCHECKED_CAST")
        action(this as Manager)
        restoreState()
    }

    fun implement(action: Manager.() -> Unit) = implement(this.implementationType, this.implementationSource, action)

    fun testImplement(
        implementationSource: Sources = this.implementationSource,
        action: Manager.() -> Unit
    ) = implement(Types.GRADLE_TEST, implementationSource, action)

    fun compileImplement(
        implementationSource: Sources = this.implementationSource,
        action: Manager.() -> Unit
    ) = implement(Types.COMPILE, implementationSource, action)

    fun kaptImplement(
        implementationSource: Sources = this.implementationSource,
        action: Manager.() -> Unit
    ) = implement(Types.KAPT, implementationSource, action)

    private val internalGroup = mutableListOf<String>()
    var separator: Char = '-'

    private val identifierPostFix = ""

    operator fun String.invoke(
        libraryVersion: String = "",
        action: Group<Handler, ManagerType, Manager>.() -> Unit
    ) {
        action(
            Group(
                manager,
                mutableListOf(this),
                libraryVersion
            )
        )
    }

    operator fun String.invoke(version: String, separator: Char = '-') {
        Library(
            manager,
            internalGroup,
            mutableListOf(),
            separator,
            version,
            identifierPostFix
        ).apply {
            +this@invoke
        }
    }

    fun project(
        action: ProjectImplementationType<Handler, ManagerType, Manager>.() -> Unit
    ) {
        runInSafeState(ProjectImplementationType(manager), action) {
            implementationSource = Sources.PROJECT
        }
    }


    fun kotlinImplement(action: KotlinImplementationType<Handler, ManagerType, Manager>.() -> Unit) {
        runInSafeState(KotlinImplementationType(manager), action) {
            implementationSource = Sources.KOTLIN
        }
    }

    fun jar(
        path: String = "",
        version: String = "",
        commonLibsPath: String = "",
        versionSeparator: Char? = null,
        directorySeparator: Char = '/',
        action: Jar<Handler, ManagerType, Manager>.() -> Unit
    ) {
        runInSafeState(
            Jar(
                path,
                manager,
                commonLibsPath,
                version,
                versionSeparator,
                directorySeparator
            ), action
        ) {
            implementationSource = Sources.JAR
        }

    }


    fun <T : Type<Handler, ManagerType, Manager, *, *>> runInSafeState(
        context: T,
        action: T.() -> Unit,
        stateSaver: Manager.() -> Unit
    ) {
        stateSaver(manager)
        action(context)
        restoreState()
    }

    abstract fun Handler.getSource(dependency: Any): Any

    abstract fun Handler.apply(dependency: Any): Dependency?

}


