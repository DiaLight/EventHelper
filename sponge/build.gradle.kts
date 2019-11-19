import org.gradle.api.internal.artifacts.configurations.Configurations
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.spongepowered.asm.gradle.plugins.MixinExtension
import org.spongepowered.asm.gradle.plugins.MixinGradlePlugin
import net.minecrell.vanillagradle.VanillaServerPlugin
import net.minecraftforge.gradle.user.UserBaseExtension
import org.apache.commons.io.FilenameUtils
import org.spongepowered.gradle.SpongeGradlePlugin
import org.spongepowered.gradle.meta.MetadataPlugin
import java.util.*

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
    kotlin("jvm")
    java
    base
}

var buildProps: Properties by rootProject.ext
val allVersion: String by rootProject.ext
val mcVersion: String by rootProject.ext

var libDir: String by ext
var spongeDep: DependencyHandler.() -> Unit by ext

var configureProject: Project.(List<String>, List<String>) -> Unit by ext
var configureMixinProject: Project.(List<String>, List<String>, String) -> Unit by ext

libDir = buildProps.getProperty("spongeLibDir", "$buildDir/libs")

this.version = mcVersion
minecraft {
    version = mcVersion
//        mappings = "snapshot_20180814"
    mappings = "stable_45"
    makeObfSourceJar = false
}

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

configureProject = { join, deps ->

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    configurations {
        val fat = this.create("fat")
        this["compile"].extendsFrom(fat)
    }

    dependencies {
        val fat by configurations
        spongeDep()
        implementation(kotlin("stdlib-jdk8"))
        deps.forEach { implementation(project(it)) }
        join.forEach { implementation(project(it)) }
    }

    task("joinJar", Jar::class) {
        from(sourceSets["main"].output)
        exclude("mcmod.info")
        baseName = "${project.name}-join"
    }
    task("fatJar", Jar::class) {
        from(sourceSets["main"].output)
        join.forEach {
            dependsOn("$it:build")  // we need execution if reobfJar task if it exists
            dependsOn("$it:joinJar")
        }
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

    task("copyToServer", Copy::class) {
        dependsOn(tasks["fatJar"])
        val jar = tasks["fatJar"] as Jar
        from(jar)
        this.rename {
            val dir = FilenameUtils.getFullPathNoEndSeparator(it)
            val name = FilenameUtils.getBaseName(it).split("-")[0]
            return@rename "$dir/$name.jar"
        }
        into(libDir)
        outputs.upToDateWhen { false }  // update every time
    }
    
}


configureMixinProject = { join, deps, mixinConf ->

    apply {
        this.plugin<SpongeGradlePlugin>()
        this.plugin<MetadataPlugin>()
        this.plugin<VanillaServerPlugin>()
        this.plugin<MixinGradlePlugin>()
    }
    val mixin = the<MixinExtension>()
    val vanilla = the<UserBaseExtension>()
    val sponge = the<UserBaseExtension>()

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    configurations {
        val fat = this.create("fat")
        this["compile"].extendsFrom(fat)
    }

    this.version = mcVersion

    dependencies {
        val fat by configurations
        spongeDep()
        implementation(kotlin("stdlib-jdk8"))
        deps.forEach { implementation(project(it)) }
        join.forEach { implementation(project(it)) }
        implementation("org.spongepowered:mixin:0.7.5-SNAPSHOT")
        add("annotationProcessor", "net.fabricmc:sponge-mixin:0.7.11.16")
        add("annotationProcessor", "net.fabricmc:fabric-loom:0.3.0-SNAPSHOT")
    }

    this.version = mcVersion

    minecraft {
        version = mcVersion
        mappings = "snapshot_20180814"  // latest 1.12.2 snap
//        mappings = "stable_39"  // latest 1.12 stable
        makeObfSourceJar = false
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
                "MixinConfigs" to mixinConf,
                "FMLCorePluginContainsFMLMod" to "true"
            ))
        }
        dependsOn(tasks["reobfJar"])
        from(tasks["jar"].outputs.files.map { zipTree(it) })
        join.forEach {
            dependsOn("$it:build")  // we need execution if reobfJar task if it exists
            dependsOn("$it:joinJar")
        }
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
}
