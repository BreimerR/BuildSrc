package libetal.gradle.implementations

import libetal.gradle.managers.DependenciesManager
import libetal.gradle.enums.Sources
import libetal.gradle.enums.Types
import libetal.gradle.implementations.types.Kapt

abstract class Type<Handler, ManagerType, Manager : DependenciesManager<Handler, ManagerType, Manager>, T, GenericImplementationType : Type<Handler, ManagerType, Manager, T, GenericImplementationType>>(
    protected val manager: Manager
) {
    @Suppress("UNCHECKED_CAST")
    val context by lazy {
        this as T
    }

    fun restoreState() = manager.restoreState()

    fun updateState(action: Manager.() -> Unit) = manager.updateState(action)

    internal fun implement(
        implementationType: Types,
        implementationSource: Sources,
        action: GenericImplementationType.() -> Unit
    ) {
        with(manager) {
            implement(implementationType, implementationSource) {
                @Suppress("UNCHECKED_CAST")
                action(this@Type as GenericImplementationType)
            }
        }
    }

    internal fun implement(implementationType: Types, action: GenericImplementationType.() -> Unit) =
        implement(implementationType, manager.implementationSource, action)

    fun implement(action: GenericImplementationType.() -> Unit) =
        implement(Types.GRADLE, manager.implementationSource, action)



    fun compileImplement(action: GenericImplementationType.() -> Unit) = implement(Types.COMPILE, action)

    fun compileImplement(
        implementationSource: Sources = manager.implementationSource,
        action: GenericImplementationType.() -> Unit
    ) = implement(Types.COMPILE, implementationSource, action)

    fun testImplement(action: GenericImplementationType.() -> Unit) = implement(Types.GRADLE_TEST, action)

    fun resolve(toImplement: Any) {
        with(manager) {
            with(handler) {
                apply(getSource(toImplement))
            }
        }
    }

}