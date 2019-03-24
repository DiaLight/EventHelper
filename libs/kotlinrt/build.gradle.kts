import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.spongepowered.gradle.meta.MetadataBaseExtension

buildscript {
    repositories {
        maven("http://files.minecraftforge.net/maven") { name = "forge" }
        maven("https://repo.spongepowered.org/maven") { name = "sponge" }
    }

    dependencies {
        classpath("net.minecraftforge.gradle:ForgeGradle:2.3-SNAPSHOT")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.21")
    }
}

plugins {
    id("org.spongepowered.plugin")
    kotlin("jvm")
    java
    base
}

val allVersion: String by rootProject.ext

var sponge_conf: MetadataBaseExtension.() -> Unit by ext
sponge_conf = {
    this.plugins.apply {
        this.create("kotlinrt") {
            this.meta.apply {
                this.setName("KotlinRt")
                setVersion(allVersion)
                this.authors.add("DiaLight")
            }
        }
    }
}
sponge.sponge_conf()

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

repositories {
    mavenCentral()
    maven("https://jitpack.io/") { name = "jitpack-repo" }
    maven("https://hub.spigotmc.org/nexus/content/groups/public/") { name = "spigotmc-repo" }
//    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") { name = "spigotmc-snapshots-repo" }
    maven("https://repo.spacehq.org/content/repositories/releases/") { name = "spacehq-repo" }
//    maven("https://repo.spacehq.org/content/repositories/snapshots/") { name = "spacehq-snapshots-repo" }
    maven("https://oss.sonatype.org/content/groups/public/") { name = "sonatype-repo" }
//    maven("https://oss.sonatype.org/content/repositories/snapshots") { name = "sonatype-snapshots-repo" }
//    maven("https://maven2.ontando.ru/repository/") { name = "angal-repo" }
    maven("http://repo.dmulloy2.net/content/groups/public/") { name = "dmulloy2-repo" }
    maven("http://maven.fabricmc.net/") { name = "fabricmc-repo" }
}

configurations {
    val shadow = create("shadow")
    this["compile"].extendsFrom(shadow)
}

dependencies {
    val shadow by configurations
    implementation("org.spongepowered:spongeapi:7.1.0-SNAPSHOT")
    shadow("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

tasks["jar"].apply { this as Jar
    from(sourceSets["main"].output)
    configurations["shadow"].forEach {
        from(zipTree(it.absoluteFile))
    }
}
task("joinJar", Jar::class) {
    from(sourceSets["main"].output)
    configurations["shadow"].forEach {
        from(zipTree(it.absoluteFile))
    }
    exclude("mcmod.info")
    baseName = "${project.name}-join"
}
task("fatJar", Jar::class) {
    from(sourceSets["main"].output)
    configurations["shadow"].forEach {
        from(zipTree(it.absoluteFile))
    }
}

tasks["build"].apply {
    dependsOn(":incrementVersion")
}
