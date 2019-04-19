import java.util.*

plugins {
    kotlin("jvm")
    java
    base
}

subprojects {
    repositories {
        mavenCentral()
        maven("https://jitpack.io/") { name = "jitpack-repo" }
        maven("https://repo.destroystokyo.com/repository/maven-public/") { name = "destroystokyo-repo" }
        maven("https://hub.spigotmc.org/nexus/content/groups/public/") { name = "spigotmc-repo" }
//    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") { name = "spigotmc-snapshots-repo" }
        maven("https://oss.sonatype.org/content/groups/public/") { name = "sonatype-repo" }
    }
}

var buildProps: Properties by rootProject.ext

var libDir: String by ext
var bukkitDep: DependencyHandler.() -> Unit by ext

bukkitDep = {
    implementation("com.destroystokyo.paper:paper-api:1.13.2-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.6")
    annotationProcessor("org.projectlombok:lombok:1.18.6")
}

libDir = buildProps.getProperty("bukkitLibDir", "$buildDir/libs")
