package dialight.extensions

import org.spongepowered.api.plugin.PluginManager


@Suppress("UNCHECKED_CAST")
fun <T> PluginManager.getPluginInstance(id: String): T? {
    val oplugin = getPlugin(id)
    if(!oplugin.isPresent) return null
    val plugin = oplugin.get()
    val oinstance = plugin.instance
    if(!oinstance.isPresent) return null
    return oinstance.get() as T
}
