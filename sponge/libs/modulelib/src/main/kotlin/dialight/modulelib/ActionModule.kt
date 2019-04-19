package dialight.modulelib

import org.spongepowered.api.text.channel.MessageReceiver

abstract class ActionModule(
    override val id: String,
    override val name: String
) : Module {

    override val enabled = false

    abstract fun onAction(invoker: MessageReceiver): Boolean

    override fun enable(invoker: MessageReceiver): Boolean {
        onAction(invoker)
        invoker.sendMessage(ModuleMessages.successEnable(this))
        return true
    }

    override fun disable(invoker: MessageReceiver): Boolean {
        invoker.sendMessage(ModuleMessages.cantDisable(this))
        return false
    }


}