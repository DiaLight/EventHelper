import org.apache.commons.io.FilenameUtils
import org.apache.tools.ant.filters.ReplaceTokens
import java.util.*

buildscript {
    repositories {
        maven("http://files.minecraftforge.net/maven") { name = "forge" }
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.21")
    }
}

plugins {
    java
    base
    idea
}

subprojects {
    repositories {
        mavenCentral()
        maven("https://jitpack.io/") { name = "jitpack-repo" }
        maven("https://repo.destroystokyo.com/repository/maven-public/") { name = "destroystokyo-repo" }
        maven("https://hub.spigotmc.org/nexus/content/groups/public/") { name = "spigotmc-repo" }
//        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") { name = "spigotmc-snap-repo" }
        maven("https://oss.sonatype.org/content/groups/public/") { name = "sonatype-repo" }
//        maven("https://oss.sonatype.org/content/repositories/snapshots") { name = "sonatype-snap-repo" }
        mavenLocal()
    }

}

var buildProps: Properties by rootProject.ext
val allVersion: String by rootProject.ext
val mcVersion: String by rootProject.ext

var libDir: String by ext
var libDirs: List<String> by ext
var configureProject: Project.(List<String>, List<String>) -> Unit by ext

configureProject = { join, deps ->
    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    configurations {
        val shadow = this.create("shadow")
        this["compile"].extendsFrom(shadow)
    }

    dependencies {
        val shadow by configurations
//        compileOnly("org.projectlombok:lombok:1.18.6")
//        annotationProcessor("org.projectlombok:lombok:1.18.6")
//        implementation("com.destroystokyo.paper:paper-api:1.13.2-R0.1-SNAPSHOT")
        implementation("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
        implementation("org.jetbrains:annotations:13.0")
        deps.forEach { implementation(project(it)) }
        join.forEach { implementation(project(it)) }
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    tasks["processResources"].apply { this as ProcessResources
        val tokens = mapOf("version" to allVersion)
        from(sourceSets["main"].resources.srcDirs) {
            filter<ReplaceTokens>("tokens" to tokens)
        }
    }

    task("joinJar", Jar::class) {
        from(sourceSets["main"].output)
        exclude("plugin.yml")
        baseName = "${project.name}-join"
    }
    task("fatJar", Jar::class) {
        from(sourceSets["main"].output)
        join.forEach {
            dependsOn("$it:build")  // we need execution if reobfJar task if it exists
            dependsOn("$it:joinJar")
        }
        doFirst {
            join.forEach {
                from(project(it).tasks["joinJar"].outputs.files.map { zipTree(it) })
            }
        }
        baseName = "${project.name}-fat"
    }

    tasks["build"].apply {
        dependsOn(":incrementVersion")
    }

    task("copyToServer", Copy::class) {
        dependsOn(tasks["fatJar"])
        val jar = tasks["fatJar"] as Jar
        from(jar)
        this.rename {
            val dir = FilenameUtils.getFullPathNoEndSeparator(it)
            val name = FilenameUtils.getBaseName(it).split("-")[0]
            return@rename "$dir/$name.jar"
        }
        into(libDir)
        outputs.upToDateWhen { false }  // update every time
    }
}

libDir = buildProps.getProperty("bukkitLibDir", "$buildDir/libs")
libDirs = buildProps.getProperty("bukkitLibDirs", "").split(";").filter { it.isNotEmpty() }
libDirs = libDirs + libDir
