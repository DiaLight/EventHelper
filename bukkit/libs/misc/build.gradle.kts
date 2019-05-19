
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

val deps = listOf()
val join = listOf()

configureProject(join, deps)

val bcs = mapOf(
    "bukkit_8" to mapOf(
        "api" to "org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT",
        "provided" to "org.bukkit:craftbukkit:1.8.8-R0.1-SNAPSHOT"
    ),
    "bukkit_9" to mapOf(
        "api" to "org.spigotmc:spigot-api:1.9.4-R0.1-SNAPSHOT",
        "provided" to "org.bukkit:craftbukkit:1.9.4-R0.1-SNAPSHOT"
    ),
    "bukkit_10" to mapOf(
        "api" to "org.spigotmc:spigot-api:1.10.2-R0.1-SNAPSHOT",
        "provided" to "org.bukkit:craftbukkit:1.10.2-R0.1-SNAPSHOT"
    ),
    "bukkit_11" to mapOf(
        "api" to "org.spigotmc:spigot-api:1.11.2-R0.1-SNAPSHOT",
        "provided" to "org.bukkit:craftbukkit:1.11.2-R0.1-SNAPSHOT"
    ),
    "bukkit_12" to mapOf(
        "api" to "com.destroystokyo.paper:paper-api:1.12.2-R0.1-SNAPSHOT",
        "provided" to "org.bukkit:craftbukkit:1.12.2-R0.1-SNAPSHOT"
    ),
    "bukkit_13" to mapOf(
        "api" to "com.destroystokyo.paper:paper-api:1.13.2-R0.1-SNAPSHOT",
        "provided" to "org.bukkit:craftbukkit:1.13.2-R0.1-SNAPSHOT"
    ),
    "bukkit_14" to mapOf(
        "api" to "org.spigotmc:spigot-api:1.14.1-R0.1-SNAPSHOT",
        "provided" to "org.bukkit:craftbukkit:1.14.1-R0.1-SNAPSHOT"
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
//        this["${it.key}CompileOnly"].extendsFrom(this["compileOnly"])
//        this["${it.key}AnnotationProcessor"].extendsFrom(this["annotationProcessor"])
//        this["${it.key}Implementation"].extendsFrom(this["implementation"])
        val provided = this.create("${it.key}Provided")
    }
}

dependencies {
    bcs.forEach {
        val provided = configurations["${it.key}Provided"]
        val implementation = configurations["${it.key}Implementation"]
        val compile = configurations["${it.key}Compile"]
        val compileOnly = configurations["${it.key}CompileOnly"]
        compileOnly(it.value["provided"]!!)
        implementation(it.value["api"]!!)
        implementation("org.jetbrains:annotations:13.0")
        compile(sourceSets["main"].output)
    }
    compileOnly("io.netty:netty-all:4.1.9.Final")
}

bcs.forEach {
    sourceSets[it.key].compileClasspath.files.addAll(configurations["${it.key}Provided"].files())
}

tasks["joinJar"].apply { this as Jar
    bcs.forEach {
        from(sourceSets[it.key].output)
    }
}
tasks["fatJar"].apply { this as Jar
    bcs.forEach {
        from(sourceSets[it.key].output)
    }
}
//idea {
//    module{
//        println(scopes.keys)
//        println(scopes)
//        scopes["PROVIDED"]?.get("plus")?.add(configurations["bukkit_8Provided"])
//    }
//}
