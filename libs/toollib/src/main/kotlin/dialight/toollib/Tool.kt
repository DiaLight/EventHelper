package dialight.toollib

import dialight.extensions.ItemStackBuilderEx
import dialight.extensions.itemStackOf
import dialight.toollib.events.ToolInteractEvent
import jekarus.colorizer.Colorizer
import jekarus.colorizer.Text_colorized
import org.spongepowered.api.Sponge
import org.spongepowered.api.data.DataHolder
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.data.manipulator.mutable.DisplayNameData
import org.spongepowered.api.data.value.ValueContainer
import org.spongepowered.api.item.ItemType
import org.spongepowered.api.item.ItemTypes
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.api.item.inventory.ItemStackSnapshot
import org.spongepowered.api.text.Text
import java.util.*
import java.util.regex.Pattern

abstract class Tool(val id: String) {

    companion object {

        private val prefix = Colorizer.apply("|dgr|Tool ID: |gr|")
        private val pattern = Pattern.compile("^${prefix.toPlain()}(.*)$")

        fun parseId(itemStack: ValueContainer<*>): String? {
            val olore = itemStack.get(Keys.ITEM_LORE)
            if(!olore.isPresent) return null
            return parseId(olore.get())
        }
        fun parseId(lore: List<Text>): String? {
            val last = lore.lastOrNull() ?: return null
            val matcher = pattern.matcher(last.toPlain())
            if (!matcher.matches()) return null
            return matcher.group(1)
        }

    }

    val uuid: UUID = let {
        val rnd = Random(id.hashCode().toLong())
        UUID(rnd.nextLong(), rnd.nextLong())
    }

    abstract val type: ItemType
    abstract val title: Text
    abstract val lore: List<Text>

    open val build: ItemStackBuilderEx.() -> Unit = {}

    fun buildItem() = itemStackOf(type) {
        builder {
            itemData(UniqueIdDataImpl(uuid))
        }
        displayName = title
        lore.addAll(this@Tool.lore)
        this@Tool.build(this)
        lore.add(Text.of(prefix, id))
    }

    open fun onClick(e: ToolInteractEvent) {
//        val oid = e.item.get(ITEM_ID)
//        if(!oid.isPresent) return
//        val id = oid.get()
    }

}
