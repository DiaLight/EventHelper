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
    id("com.github.johnrengelman.shadow") version "2.0.1"
    kotlin("jvm") version "1.3.21"
    java
    base
}

val pluginGroup: String by project
val pluginVersion: String by project

val self = this
allprojects {
    if(!self.ext.has("buildProps")) {
        val buildPropsFile = self.file(File(buildDir, "build.properties"))
        val buildProps = Properties()
        if(buildPropsFile.exists()) buildProps.load(buildPropsFile.inputStream())
        val buildVersion = buildProps.getProperty("build-version", "0").toInt() + 1
        val libDir = buildProps.getProperty("libDir", "$buildDir/libs")
        val mcVersion = "1.12.2"
        val allVersion = "~build-$buildVersion"

        self.ext.set("buildPropsFile", buildPropsFile)
        self.ext.set("buildProps", buildProps)
        self.ext.set("buildVersion", buildVersion)
        self.ext.set("allVersion", allVersion)
        self.ext.set("mcVersion", mcVersion)
        self.ext.set("libDir", libDir)
    }
}

group = pluginGroup as String

task("incrementVersion") { this as DefaultTask
    doFirst {
        val buildPropsFile: File by ext
        val buildProps: Properties by ext
        val buildVersion: Int by ext
        buildProps["build-version"] = buildVersion.toString()
        buildProps.store(buildPropsFile.writer(), null)
    }
}


repositories {
    mavenCentral()
    maven("https://jitpack.io/") { name = "jitpack-repo" }
    maven("https://hub.spigotmc.org/nexus/content/groups/public/") { name = "spigotmc-repo" }
//    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") { name = "spigotmc-snapshots-repo" }
    maven("https://repo.spacehq.org/content/repositories/releases/") { name = "spacehq-repo" }
//    maven("https://repo.spacehq.org/content/repositories/snapshots/") { name = "spacehq-snapshots-repo" }
    maven("https://oss.sonatype.org/content/groups/public/") { name = "sonatype-repo" }
//    maven("https://oss.sonatype.org/content/repositories/snapshots") { name = "sonatype-snapshots-repo" }
//    maven("https://maven2.ontando.ru/repository/") { name = "angal-repo" }
    maven("http://repo.dmulloy2.net/content/groups/public/") { name = "dmulloy2-repo" }
    maven("http://maven.fabricmc.net/") { name = "fabricmc-repo" }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

configurations {
    val shadow = this.getByName("shadow")
    this.getByName("compile") {
        extendsFrom(shadow)
    }
}

dependencies {
    val shadow by configurations
}
