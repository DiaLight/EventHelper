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
val libDirs: List<String> by project(":bukkit").ext
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
    ":bukkit:teams",
    ":bukkit:autorespawn",
    ":bukkit:randomizer",
    ":bukkit:captain",
    ":bukkit:priority"
)

val deps = listOf<String>()
val join = listOf(
    ":bukkit:misc"
)

configureProject(join, deps)

val allInOne by sourceSets.creating
val main by sourceSets.getting

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


val jar by tasks.getting(Jar::class)
val fatJar by tasks.getting(Jar::class) {
    val tokens = mapOf(
        "version" to allVersion,
        "main_class" to "dialight.eventhelper.EventHelper"
    )
    from(main.resources.srcDirs) {
        filter<ReplaceTokens>("tokens" to tokens)
    }
}
val fatJar_allInOne by tasks.creating(Jar::class) {
    from(allInOne.output)
    val deps = join + parts
    deps.forEach {
        val proj = project(it)
        val joinJar by proj.tasks.getting(Jar::class)
        dependsOn(joinJar)
        inputs.files(joinJar)
    }
    doFirst {
        deps.forEach {
            val proj = project(it)
            val joinJar by proj.tasks.getting(Jar::class)
            from(joinJar.outputs.files.map { zipTree(it) })
        }
    }
    baseName = "${project.name}-fat-allInOne"
    val tokens = mapOf(
        "version" to allVersion,
        "main_class" to "dialight.eventhelper.EventHelperBuiltin"
    )
    from(allInOne.resources.srcDirs) {
        filter<ReplaceTokens>("tokens" to tokens)
    }
    outputs.upToDateWhen { false }  // update every time
}
val copyToServer_allInOne by tasks.creating(Copy::class) {
//    doFirst {
//        val libDirFile = File(libDir)
//        libDirFile.deleteRecursively()
//        libDirFile.mkdirs()
//    }
    dependsOn(fatJar_allInOne)
    from(fatJar_allInOne)
    this.rename {
        val name = FilenameUtils.getBaseName(it).split("-")[0]
        return@rename "$name.jar"
    }
    into(libDir)
//    outputs.upToDateWhen { false }  // update every time
}
val copyToServer_allInOne_update by tasks.creating {
    dependsOn(fatJar_allInOne)
    doFirst {
        libDirs.forEach {
            copy {
                this.rename {
                    val name = FilenameUtils.getBaseName(it).split("-")[0]
                    return@rename "$name.jar"
                }
                from(fatJar_allInOne)
                into(it)
            }
        }
        copy {
            this.rename {
                return@rename "EventHelper-bukkit-1.8.X-build-$buildVersion.jar"
            }
            val binDir = File("$buildDir/bin")
            binDir.deleteRecursively()
            from(fatJar_allInOne)
            into(binDir)
        }
    }
}
val copyToServer_splitted by tasks.creating(Copy::class) {
    doFirst {
        val libDirFile = File(libDir)
        libDirFile.deleteRecursively()
        libDirFile.mkdirs()
    }
    parts.forEach {
        dependsOn("$it:fatJar")
        doFirst {
            val proj = project(it)
            val fatJar by proj.tasks.getting(Jar::class)
            from(fatJar.outputs.files)
        }
    }
    from(jar)
    doFirst {
        exclude(jar.outputs.files.singleFile.name)
    }
    this.rename {
        val name = FilenameUtils.getBaseName(it).split("-")[0]
        return@rename "$name.jar"
    }
    into(libDir)
    outputs.upToDateWhen { false }  // update every time
}
