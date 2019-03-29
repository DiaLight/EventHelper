package dialight.autorespawn

import dialight.modulelib.AbstractModule
import org.spongepowered.api.Sponge

class AutoRespawnModule(val plugin: AutoRespawnPlugin) : AbstractModule(AutoRespawnModule.ID, "AutoRespawn") {

    companion object {
        val ID = "autorespawn"
    }

    override fun onEnable(): Boolean {
        Sponge.getEventManager().registerListeners(plugin, plugin.listener)
        return true
    }

    override fun onDisable(): Boolean {
        Sponge.getEventManager().unregisterListeners(plugin.listener)
        return true
    }

}