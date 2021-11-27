val jvmTarget: String by project
val projectGroup: String by project
val projectVersion: String by project


plugins {
    kotlin("multiplatform")
    `maven-publish`
}

group = projectGroup
version = projectVersion

kotlin {
    jvm()

    sourceSets {
        val jvmMain by getting {
            dependencies {

                compileOnly("dev.gradleplugins:gradle-api:7.1.1")
                implementation(kotlin("gradle-plugin-api"))
                compileOnly(files("/home/brymher/.gradle/wrapper/dists/gradle-7.1.1-bin/f29rtwfnc96ub43tt7p47zsru/gradle-7.1.1/lib/gradle-kotlin-dsl-7.1.1.jar"))
                compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:+")


            }
        }
    }
}