rootProject.name = "EventHelper"

fun Settings.includeAll(base: String, path: String) = file(path).listFiles().asList().stream()
    .filter { it.isDirectory }
    .filter { File(it, "build.gradle.kts").exists() }
    .forEach { dir ->
    include(":$base:${dir.name}")
    project(":$base:${dir.name}").projectDir = dir
}
fun Settings.includeProjects(base: String, path: String) = file(path).listFiles().asList().stream()
    .filter { it.isDirectory }
    .filter { File(it, "build.gradle.kts").exists() }
    .forEach { dir ->
    include(":$base:${dir.name}")
    project(":$base:${dir.name}").projectDir = dir
}

includeAll("sponge", "sponge/libs")
includeProjects("sponge", "sponge")
includeAll("sponge", "sponge/teams_controllers")
includeAll("sponge", "sponge/modules")

includeAll("bukkit", "bukkit/libs")
includeProjects("bukkit", "bukkit")
includeAll("bukkit", "bukkit/modules")
includeAll("bukkit", "bukkit/teams")


file("local.settings.gradle.kts").let { if(it.exists()) apply(from = it) }
