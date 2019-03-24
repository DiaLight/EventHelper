rootProject.name = "EventHelper"

fun Settings.includeAll(path: String) = file(path).listFiles().asList().stream()
    .filter { it.isDirectory }
    .forEach { dir ->
    include(dir.name)
    project(":${dir.name}").projectDir = dir
}
fun Settings.includeProjects(path: String) = file(path).listFiles().asList().stream()
    .filter { it.isDirectory }
    .filter { File(it, "build.gradle.kts").exists() }
    .forEach { dir ->
    include(dir.name)
    project(":${dir.name}").projectDir = dir
}

includeAll("libs")
includeProjects(".")
includeAll("teams_controllers")

file("local.settings.gradle.kts").let { if(it.exists()) apply(from = it) }
