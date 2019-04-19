import org.apache.commons.io.FilenameUtils

plugins {
    kotlin("jvm")
    java
    base
}

val pluginGroup: String by project
val pluginVersion: String by project

val buildVersion: Int by rootProject.ext
val allVersion: String by rootProject.ext
val mcVersion: String by rootProject.ext

val libDir: String by project(":bukkit").ext


val ehProjects = listOf(
    ":bukkit:toollib"
//    ":bukkit:modulelib",
//    ":bukkit:guilib",
//    ":bukkit:offlinelib",
//    ":bukkit:eventhelper",
//    ":bukkit:teleporter",
//    ":bukkit:freezer",
//    ":bukkit:teams",
//    ":bukkit:random",
//    ":bukkit:autorespawn",
//    ":bukkit:oldpvp",  // not ready yet
//    ":bukkit:captain"
)

val allTestMode = false

task("copyToServer", Copy::class) {
    doFirst {
        val libDirFile = File(libDir)
        libDirFile.deleteRecursively()
        libDirFile.mkdirs()
    }
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
