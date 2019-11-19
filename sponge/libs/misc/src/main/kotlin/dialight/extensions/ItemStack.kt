package dialight.extensions

import org.spongepowered.api.Sponge
import org.spongepowered.api.data.DataContainer
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.data.manipulator.DataManipulator
import org.spongepowered.api.data.manipulator.mutable.ColoredData
import org.spongepowered.api.data.manipulator.mutable.DisplayNameData
import org.spongepowered.api.data.manipulator.mutable.DyeableData
import org.spongepowered.api.data.manipulator.mutable.item.LoreData
import org.spongepowered.api.data.persistence.DataFormats
import org.spongepowered.api.data.type.DyeColor
import org.spongepowered.api.data.type.DyeColors
import org.spongepowered.api.item.ItemType
import org.spongepowered.api.item.ItemTypes
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.api.text.Text
import org.spongepowered.api.util.Color
import org.spongepowered.common.data.util.DataQueries
import java.util.*
import kotlin.reflect.KProperty

class ItemStackBuilderEx(var type: ItemType = ItemTypes.NONE) {

    class ItemData<T : DataManipulator<*, *>>(val op: () -> T) {

        var value: T? = null

        operator fun getValue(itemStackBuilderEx: ItemStackBuilderEx, property: KProperty<*>): T {
            if(value == null) {
                value = op()
                itemStackBuilderEx.itemData.add(value!!)
            }
            return value!!
        }

    }

    private val itemData = arrayListOf<DataManipulator<*, *>>()

    private val loreData by ItemData { Sponge.getDataManager().getManipulatorBuilder(LoreData::class.java).get().create() }
    private val nameData by ItemData { Sponge.getDataManager().getManipulatorBuilder(DisplayNameData::class.java).get().create() }
    private val dyeData by ItemData { Sponge.getDataManager().getManipulatorBuilder(DyeableData::class.java).get().create() }
    private val colorData by ItemData { Sponge.getDataManager().getManipulatorBuilder(ColoredData::class.java).get().create() }


    private val toApply = arrayListOf<ItemStack.() -> Unit>()
    private val toBuilder = arrayListOf<ItemStack.Builder.() -> Unit>()
    private val toContainer = arrayListOf<DataContainer.() -> Unit>()

    init {

    }

    class LoreBuilder(val loreData: LoreData) : MutableCollection<Text> {
        override val size get() = loreData.lore().size()
        override fun contains(element: Text) = loreData.lore().contains(element)
        override fun containsAll(elements: Collection<Text>) = loreData.lore().containsAll(elements)
        override fun isEmpty() = loreData.lore().isEmpty

        override fun add(element: Text): Boolean {
            val lore = loreData.lore()
            val result = lore.add(element)
            loreData.set(lore)
            return true
        }

        override fun addAll(elements: Collection<Text>): Boolean {
            val lore = loreData.lore()
            val result = lore.addAll(elements)
            loreData.set(lore)
            return true
        }

        override fun clear() {
            val lore = loreData.lore()
            val result = lore.removeAll { true }
            loreData.set(lore)
        }

        override fun iterator() = loreData.lore().iterator()

        override fun remove(element: Text): Boolean {
            val lore = loreData.lore()
            val result = lore.remove(element)
            loreData.set(lore)
            return true
        }

        override fun removeAll(elements: Collection<Text>): Boolean {
            val lore = loreData.lore()
            val result = lore.removeAll(elements)
            loreData.set(lore)
            return true
        }

        override fun retainAll(elements: Collection<Text>): Boolean {
            val lore = loreData.lore()
            val result = lore.retainAll { elements.contains(it) }
            loreData.set(lore)
            return true
        }

    }

    var lore: MutableCollection<Text>
        get() = LoreBuilder(loreData)
        set(value) {
            val lore = LoreBuilder(loreData)
            lore.clear()
            lore.addAll(value)
        }

    fun lore(lines: List<Text>): ItemStackBuilderEx {
        lore.addAll(lines)
        return this
    }
    fun lore(vararg lines: Text) = lore.addAll(listOf(*lines))

    var displayName: Text
        get() = nameData.get(Keys.DISPLAY_NAME).get()
        set(value) { nameData.set(Keys.DISPLAY_NAME, value) }

    fun displayName(name: Text): ItemStackBuilderEx {
        this.displayName = name
        return this
    }

    var dyeColor: DyeColor
        get() = dyeData.get(Keys.DYE_COLOR).get()
        set(value) { dyeData.set(Keys.DYE_COLOR, value) }

    var color: Color
        get() = colorData.get(Keys.COLOR).get()
        set(value) { colorData.set(Keys.COLOR, value) }

    var hideMiscellaneous = false
    var hideAttributes = false

    inner class Raw {

        abstract inner class Data<T>(val op: DataContainer.(T) -> Unit) {
            abstract var value: T
            var registered = false

            operator fun getValue(raw: Raw, property: KProperty<*>) = value
            operator fun setValue(raw: Raw, property: KProperty<*>, v: T) {
                value = v
                if(!registered) {
                    container {
                        op(value)
                    }
                    registered = true
                }
            }
        }
        inner class IntData(op: DataContainer.(Int) -> Unit): Data<Int>(op) {
            override var value = 0
        }
        inner class StringData(op: DataContainer.(String) -> Unit): Data<String>(op) {
            override var value = ""
        }

        var itemDamageValue: Int by IntData { set(DataQueries.ITEM_DAMAGE_VALUE, it) }
        var nbt: String by StringData { set(DataQueries.UNSAFE_NBT, DataFormats.HOCON.read(it)) }

    }

    fun raw(op: Raw.() -> Unit) {
        Raw().op()
    }

    fun also(op: ItemStack.() -> Unit): ItemStackBuilderEx {
        toApply += op
        return this
    }

    fun builder(op: ItemStack.Builder.() -> Unit): ItemStackBuilderEx {
        toBuilder += op
        return this
    }

    fun container(op: DataContainer.() -> Unit): ItemStackBuilderEx {
        toContainer += op
        return this
    }

    fun build(): ItemStack {
        val bld = ItemStack.builder()
        if(!toContainer.isEmpty()) {
            val conteiner = ItemStack.builder().itemType(type).build().toContainer()
            for(op in toContainer) conteiner.op()
            bld.fromContainer(conteiner)
        }
        bld.itemType(type)
        for(id in itemData) bld.itemData(id)
        for(op in toBuilder) bld.op()
        if(hideMiscellaneous) bld.add(Keys.HIDE_MISCELLANEOUS, true)
        if(hideAttributes) bld.add(Keys.HIDE_ATTRIBUTES, true)

        val itemStack = bld.build()
        toApply.forEach { itemStack.it() }
        return itemStack
    }

}

fun itemStackOf(type: ItemType = ItemTypes.NONE, op: ItemStackBuilderEx.() -> Unit = {}): ItemStack {
    val builder = ItemStackBuilderEx(type)
    builder.op()
    return builder.build()
}
