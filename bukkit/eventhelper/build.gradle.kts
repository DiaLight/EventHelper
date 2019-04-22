import org.apache.commons.io.FilenameUtils

plugins {
    java
    base
}

val pluginGroup: String by project
val pluginVersion: String by project

val buildVersion: Int by rootProject.ext
val allVersion: String by rootProject.ext
val mcVersion: String by rootProject.ext

val libDir: String by project(":bukkit").ext
var configureProject: Project.(List<String>, List<String>) -> Unit by project(":bukkit").ext

val parts = arrayOf(
    ":bukkit:toollib",
    ":bukkit:modulelib",
    ":bukkit:guilib",
    ":bukkit:offlinelib",
    ":bukkit:ehgui",
    ":bukkit:teleporter",
    ":bukkit:freezer"
//    ":bukkit:teams",
//    ":bukkit:random",
//    ":bukkit:autorespawn",
//    ":bukkit:oldpvp",  // not ready yet
//    ":bukkit:captain"
)

val deps = listOf()
val join = listOf(
    ":bukkit:misc",
    *parts
)

configureProject(join, deps)

task("copyToServer_splitted", Copy::class) {
    doFirst {
        val libDirFile = File(libDir)
        libDirFile.deleteRecursively()
        libDirFile.mkdirs()
    }
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
