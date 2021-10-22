package libetal.gradle.implementations

import libetal.gradle.enums.Sources
import libetal.gradle.enums.Types
import libetal.gradle.implementations.types.Group
import libetal.gradle.implementations.types.Kapt
import libetal.gradle.managers.DependenciesManager

abstract class VersionedType<Handler, ManagerType, Manager : DependenciesManager<Handler, ManagerType, Manager>, T, VT : VersionedType<Handler, ManagerType, Manager, T, VT>>(
    val version: String,
    manager: Manager
) : Type<Handler, ManagerType, Manager, T, VT>(manager) {

    fun kaptImplement(action: Kapt<Handler, ManagerType, Manager>.() -> Unit) {
        val implementationType = manager.implementationType
        manager.implementationType = Types.KAPT
        action(Kapt(version, manager))
        manager.implementationType = implementationType
    }
}