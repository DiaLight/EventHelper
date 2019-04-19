import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.apache.tools.ant.filters.*

buildscript {
    repositories {
        maven("http://files.minecraftforge.net/maven") { name = "forge" }
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.21")
    }
}

plugins {
    kotlin("jvm")
    java
    base
}

val pluginGroup: String by project
val pluginVersion: String by project

val allVersion: String by rootProject.ext
val mcVersion: String by rootProject.ext

var bukkitDep: DependencyHandler.() -> Unit by project(":bukkit").ext

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

configurations {
    val shadow = this.create("shadow")
    this["compile"].extendsFrom(shadow)
}

val join = listOf()

dependencies {
    val shadow by configurations
    bukkitDep()
    implementation(kotlin("stdlib-jdk8"))
    join.forEach { implementation(project(it)) }
}

tasks["processResources"].apply { this as ProcessResources
    val tokens = mapOf("version" to "2.3.1")
    from(sourceSets["main"].resources.srcDirs) {
        filter<ReplaceTokens>("tokens" to tokens)
    }
}

task("joinJar", Jar::class) {
    from(sourceSets["main"].output)
    exclude("mcmod.info")
    baseName = "${project.name}-join"
}
task("fatJar", Jar::class) {
    from(sourceSets["main"].output)
    doFirst {
        join.forEach {
            from(project(it).tasks["joinJar"].outputs.files.map { zipTree(it) })
        }
    }
    baseName = "${project.name}-fat"
}

tasks["build"].apply {
    dependsOn(":incrementVersion")
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}