package dialight.modulelib

import org.spongepowered.api.Sponge
import org.spongepowered.api.command.spec.CommandExecutor
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.channel.MessageReceiver

interface Module {

    val id: String
    val name: String
    val enabled: Boolean

    fun enable(invoker: MessageReceiver): Boolean

    fun disable(invoker: MessageReceiver): Boolean

    fun toggle(invoker: MessageReceiver) = if(enabled) disable(invoker) else enable(invoker)

}