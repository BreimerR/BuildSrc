package libetal.gradle.implementations.types

import libetal.gradle.managers.DependenciesManager
import libetal.gradle.implementations.Type


class Project<Handler, T, Manager : DependenciesManager<Handler, T, Manager>>(manager: Manager) :
    Type<Handler, T, Manager, Project<Handler, T, Manager>, Project<Handler, T, Manager>>(manager) {
    operator fun String.unaryPlus() {
        resolve(":$this")
    }
}



