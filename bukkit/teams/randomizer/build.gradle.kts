
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

val deps = listOf(
    ":bukkit:eventhelper",
    ":bukkit:guilib",
    ":bukkit:maingui",
    ":bukkit:teams",
    ":bukkit:misc"
)
val join = listOf(
)

configureProject(join, deps)
