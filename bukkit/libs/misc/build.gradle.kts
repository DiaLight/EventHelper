
plugins {
    java
    base
    idea
}

val pluginGroup: String by project
val pluginVersion: String by project

val allVersion: String by rootProject.ext
val mcVersion: String by rootProject.ext

var configureProject: Project.(List<String>, List<String>) -> Unit by project(":bukkit").ext

val deps = listOf<String>()
val join = listOf<String>()

configureProject(join, deps)

val bcs = mapOf(
    "bukkit_8" to mapOf(
        "api" to "org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT",
        "provided" to "org.bukkit:bukkit:1.8.8-R0.1-SNAPSHOT",
        "jar" to files("/home/dialight/room/server/spigot/1.8.9/spigot-1.8.8.jar")
    ),
    "bukkit_9" to mapOf(
        "api" to "org.spigotmc:spigot-api:1.9.4-R0.1-SNAPSHOT",
        "provided" to "org.bukkit:bukkit:1.9.4-R0.1-SNAPSHOT",
        "jar" to files("/home/dialight/room/server/spigot/1.9.4/spigot-1.9.4.jar")
    ),
    "bukkit_10" to mapOf(
        "api" to "org.spigotmc:spigot-api:1.10.2-R0.1-SNAPSHOT",
        "provided" to "org.bukkit:bukkit:1.10.2-R0.1-SNAPSHOT",
        "jar" to files("/home/dialight/room/server/spigot/1.10.2/spigot-1.10.2.jar")
    ),
    "bukkit_11" to mapOf(
        "api" to "org.spigotmc:spigot-api:1.11.2-R0.1-SNAPSHOT",
        "provided" to "org.bukkit:bukkit:1.11.2-R0.1-SNAPSHOT",
        "jar" to files("/home/dialight/room/server/spigot/1.11.2/spigot-1.11.2.jar")
    ),
    "bukkit_12" to mapOf(
        "api" to "com.destroystokyo.paper:paper-api:1.12.2-R0.1-SNAPSHOT",
        "provided" to "org.bukkit:bukkit:1.12.2-R0.1-SNAPSHOT",
        "jar" to files("/home/dialight/room/server/paper/1.12.2/cache/patched_1.12.2.jar")
    ),
    "bukkit_13" to mapOf(
        "api" to "com.destroystokyo.paper:paper-api:1.13.2-R0.1-SNAPSHOT",
        "provided" to "org.bukkit:bukkit:1.13.2-R0.1-SNAPSHOT",
        "jar" to files("/home/dialight/room/server/paper/1.13.2/cache/patched_1.13.2.jar")
    ),
    "bukkit_14" to mapOf(
        "api" to "com.destroystokyo.paper:paper-api:1.14.4-R0.1-SNAPSHOT",
        "provided" to "org.bukkit:bukkit:1.14.4-R0.1-SNAPSHOT",
        "jar" to files("/home/dialight/room/server/paper/1.14.4/cache/patched_1.14.4.jar")
    )
)

sourceSets {
    bcs.forEach {
        sourceSets.create(it.key)
    }
}

configurations {
    bcs.forEach {
//        this["${it.key}Compile"].extendsFrom(this["compile"])
        this["${it.key}CompileOnly"].extendsFrom(this["compileOnly"])
//        this["${it.key}AnnotationProcessor"].extendsFrom(this["annotationProcessor"])
//        this["${it.key}Implementation"].extendsFrom(this["implementation"])
        val provided = this.create("${it.key}Provided")
    }
    val provided = this.create("provided")
    this["compileOnly"].extendsFrom(provided)
}
val provided = configurations["provided"]

dependencies {
    bcs.forEach {
        val provided = configurations["${it.key}Provided"]
        val implementation = configurations["${it.key}Implementation"]
        val compile = configurations["${it.key}Compile"]
        val compileOnly = configurations["${it.key}CompileOnly"]
        compileOnly(it.value["provided"]!!)
        compileOnly(it.value["jar"]!!)
        implementation(it.value["api"]!!)
        implementation("org.jetbrains:annotations:13.0")
        compile(sourceSets["main"].output)
    }
    compileOnly("io.netty:netty-all:4.1.9.Final")
    provided("org.ow2.asm:asm:7.1")
    provided("org.ow2.asm:asm-tree:7.1")
}

//bcs.forEach {
//    val files = configurations["${it.key}Provided"].files()
//    sourceSets[it.key].compileClasspath.files.addAll(files)
//}

tasks["joinJar"].apply { this as Jar
    bcs.forEach {
        from(sourceSets[it.key].output)
    }
    provided.files.map { from(zipTree(it)) }
}
tasks["fatJar"].apply { this as Jar
    bcs.forEach {
        from(sourceSets[it.key].output)
    }
    provided.files.map { from(zipTree(it)) }
}
//idea {
//    module{
//        println(scopes.keys)
//        println(scopes)
//        scopes["PROVIDED"]?.get("plus")?.add(configurations["bukkit_8Provided"])
//    }
//}
