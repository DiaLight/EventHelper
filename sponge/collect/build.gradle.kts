import org.apache.commons.io.FilenameUtils
import org.apache.tools.ant.filters.ReplaceTokens
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.spongepowered.gradle.meta.MetadataBaseExtension
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
    id("org.spongepowered.plugin")
    kotlin("jvm")
    java
    base
}

val pluginGroup: String by project
val pluginVersion: String by project

val buildVersion: Int by rootProject.ext
val allVersion: String by rootProject.ext
val mcVersion: String by rootProject.ext

val libDir: String by project(":sponge").ext

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

val ehProjects = listOf(
    ":sponge:kotlinrt",
    ":sponge:toollib",
    ":sponge:modulelib",
    ":sponge:guilib",
    ":sponge:offlinelib",
    ":sponge:eventhelper",
    ":sponge:teleporter",
    ":sponge:freezer",
    ":sponge:teams",
    ":sponge:random",
    ":sponge:autorespawn",
//    ":sponge:oldpvp",  // not ready yet
    ":sponge:captain"
)

val join = listOf(":sponge:misc")

dependencies {
    val shadow by configurations
}

sponge {
    plugins.apply {
        this.clear()
    }
}

fun <T> ExtraPropertiesExtension.getOrNull(key: String): T? {
    return if(has(key)) get(key) as T? else null
}
val self = this
for(proj in gradle.rootProject.allprojects) {
    val conf = proj.ext.getOrNull<MetadataBaseExtension.() -> Unit>("sponge_conf")
    if(conf != null) {
//        println("**** apply conf from ${this.name}")
        self.sponge.conf()
    }
}
gradle.afterProject {
    if (state.failure == null) {
        val conf = this.ext.getOrNull<MetadataBaseExtension.() -> Unit>("sponge_conf")
        if(conf != null) {
//            println("**** apply conf from ${this.name}")
            self.sponge.conf()
        } else {
//            println("**** skip conf from ${this.name}")
        }
    }
}
tasks["jar"].apply { this as Jar
    baseName = "dummy"
}

task("fatJar", Jar::class) {
    manifest {
        attributes(mapOf(
            "TweakClass" to "org.spongepowered.asm.launch.MixinTweaker",
            "Main-Class" to "org.spongepowered.asm.launch.MixinTweaker",
            "MixinConfigs" to "mixins.guilib.json,mixins.teams.json",
            "FMLCorePluginContainsFMLMod" to "true"
        ))
    }
    ehProjects.forEach {
        dependsOn("$it:build")  // we need execution if reobfJar task if it exists
        dependsOn("$it:joinJar")
    }
    doFirst {
        ehProjects.forEach {
            from(project(it).tasks["joinJar"].outputs.files.map { zipTree(it) })
        }
    }
    join.forEach {
        dependsOn("$it:joinJar")
    }
    doFirst {
        join.forEach {
            from(project(it).tasks["joinJar"].outputs.files.map { zipTree(it) })
        }
    }
    baseName = "all"
    from(
        tasks["jar"].outputs.files.map { zipTree(it) }
    )
    outputs.upToDateWhen { false }  // update every time
}

task("collectReleaseJars", Copy::class) {
    val binDir = file("bin")
    doFirst {
        binDir.deleteRecursively()
    }

    didWork = false  // update every time

    ehProjects.forEach {
        dependsOn("$it:fatJar")
        doFirst {
            val jar_task = project(it).tasks["fatJar"] as Jar
            from(jar_task.outputs.files)
        }
    }
    val fatJar = tasks["fatJar"] as Jar
    dependsOn(fatJar)
    from(fatJar.outputs.files.singleFile)
    into(binDir)
    this.rename {
        val dir = FilenameUtils.getFullPathNoEndSeparator(it)
        val name = FilenameUtils.getBaseName(it).split("-")[0]
        return@rename "$dir/EventHelper-$name-sponge-$mcVersion-build-$buildVersion.jar"
    }
    outputs.upToDateWhen { false }  // update every time
}

val allTestMode = false

task("copyToServer", Copy::class) {
    doFirst {
        val libDirFile = File(libDir)
        libDirFile.deleteRecursively()
        libDirFile.mkdirs()
    }
    dependsOn(tasks["fatJar"])
    if(allTestMode) {
        val jar = tasks["fatJar"] as Jar
        from(jar)
        this.rename {
            val dir = FilenameUtils.getFullPathNoEndSeparator(it)
            val name = FilenameUtils.getBaseName(it).split("-")[0]
            return@rename "$dir/eventhelper-$name.jar"
        }
    } else {
        ehProjects.forEach {
            dependsOn("$it:fatJar")
            doFirst {
                val jar_task = project(it).tasks["fatJar"] as Jar
                from(jar_task.outputs.files)
            }
        }
        val jar = tasks["jar"] as Jar
        from(jar)
        this.rename {
            val dir = FilenameUtils.getFullPathNoEndSeparator(it)
            val name = FilenameUtils.getBaseName(it).split("-")[0]
            return@rename "$dir/$name.jar"
        }
    }
    into(libDir)
    outputs.upToDateWhen { false }  // update every time
}

tasks["processResources"].apply { this as Copy
    from(sourceSets["main"].resources.srcDirs) {
        filter<ReplaceTokens>(
            "tokens" to mapOf(
                "version" to version
            )
        )
    }
}

tasks["build"].apply {
    dependsOn(tasks["collectReleaseJars"])
    dependsOn(tasks["copyToServer"])
    dependsOn(":incrementVersion")
}
