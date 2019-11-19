
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
    ":bukkit:offlinelib",
    ":bukkit:misc",
    ":bukkit:teams"
)
val join = listOf(
)

configureProject(join, deps)
