package libetal.gradle.implementations.types

import libetal.gradle.managers.DependenciesManager
import libetal.gradle.implementations.Type

class Kotlin<Handler, T, Manager : DependenciesManager<Handler, T, Manager>>(manager: Manager) :
    Type<Handler, T, Manager, Project<Handler, T, Manager>, Project<Handler, T, Manager>>(manager)