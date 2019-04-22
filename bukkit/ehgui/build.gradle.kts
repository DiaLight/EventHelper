
plugins {
    java
    base
}

val pluginGroup: String by project
val pluginVersion: String by project

val allVersion: String by rootProject.ext
val mcVersion: String by rootProject.ext

var configureProject: Project.(List<String>, List<String>) -> Unit by project(":bukkit").ext

val deps = listOf(
    ":bukkit:toollib"
)
val join = listOf(
    ":bukkit:misc"
)

configureProject(join, deps)
