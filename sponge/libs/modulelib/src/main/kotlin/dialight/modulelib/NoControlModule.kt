package dialight.modulelib

import org.spongepowered.api.text.channel.MessageReceiver

abstract class NoControlModule(
    override val id: String,
    override val name: String,
    enable: Boolean = false) : AbstractModule(id, name) {


    override var enabled: Boolean = enable

    override fun enable(invoker: MessageReceiver): Boolean {
        val success = super.enable(invoker)
        if(success) {
            enabled = true
        }
        return success
    }

    override fun disable(invoker: MessageReceiver): Boolean {
        val success = super.disable(invoker)
        if(success) {
            enabled = false
        }
        return success
    }

}