package dialight.kotlin

import com.google.inject.Inject
import org.slf4j.Logger
import org.spongepowered.api.plugin.Plugin
import org.spongepowered.api.plugin.PluginContainer

@Plugin(id = "kotlinrt")
class KotlinPlugin @Inject constructor(
    val container: PluginContainer,
    val logger: Logger
) {
}