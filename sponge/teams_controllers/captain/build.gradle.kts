import org.apache.commons.io.FilenameUtils
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.spongepowered.gradle.meta.MetadataBaseExtension

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
    id("org.spongepowered.plugin")
    kotlin("jvm")
    java
    base
}

val pluginGroup: String by project
val pluginVersion: String by project

val allVersion: String by rootProject.ext
val mcVersion: String by rootProject.ext

val libDir: String by project(":sponge").ext
var spongeDep: DependencyHandler.() -> Unit by project(":sponge").ext
var configureProject: Project.(List<String>, List<String>) -> Unit by project(":sponge").ext

val deps = listOf(
    ":sponge:modulelib",
    ":sponge:toollib",
    ":sponge:guilib",
    ":sponge:teleporter",
    ":sponge:freezer",
    ":sponge:teams",
    ":sponge:offlinelib",
    ":sponge:ehgui"
)
val join = listOf(
    ":sponge:misc"
)

configureProject(join, deps)


var sponge_conf: MetadataBaseExtension.() -> Unit by ext
sponge_conf = {
    this.plugins.apply {
        this.create("captain") {
            this.meta.apply {
                this.setName("Captain")
                setVersion(allVersion)
                this.authors.add("DiaLight")
                this.setDescription("Useful tool to help event masters with theirs job")
                this.dependencies.apply {
                    this.create("kotlinrt")
                    this.create("modulelib")
                    this.create("toollib")
                    this.create("teleporter")
                    this.create("freezer")
                    this.create("teams")
                    this.create("offlinelib")
                    this.create("guilib") {
                        this.optional = true
                    }
                    this.create("ehgui") {
                        this.optional = true
                    }
                }
            }
        }
    }
}
sponge.sponge_conf()