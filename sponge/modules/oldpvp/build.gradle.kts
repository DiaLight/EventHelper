import com.google.code.regexp.Pattern
import org.apache.commons.io.FilenameUtils
import org.apache.tools.ant.filters.ReplaceTokens
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.spongepowered.gradle.meta.MetadataBaseExtension
import java.util.Properties

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

var spongeDep: DependencyHandler.() -> Unit by project(":sponge").ext
var configureProject: Project.(List<String>, List<String>) -> Unit by project(":sponge").ext

val deps = listOf(
    ":sponge:modulelib",
    ":sponge:guilib",
    ":sponge:maingui"
)
val join = listOf(
    ":sponge:misc"
)

configureProject(join, deps)


var sponge_conf: MetadataBaseExtension.() -> Unit by ext
sponge_conf = {
    this.plugins.apply {
        this.create("oldpvp") {
            this.meta.apply {
                this.setName("OldPvP")
                setVersion(allVersion)
                this.authors.add("DiaLight")
                this.setDescription("Useful tool to help event masters with theirs job")
                this.dependencies.apply {
                    this.create("kotlinrt")
                    this.create("modulelib")
                    this.create("guilib") {
                        this.optional = true
                    }
                    this.create("maingui") {
                        this.optional = true
                    }
                    this.create("teleporter") {
                        this.optional = true
                    }
                }
            }
        }
    }
}
sponge.sponge_conf()
