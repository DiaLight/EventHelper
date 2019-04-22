import org.spongepowered.asm.gradle.plugins.MixinExtension
import org.spongepowered.asm.gradle.plugins.MixinGradlePlugin
import org.spongepowered.gradle.meta.MetadataBaseExtension


plugins {
    kotlin("jvm")
    java
    base
}

val pluginGroup: String by project
val pluginVersion: String by project

val allVersion: String by rootProject.ext
val mcVersion: String by rootProject.ext

var spongeDep: DependencyHandler.() -> Unit by project(":sponge").ext
var configureMixinProject: Project.(List<String>, List<String>, String) -> Unit by project(":sponge").ext

val deps = listOf(
    ":sponge:toollib",
    ":sponge:guilib",
    ":sponge:teleporter",
    ":sponge:ehgui"
)
val join = listOf(
    ":sponge:misc"
)

configureMixinProject(join, deps, "mixins.teams.json")

var sponge_conf: MetadataBaseExtension.() -> Unit by ext
sponge_conf = {
    this.plugins.apply {
        this.create("teams") {
            this.meta.apply {
                this.setName("Teams")
                setVersion(allVersion)
                this.authors.add("DiaLight")
                this.setDescription("Useful tool to help event masters with theirs job")
                this.dependencies.apply {
                    this.create("kotlinrt")
                    this.create("toollib")
                    this.create("guilib") {
                        this.optional = true
                    }
                    this.create("ehgui") {
                        this.optional = true
                    }
                    this.create("teleporter") {
                        this.optional = true
                    }
                }
            }
        }
    }
}
val mixin = the<MixinExtension>()
val sponge = the<MetadataBaseExtension>()

sponge.sponge_conf()

mixin.apply {
    this.add(sourceSets["main"], "mixins.teams.refmap.json")
}
