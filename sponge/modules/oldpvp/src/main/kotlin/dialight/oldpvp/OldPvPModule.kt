package dialight.oldpvp

import dialight.modulelib.AbstractModule
import dialight.modulelib.NoControlModule
import org.spongepowered.api.Sponge
import org.spongepowered.api.text.channel.MessageReceiver

class OldPvPModule(val plugin: OldPvPPlugin) : NoControlModule(OldPvPModule.ID, "OldPvP") {

    companion object {
        val ID = "oldpvp"
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