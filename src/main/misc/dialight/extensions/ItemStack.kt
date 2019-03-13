package dialight.extensions

import org.spongepowered.api.Sponge
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.data.manipulator.mutable.DisplayNameData
import org.spongepowered.api.data.manipulator.mutable.item.LoreData
import org.spongepowered.api.item.ItemType
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.api.item.inventory.ItemStackSnapshot
import org.spongepowered.api.text.Text

class ItemStackBuilderEx(val type: ItemType) {

    private val loreData = Sponge.getDataManager().getManipulatorBuilder(LoreData::class.java).get().create()
    private val nameData = Sponge.getDataManager().getManipulatorBuilder(DisplayNameData::class.java).get().create()

    private val toApply = arrayListOf<ItemStack.() -> Unit>()

    fun lore(lines: List<Text>): ItemStackBuilderEx {
        val lore = loreData.lore()
        lore.addAll(lines)
        loreData.set(lore)
        return this
    }
    fun lore(vararg lines: Text) = lore(listOf(*lines))

    fun name(name: Text): ItemStackBuilderEx {
        nameData.set(Keys.DISPLAY_NAME, name)
        return this
    }

    fun also(op: ItemStack.() -> Unit): ItemStackBuilderEx {
        toApply += op
        return this
    }

    fun build(): ItemStackSnapshot {
        val itemStack = ItemStack.builder()
            .itemType(type)
            .itemData(nameData)
            .itemData(loreData)
            .build()
        toApply.forEach { itemStack.it() }
        return itemStack.createSnapshot()
    }

}