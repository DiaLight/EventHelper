package dialight.toollib.events

import dialight.extensions.Utils
import dialight.extensions.direction
import dialight.extensions.eyeLocation
import org.spongepowered.api.block.BlockSnapshot
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.entity.Entity
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.api.item.inventory.ItemStackSnapshot
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World


abstract class ToolInteractEvent(val player: Player, val item: ItemStackSnapshot, val action: ToolInteractEvent.Type) {

    var deny = true

    abstract val type: ToolInteractEvent.Target
    val sneaking = player.get(Keys.IS_SNEAKING).orElse(false)

    override fun toString() = String.format("%s, %s, %s", player.name, item.type, action)

    open fun lookingAtLoc() =
        Utils.getTargetBlock(
            player.eyeLocation,
            player.direction.normalize(),
            50
        ).add(.5, 1.0, .5)

    class Air(player: Player, item: ItemStackSnapshot, type: ToolInteractEvent.Type) : ToolInteractEvent(player, item, type) {

        override val type: Target = Target.AIR

        override fun toString() = String.format("ToolInteractEvent.Air{%s}", super.toString())

    }

    class Block(player: Player, item: ItemStackSnapshot, val block: BlockSnapshot, type: ToolInteractEvent.Type) : ToolInteractEvent(player, item, type) {

        override val type: Target = Target.BLOCK

        override fun toString() = String.format("ToolInteractEvent.Block{%s, %s}", super.toString(), block)

        override fun lookingAtLoc() = block.location.get().add(.5, 1.0, .5)

    }

    class Entity(player: Player, item: ItemStackSnapshot, val entity: org.spongepowered.api.entity.Entity, type: ToolInteractEvent.Type) : ToolInteractEvent(player, item, type) {

        override val type: Target = Target.ENTITY

        override fun toString() = String.format("ToolInteractEvent.Entity{%s, %s}", super.toString(), entity)

    }

    enum class Target {
        AIR,
        BLOCK,
        ENTITY
    }

    enum class Type {
        LEFT_CLICK,
        RIGHT_CLICK,
        DROP
    }


}
