package dialight.autorespawn

import dialight.modulelib.AbstractModule
import dialight.modulelib.NoControlModule
import org.spongepowered.api.Sponge
import org.spongepowered.api.text.channel.MessageReceiver

class AutoRespawnModule(val plugin: AutoRespawnPlugin) : NoControlModule(AutoRespawnModule.ID, "AutoRespawn") {

    companion object {
        val ID = "autorespawn"
    }

    override fun doEnable(invoker: MessageReceiver): Boolean {
        Sponge.getEventManager().registerListeners(plugin, plugin.listener)
        return true
    }

    override fun doDisable(invoker: MessageReceiver): Boolean {
        Sponge.getEventManager().unregisterListeners(plugin.listener)
        return true
    }


}