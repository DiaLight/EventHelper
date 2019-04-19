package dialight.toollib

import dialight.extensions.*
import dialight.toollib.events.ToolInteractEvent
import org.spongepowered.api.block.BlockSnapshot
import org.spongepowered.api.block.BlockTypes
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.data.type.HandTypes
import org.spongepowered.api.data.value.ValueContainer
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.block.ChangeBlockEvent
import org.spongepowered.api.event.cause.EventContextKeys
import org.spongepowered.api.event.cause.entity.damage.source.BlockDamageSource
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource
import org.spongepowered.api.event.filter.cause.First
import org.spongepowered.api.event.item.inventory.DropItemEvent
import org.spongepowered.api.event.item.inventory.InteractItemEvent
import org.spongepowered.api.item.inventory.ItemStack
import java.util.*


class ToolListener(val plugin: ToolPlugin) {

    fun identifyTool(itemStack: ValueContainer<*>): Tool? {
        val id = Tool.parseId(itemStack) ?: return null
        return plugin.getTool(id)
    }

    @Listener
    fun onLeftClick(e: InteractItemEvent.Primary, @First player: Player) {
        val tool = identifyTool(e.itemStack) ?: return
        val toolEvent = ToolInteractEvent.Air(player, e.itemStack, ToolInteractEvent.Type.LEFT_CLICK)
        tool.onClick(toolEvent)
        if (toolEvent.deny) e.isCancelled = true
    }

    private var entityHit = false
    @Listener
    fun onRightClick(e: InteractItemEvent.Secondary, @First player: Player) {
        val oitem = e.context[EventContextKeys.USED_ITEM]
        if(!oitem.isPresent) return
        val item = oitem.get()
        val tool = identifyTool(item) ?: return
        val oentity = e.context[EventContextKeys.ENTITY_HIT]
        val oblock = e.context[EventContextKeys.BLOCK_HIT]
        if(oentity.isPresent) {
            val entity = oentity.get()
            entityHit = true
            val toolEvent = ToolInteractEvent.Entity(player, e.itemStack, entity, ToolInteractEvent.Type.RIGHT_CLICK)
            tool.onClick(toolEvent)
            if (toolEvent.deny) e.isCancelled = true
            return
        }
        if(oblock.isPresent) {
            val block = oblock.get()
            if(entityHit) {
                entityHit = false
                return
            }
            val toolEvent = if(block.state.type != BlockTypes.AIR) {
                ToolInteractEvent.Block(player, e.itemStack, block, ToolInteractEvent.Type.RIGHT_CLICK)
            } else {
                ToolInteractEvent.Air(player, e.itemStack, ToolInteractEvent.Type.RIGHT_CLICK)
            }
            tool.onClick(toolEvent)
            if (toolEvent.deny) e.isCancelled = true
        }
    }

    @Listener
    fun onBlockBreak(e: ChangeBlockEvent.Break, @First player: Player) {
        val block = e.transactions.firstOrNull()?.final ?: return
        if(!e.context.containsKey(EventContextKeys.BLOCK_HIT)) return
        if(!onBlockChangeUsingItem(e, player.getItemInHand(HandTypes.MAIN_HAND), player, block, false)) return
        if(!onBlockChangeUsingItem(e, player.getItemInHand(HandTypes.OFF_HAND), player, block, false)) return
    }

    @Listener
    fun onBlockPlace(e: ChangeBlockEvent.Place, @First player: Player) {
        val block = e.transactions.firstOrNull()?.final ?: return
        if(!onBlockChangeUsingItem(e, player.getItemInHand(HandTypes.MAIN_HAND), player, block, true)) return
        if(!onBlockChangeUsingItem(e, player.getItemInHand(HandTypes.OFF_HAND), player, block, true)) return
    }
    fun onBlockChangeUsingItem(e: ChangeBlockEvent, oitem: Optional<ItemStack>, player: Player, block: BlockSnapshot, place: Boolean): Boolean {
        val itemStack = oitem.getOrNull() ?: return false
        val tool = identifyTool(itemStack) ?: return false
        if(place) {
            val blockType = tool.type.block.getOrNull()
            if(blockType == null || block.state.type != blockType) {
                return false
            }
        }
        val toolEvent = ToolInteractEvent.Block(player, itemStack.createSnapshot(), block, ToolInteractEvent.Type.LEFT_CLICK)
        tool.onClick(toolEvent)
        if (toolEvent.deny) e.isCancelled = true
        return true
    }

    @Listener
    fun onDropItem(e: DropItemEvent.Pre, @First player: Player) {
        if(e.cause.containsType(DamageSource::class.java)) return
        val it = e.droppedItems.iterator()
        while(it.hasNext()) {
            val item = it.next()
            val tool = identifyTool(item) ?: continue
            val toolEvent = ToolInteractEvent.Air(player, item, ToolInteractEvent.Type.DROP)
            tool.onClick(toolEvent)
            if (toolEvent.deny) it.remove()
        }
    }

}