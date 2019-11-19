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
var configureProject: Project.(List<String>, List<String>) -> Unit by project(":sponge").ext


val parts = arrayOf(
    ":sponge:kotlinrt",
    ":sponge:toollib",
    ":sponge:modulelib",
    ":sponge:guilib",
    ":sponge:offlinelib",
    ":sponge:maingui",
    ":sponge:teleporter",
    ":sponge:freezer",
    ":sponge:teams",
    ":sponge:random",
    ":sponge:autorespawn",
//    ":sponge:oldpvp",  // not ready yet
    ":sponge:captain"
)

val deps = listOf()
val join = listOf(
    ":sponge:misc",
    *parts
)

configureProject(join, deps)


var sponge_conf: MetadataBaseExtension.() -> Unit by ext
sponge_conf = {
    this.plugins.apply {
        this.create("eventhelper") {
            this.meta.apply {
                this.setName("EventHelper")
                setVersion(allVersion)
                this.authors.add("DiaLight")
                this.setDescription("Useful tool to help event masters with theirs job")
            }
        }
    }
}
sponge.sponge_conf()


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
        if(conf != null && this.name != self.name) {
//            println("**** apply conf from ${this.name}")
            self.sponge.conf()
        } else {
//            println("**** skip conf from ${this.name}")
        }
    }
}

task("fatJar-AllInOne", Jar::class) {
    manifest {
        attributes(mapOf(
            "TweakClass" to "org.spongepowered.asm.launch.MixinTweaker",
            "Main-Class" to "org.spongepowered.asm.launch.MixinTweaker",
            "MixinConfigs" to "mixins.guilib.json,mixins.teams.json",
            "FMLCorePluginContainsFMLMod" to "true"
        ))
    }
    join.forEach {
        dependsOn("$it:build")  // we need execution if reobfJar task if it exists
        dependsOn("$it:joinJar")
    }
    doFirst {
        join.forEach {
            from(project(it).tasks["joinJar"].outputs.files.map { zipTree(it) })
        }
    }
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

    parts.forEach {
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

task("copyToServer_splitted", Copy::class) {
    doFirst {
        val libDirFile = File(libDir)
        libDirFile.deleteRecursively()
        libDirFile.mkdirs()
    }
    dependsOn(tasks["fatJar"])
    parts.forEach {
        dependsOn("$it:fatJar")
        doFirst {
            val jar_task = project(it).tasks["fatJar"] as Jar
            from(jar_task.outputs.files)
        }
    }
    val jar = tasks["jar"] as Jar
    from(jar)
    doFirst {
        exclude(jar.outputs.files.singleFile.name)
    }
    this.rename {
        val dir = FilenameUtils.getFullPathNoEndSeparator(it)
        val name = FilenameUtils.getBaseName(it).split("-")[0]
        return@rename "$dir/$name.jar"
    }
    into(libDir)
    outputs.upToDateWhen { false }  // update every time
}
