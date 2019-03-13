package dialight.guilib.mixin

//import org.spongepowered.common.interfaces.entity.player.IMixinEntityPlayerMP
import dialight.guilib.mixin.interfaces.IMixinAbstractInventoryProperty
import dialight.guilib.mixin.interfaces.IMixinCustomInventory
import dialight.guilib.mixin.interfaces.IMixinEntityPlayerMP
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.network.NetHandlerPlayServer
import net.minecraft.network.Packet
import net.minecraft.network.play.server.SPacketOpenWindow
import net.minecraft.util.text.TextComponentString
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.inventory.Inventory
import org.spongepowered.api.item.inventory.property.AbstractInventoryProperty
import org.spongepowered.api.item.inventory.property.InventoryTitle
import org.spongepowered.api.text.Text


val Player.currentWindowId: Int
    get() = (this as IMixinEntityPlayerMP).currentWindowId

fun EntityPlayerMP.sendPacket(packetIn: Packet<*>) {
    (connection as NetHandlerPlayServer).sendPacket(packetIn)
}
fun Player.sendPacket(packetIn: Packet<*>) {
    (connection as NetHandlerPlayServer).sendPacket(packetIn)
}
