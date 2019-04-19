package dialight.guilib.mixin

import dialight.guilib.mixin.interfaces.IMixinAbstractInventoryProperty
import dialight.guilib.mixin.interfaces.IMixinCustomInventory
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.network.play.server.SPacketOpenWindow
import net.minecraft.util.text.TextComponentString
import org.spongepowered.api.item.inventory.Inventory
import org.spongepowered.api.item.inventory.property.AbstractInventoryProperty
import org.spongepowered.api.item.inventory.property.InventoryTitle
import org.spongepowered.api.text.Text


fun <K, V> AbstractInventoryProperty<K, V>.setValue(value: V) {
    (this as IMixinAbstractInventoryProperty<K, V>).setValue(value)
}

var Inventory.title: Text
    get() {
        // get property field
        val otitle = getInventoryProperty(InventoryTitle::class.java)
        if(otitle.isPresent) {
            val value = otitle.get().value!!
            return value
        }

        // get native field
        this as IMixinCustomInventory
        return Text.of(nativeTitle)
    }
    set(value) {
        this as IMixinCustomInventory
        val viewers = viewers

        // update native field
        nativeTitle = value.toPlain()

        // update property field
        val otitle = getInventoryProperty(InventoryTitle::class.java)
        if(otitle.isPresent) otitle.get().setValue(value)

        // update client side view
        val textcs = TextComponentString(value.toPlain())
        for(viewer in viewers) {
            viewer as? EntityPlayerMP ?: continue
            val container = viewer.openContainer ?: continue
            viewer.sendPacket(SPacketOpenWindow(container.windowId, archetype.id, textcs, capacity()))
        }
    }
