package dialight.modulelib

import org.spongepowered.api.text.channel.MessageReceiver

abstract class AbstractModule(
    override val id: String,
    override val name: String
    ) : Module {

    override fun enable(invoker: MessageReceiver): Boolean {
        if(enabled) return false
        if(!doEnable(invoker)) {
            invoker.sendMessage(ModuleMessages.cantEnable(this))
            return false
        }
        invoker.sendMessage(ModuleMessages.successEnable(this))
        return true
    }

    override fun disable(invoker: MessageReceiver): Boolean {
        if(!enabled) return false
        if(!doDisable(invoker)) {
            invoker.sendMessage(ModuleMessages.cantDisable(this))
            return false
        }
        invoker.sendMessage(ModuleMessages.successDisable(this))
        return true
    }

    abstract fun doEnable(invoker: MessageReceiver): Boolean
    abstract fun doDisable(invoker: MessageReceiver): Boolean

}