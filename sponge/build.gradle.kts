import org.gradle.api.internal.artifacts.configurations.Configurations
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.*

buildscript {
    repositories {
        maven("http://files.minecraftforge.net/maven") { name = "forge" }
        maven("https://repo.spongepowered.org/maven") { name = "sponge" }
    }

    dependencies {
        classpath("net.minecraftforge.gradle:ForgeGradle:2.3-SNAPSHOT")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.21")
    }
}

plugins {
    id("org.spongepowered.plugin") version "0.8.1"
    kotlin("jvm")
    java
    base
}

var buildProps: Properties by rootProject.ext

var libDir: String by ext
var spongeDep: DependencyHandler.() -> Unit by ext

libDir = buildProps.getProperty("spongeLibDir", "$buildDir/libs")

subprojects {
    repositories {
        maven("https://repo.spongepowered.org/maven") { name = "sponge" }
        maven("http://maven.fabricmc.net/") { name = "fabricmc-repo" }
        maven("https://jitpack.io/") { name = "jitpack-repo" }
        mavenCentral()
        maven("https://oss.sonatype.org/content/groups/public/") { name = "sonatype-repo" }
    }
}

spongeDep = {
    implementation("org.spongepowered:spongeapi:7.1.0-SNAPSHOT")
    implementation("org.spongepowered:spongevanilla:1.12.2-7.1.5")
}

