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

val pluginGroup: String by project
val pluginVersion: String by project

val allVersion: String by rootProject.ext
val mcVersion: String by rootProject.ext

val conf: MetadataBaseExtension.() -> Unit = {
    this.plugins.apply {
        this.create("modulelib") {
            this.meta.apply {
                this.setName("ModuleLib")
                setVersion(allVersion)
                this.authors.add("DiaLight")
                this.setDescription("Useful tool to help event masters with theirs job")
                this.dependencies.apply {
                    this.create("kotlinrt")
                }
            }
        }
    }
}
ext.set("sponge_conf", conf)
sponge.conf()

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

configurations {
    val shadow = this.create("shadow")
    this.getByName("compile").extendsFrom(shadow)
}

val join = listOf(":misc")

dependencies {
    val shadow by configurations
    implementation("org.spongepowered:spongeapi:7.1.0-SNAPSHOT")
    implementation("org.spongepowered:spongevanilla:1.12.2-7.1.5")
    implementation("org.spongepowered:mixin:0.7.5-SNAPSHOT")
    implementation(kotlin("stdlib-jdk8"))
    join.forEach { implementation(project(it)) }
}

task("joinJar", Jar::class) {
    from(sourceSets["main"].output)
    exclude("mcmod.info")
    baseName = "${project.name}-join"
}
task("fatJar", Jar::class) {
    from(sourceSets["main"].output)
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
repositories {
    mavenCentral()
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}