import com.google.code.regexp.Pattern
import net.minecraftforge.gradle.user.TaskSingleReobf
import org.apache.commons.io.FilenameUtils
import org.apache.tools.ant.filters.ReplaceTokens
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.spongepowered.asm.gradle.plugins.MixinExtension
import org.spongepowered.asm.gradle.plugins.MixinGradlePlugin
import org.spongepowered.gradle.meta.MetadataBaseExtension
import java.util.Properties

buildscript {
    repositories {
        maven("http://files.minecraftforge.net/maven") { name = "forge" }
        maven("https://repo.spongepowered.org/maven") { name = "sponge" }
    }

    dependencies {
        classpath("net.minecraftforge.gradle:ForgeGradle:2.3-SNAPSHOT")

        classpath("org.spongepowered:mixingradle:0.6-SNAPSHOT")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.21")
    }
}

plugins {
    id("org.spongepowered.plugin")
    id("net.minecrell.vanillagradle.server") version "2.2-6"
    kotlin("jvm")
    java
    base
}

apply {
    this.plugin<MixinGradlePlugin>()
}
val mixin = the<MixinExtension>()

val pluginGroup: String by project
val pluginVersion: String by project

val allVersion: String by rootProject.ext
val mcVersion: String by rootProject.ext

version = "$mcVersion"

var sponge_conf: MetadataBaseExtension.() -> Unit by ext
sponge_conf = {
    this.plugins.apply {
        this.create("guilib") {
            this.meta.apply {
                this.setName("GuiLib")
                setVersion(allVersion)
                this.authors.add("DiaLight")
                this.setDescription("Useful tool to help event masters with theirs job")
                this.dependencies.apply {
                    this.create("kotlinrt")
                }
            }
        }
    }
}
sponge.sponge_conf()

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

configurations {
    val shadow = create("shadow")
    this["compile"].extendsFrom(shadow)
}

mixin.apply {
    this.add(sourceSets["main"], "mixins.guilib.refmap.json")
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

val join = listOf(":misc")

dependencies {
    val shadow by configurations
    implementation("org.spongepowered:spongeapi:7.1.0-SNAPSHOT")
    implementation("org.spongepowered:spongevanilla:1.12.2-7.1.5")
    implementation("org.spongepowered:mixin:0.7.5-SNAPSHOT")
    implementation(kotlin("stdlib-jdk8"))
    add("annotationProcessor", "net.fabricmc:sponge-mixin:0.7.11.16")
    add("annotationProcessor", "net.fabricmc:fabric-loom:0.3.0-SNAPSHOT")
    join.forEach { implementation(project(it)) }
}

task("joinJar", Jar::class) {
    dependsOn(tasks["reobfJar"])
    from(tasks["jar"].outputs.files.map { zipTree(it) })  // we need reobfuscated content
    exclude("mcmod.info")
    baseName = "${project.name}-join"
}
task("fatJar", Jar::class) {
    manifest {
        attributes(mapOf(
            "TweakClass" to "org.spongepowered.asm.launch.MixinTweaker",
            "Main-Class" to "org.spongepowered.asm.launch.MixinTweaker",
            "MixinConfigs" to "mixins.guilib.json",
            "FMLCorePluginContainsFMLMod" to "true"
        ))
    }
    dependsOn(tasks["reobfJar"])
    from(tasks["jar"].outputs.files.map { zipTree(it) })
    doFirst {
        join.forEach {
            from(project(it).tasks["joinJar"].outputs.files.map { zipTree(it) })
        }
    }
    baseName = "${project.name}-fat"
}

minecraft {
    version = mcVersion
    mappings = "snapshot_20180814"
    makeObfSourceJar = false
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