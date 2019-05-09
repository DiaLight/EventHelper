import org.apache.commons.io.FilenameUtils
import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    java
    base
    idea
}

val pluginGroup: String by project
val pluginVersion: String by project

val buildVersion: Int by rootProject.ext
val allVersion: String by rootProject.ext
val mcVersion: String by rootProject.ext

val libDir: String by project(":bukkit").ext
var configureProject: Project.(List<String>, List<String>) -> Unit by project(":bukkit").ext

val parts = arrayOf(
    ":bukkit:eventhelper",
    ":bukkit:toollib",
    ":bukkit:modulelib",
    ":bukkit:guilib",
    ":bukkit:offlinelib",
    ":bukkit:maingui",
    ":bukkit:teleporter",
    ":bukkit:freezer",
//    ":bukkit:teams",
//    ":bukkit:random",
    ":bukkit:autorespawn"
//    ":bukkit:oldpvp",  // not ready yet
//    ":bukkit:captain"
)

val deps = listOf()
val join = listOf(
    ":bukkit:misc"
)

configureProject(join, deps)

sourceSets {
    sourceSets.create("allInOne")
}

configurations {
    this["allInOneCompile"].extendsFrom(this["compile"])
    this["allInOneCompileOnly"].extendsFrom(this["compileOnly"])
    this["allInOneAnnotationProcessor"].extendsFrom(this["annotationProcessor"])
    this["allInOneImplementation"].extendsFrom(this["implementation"])
}

dependencies {
    val allInOneCompileOnly by configurations
    val allInOneAnnotationProcessor by configurations
    val allInOneImplementation by configurations
//    allInOneCompileOnly("org.projectlombok:lombok:1.18.6")
//    allInOneAnnotationProcessor("org.projectlombok:lombok:1.18.6")
//    allInOneImplementation("com.destroystokyo.paper:paper-api:1.13.2-R0.1-SNAPSHOT")
//    allInOneImplementation("org.jetbrains:annotations:13.0")
    parts.forEach { allInOneImplementation(project(it)) }
}

tasks["fatJar"].apply { this as Jar
    val tokens = mapOf(
        "version" to allVersion,
        "main_class" to "dialight.eventhelper.EventHelper"
    )
    from(sourceSets["main"].resources.srcDirs) {
        filter<ReplaceTokens>("tokens" to tokens)
    }
}
task("fatJar_allInOne", Jar::class) {
    from(sourceSets["allInOne"].output)
    join.forEach {
        dependsOn("$it:build")  // we need execution if reobfJar task if it exists
        dependsOn("$it:joinJar")
    }
    parts.forEach {
        dependsOn("$it:build")  // we need execution if reobfJar task if it exists
        dependsOn("$it:joinJar")
    }
    doFirst {
        join.forEach {
            from(project(it).tasks["joinJar"].outputs.files.map { zipTree(it) })
        }
        parts.forEach {
            from(project(it).tasks["joinJar"].outputs.files.map { zipTree(it) })
        }
    }
    baseName = "${project.name}-fat-allInOne"
    val tokens = mapOf(
        "version" to allVersion,
        "main_class" to "dialight.eventhelper.EventHelperBuiltin"
    )
    from(sourceSets["allInOne"].resources.srcDirs) {
        filter<ReplaceTokens>("tokens" to tokens)
    }
    outputs.upToDateWhen { false }  // update every time
}
task("copyToServer_allInOne", Copy::class) {
    doFirst {
        val libDirFile = File(libDir)
        libDirFile.deleteRecursively()
        libDirFile.mkdirs()
    }
    dependsOn(tasks["fatJar_allInOne"])
    val jar = tasks["fatJar_allInOne"] as Jar
    from(jar)
    this.rename {
        val dir = FilenameUtils.getFullPathNoEndSeparator(it)
        val name = FilenameUtils.getBaseName(it).split("-")[0]
        return@rename "$dir/$name.jar"
    }
    into(libDir)
    outputs.upToDateWhen { false }  // update every time
}
task("copyToServer_allInOne_update", Copy::class) {
    dependsOn(tasks["fatJar_allInOne"])
    val jar = tasks["fatJar_allInOne"] as Jar
    from(jar)
    this.rename {
        val dir = FilenameUtils.getFullPathNoEndSeparator(it)
        val name = FilenameUtils.getBaseName(it).split("-")[0]
        return@rename "$dir/$name.jar"
    }
    into(libDir)
    outputs.upToDateWhen { false }  // update every time
}
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
