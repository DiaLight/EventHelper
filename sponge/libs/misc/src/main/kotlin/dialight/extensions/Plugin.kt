package dialight.extensions

import org.spongepowered.api.plugin.PluginManager


@Suppress("UNCHECKED_CAST")
fun <T> PluginManager.getPluginInstance(id: String): T? {
    val plugin = getPlugin(id).getOrNull() ?: return null
    val instance = plugin.instance.getOrNull() ?: return null
    return instance as T
}
