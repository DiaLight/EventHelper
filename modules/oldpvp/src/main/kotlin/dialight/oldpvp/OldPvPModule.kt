package dialight.oldpvp

import dialight.modulelib.AbstractModule
import org.spongepowered.api.Sponge

class OldPvPModule(val plugin: OldPvPPlugin) : AbstractModule(OldPvPModule.ID, "OldPvP") {

    companion object {
        val ID = "oldpvp"
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