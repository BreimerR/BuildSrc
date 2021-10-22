package libetal.gradle.implementations.types

import libetal.gradle.implementations.Type
import libetal.gradle.implementations.VersionedType
import libetal.gradle.managers.DependenciesManager

class Kapt<Handler, T, Manager : DependenciesManager<Handler, T, Manager>>(version: String, manager: Manager) :
    VersionedType<Handler, T, Manager, Kapt<Handler, T, Manager>, Kapt<Handler, T, Manager>>(version, manager)