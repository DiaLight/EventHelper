import org.apache.tools.ant.filters.ReplaceTokens
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.spongepowered.asm.gradle.plugins.MixinExtension
import org.spongepowered.asm.gradle.plugins.MixinGradlePlugin

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
    id("org.spongepowered.plugin") version "0.8.1"
    id("net.minecrell.vanillagradle.server") version "2.2-6"
    id("com.github.johnrengelman.shadow") version "2.0.1"
    kotlin("jvm") version "1.3.21"
    java
    base
}

apply {
    this.plugin<MixinGradlePlugin>()
}
val mixin = the<MixinExtension>()


//val pluginGroup: String by project
//val pluginVersion: String by project
//val libDir: String by project
val pluginGroup: Any? by project
val pluginVersion: Any? by project
val libDir: Any? by project

fun getSourceSet(name: String): SourceSet {
//    return sourceSets[name]
    return java.sourceSets[name]
}

group = pluginGroup as String
version = pluginVersion as String

sponge {
    this.plugins.apply {
        this.create("toollib") {
            this.meta.apply {
                this.setName("ToolLib")
                setVersion(project.version)
                this.authors.add("DiaLight")
                this.setDescription("Useful tool to help event masters with theirs job")
            }
        }
        this.create("guilib") {
            this.meta.apply {
                this.setName("GuiLib")
                setVersion(project.version)
                this.authors.add("DiaLight")
                this.setDescription("Useful tool to help event masters with theirs job")
            }
        }
        this.create("eventhelper") {
            this.meta.apply {
                this.setName("EventHelper")
                setVersion(project.version)
                this.authors.add("DiaLight")
                this.setDescription("Useful tool to help event masters with theirs job")
                this.dependencies.apply {
                    this.create("toollib")
                    this.create("guilib")
                }
            }
        }
        this.create("teleporter") {
            this.meta.apply {
                this.setName("Teleporter")
                setVersion(project.version)
                this.authors.add("DiaLight")
                this.setDescription("Useful tool to help event masters with theirs job")
                this.dependencies.apply {
                    this.create("toollib")
                    this.create("guilib") {
                        this.optional = true
                    }
                    this.create("eventhelper") {
                        this.optional = true
                    }
                }
            }
        }
        this.create("freezer") {
            this.meta.apply {
                this.setName("Freezer")
                setVersion(project.version)
                this.authors.add("DiaLight")
                this.setDescription("Useful tool to help event masters with theirs job")
                this.dependencies.apply {
                    this.create("toollib")
                    this.create("guilib") {
                        this.optional = true
                    }
                    this.create("eventhelper") {
                        this.optional = true
                    }
                }
            }
        }
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
}

base {
    libsDirName = libDir as String
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    // mixin works only with java
    sourceSets.getByName("main").java.srcDirs(
        "src/main/guilib"
    )
}

kotlin {
    sourceSets.getByName("main").kotlin.srcDirs(
        "src/main/eventhelper",
        "src/main/guilib",
        "src/main/toollib",
        "src/main/misc",
        "src/main/teleporter",
        "src/main/freezer"
    )
}

mixin.apply {
    this.add(getSourceSet("main"), "mixins.gulib.refmap.json")
}

configurations {
    val shadow = this.getByName("shadow")
    this.getByName("compile") {
        extendsFrom(shadow)
    }
}

dependencies {
    val shadow by configurations
    implementation("org.spongepowered:spongeapi:7.1.0-SNAPSHOT")
    implementation("org.spongepowered:spongevanilla:1.12.2-7.1.5")
    shadow("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.spongepowered:mixin:0.7.5-SNAPSHOT")
}

minecraft {
    version = "1.12.2"
    mappings = "snapshot_20180131"
    makeObfSourceJar = false
}


tasks.withType<Jar> {
    manifest {
        attributes(mapOf(
            "TweakClass" to "org.spongepowered.asm.launch.MixinTweaker",
            "Main-Class" to "org.spongepowered.asm.launch.MixinTweaker",
            "MixinConfigs" to "mixins.guilib.json",
            "FMLCorePluginContainsFMLMod" to "true"
            ))
    }
//    archiveName = "${application.applicationName}-$version.jar"
    from(
        getSourceSet("main").output,
        configurations.shadow.map { if (it.isDirectory) it else zipTree(it) }
    )
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks["processResources"].apply { this as Copy
    from(getSourceSet("main").resources.srcDirs) {
        filter<ReplaceTokens>(
            "tokens" to mapOf(
                "version" to version
            )
        )
    }
}
