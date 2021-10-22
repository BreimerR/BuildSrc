import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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

                implementation("dev.gradleplugins:gradle-api:7.1")

                implementation("org.gradle.kotlin:gradle-kotlin-dsl-plugins:2.1.7")

                implementation("gradle.plugin.org.gradle.kotlin:gradle-kotlin-dsl-plugins:1.1.3")

                implementation("org.gradle.kotlin:gradle-kotlin-dsl-plugins:2.1.4")

                implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:+")
                implementation("org.gradle.kotlin:gradle-kotlin-dsl-conventions:0.7.0")
                implementation("org.gradle.kotlin.kotlin-dsl:org.gradle.kotlin.kotlin-dsl.gradle.plugin:2.1.7")
                implementation("org.gradle.kotlin.kotlin-dsl.base:org.gradle.kotlin.kotlin-dsl.base.gradle.plugin:2.1.7")
                implementation("org.gradle.kotlin.kotlin-dsl.compiler-settings:org.gradle.kotlin.kotlin-dsl.compiler-settings.gradle.plugin:2.1.7")
                implementation("org.gradle.kotlin.kotlin-dsl.precompiled-script-plugins:org.gradle.kotlin.kotlin-dsl.precompiled-script-plugins.gradle.plugin:2.1.7")

            }
        }
    }
}


/*tasks.register<Jar>("fatJar") {
    group = "build"
    description = "Assembles Kotlin sources"

    archiveClassifier.set("sources")

    from(sourceSets.main.all().kotlin)
    dependsOn(tasks["main"])
}*/


/*
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = jvmTarget
}
*/


