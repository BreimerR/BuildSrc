package libetal.gradle.implementations

import libetal.gradle.managers.DependenciesManager
import libetal.gradle.enums.Sources
import libetal.gradle.enums.Types

abstract class Type<Handler,ManagerType, Manager : DependenciesManager<Handler, ManagerType, Manager>, T, GenericImplementationType : Type<Handler, ManagerType, Manager, T, GenericImplementationType>>(
    private val manager: DependenciesManager<Handler, ManagerType, Manager>
) {
    @Suppress("UNCHECKED_CAST")
    val context by lazy {
        this as GenericImplementationType
    }

    fun restoreState() = manager.restoreState()

    fun updateState(action: Manager.() -> Unit) = manager.updateState(action)

    internal fun implement(
        implementationType: Types,
        implementationSource: Sources,
        action: T.() -> Unit
    ) {
        with(manager) {
            implement(implementationType, implementationSource) {
                @Suppress("UNCHECKED_CAST")
                action(this@Type as T)
            }
        }
    }

    internal fun implement(implementationType: Types, action: T.() -> Unit) =
        implement(implementationType, manager.implementationSource, action)

    fun implement(action: T.() -> Unit) =
        implement(Types.GRADLE, manager.implementationSource, action)

    fun kaptImplement(action: T.() -> Unit) = implement(Types.KAPT, action)

    fun compileImplement(action: T.() -> Unit) = implement(Types.COMPILE, action)

    fun compileImplement(
        implementationSource: Sources = manager.implementationSource,
        action: T.() -> Unit
    ) = implement(Types.COMPILE, implementationSource, action)

    fun testImplement(action: T.() -> Unit) = implement(Types.GRADLE_TEST, action)

    fun resolve(toImplement:Any){
        with(manager) {
            with(handler) {
                apply(getSource(toImplement))
            }
        }
    }

}