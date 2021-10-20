buildscript {


    repositories{
        mavenCentral()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
        mavenLocal()
    }


    val kotlinVersion: String by project

    dependencies {
        classpath(kotlin("gradle-plugin", kotlinVersion))

    }
}

allprojects{
    repositories {
        mavenCentral()
        mavenLocal()

        maven {
            url  = uri("https://plugins.gradle.org/m2/")
        }
    }
}