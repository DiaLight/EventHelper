package dialight.guilib.mixin

//import org.spongepowered.common.interfaces.entity.player.IMixinEntityPlayerMP
import dialight.guilib.mixin.interfaces.IMixinEntityPlayerMP
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.network.NetHandlerPlayServer
import net.minecraft.network.Packet
import org.spongepowered.api.entity.living.player.Player


val Player.currentWindowId: Int
    get() = (this as IMixinEntityPlayerMP).currentWindowId

fun EntityPlayerMP.sendPacket(packetIn: Packet<*>) {
    (connection as NetHandlerPlayServer).sendPacket(packetIn)
}
fun Player.sendPacket(packetIn: Packet<*>) {
    (connection as NetHandlerPlayServer).sendPacket(packetIn)
}
