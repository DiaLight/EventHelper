package dialight.captain.gui

import dialight.captain.CaptainPlugin
import dialight.extensions.itemStackOf
import dialight.guilib.View
import dialight.guilib.events.ItemClickEvent
import dialight.modulelib.ModuleMessages
import jekarus.colorizer.Text_colorized
import jekarus.colorizer.Text_colorizedList
import org.spongepowered.api.Sponge
import org.spongepowered.api.data.DataContainer
import org.spongepowered.api.data.DataView
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.data.manipulator.mutable.tileentity.BannerData
import org.spongepowered.api.data.persistence.DataFormats
import org.spongepowered.api.data.type.BannerPatternShapes
import org.spongepowered.api.data.type.DyeColors
import org.spongepowered.api.item.ItemTypes
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.common.data.util.DataQueries
import java.io.StringWriter


class CaptainItem(val plugin: CaptainPlugin) : View.Item {

    override val item get() = itemStackOf(ItemTypes.BANNER) {
//        builder {
//            val dm = Sponge.getDataManager().getManipulatorBuilder(BannerData::class.java).get().create()
//
//            val bc = dm.baseColor()
//            bc.set(DyeColors.YELLOW)
//            dm.set(bc)
//
//            val pl = dm.patternsList()
//            pl.add(BannerPatternShapes.TRIANGLES_TOP, DyeColors.BLACK)
//            pl.add(BannerPatternShapes.TRIANGLES_BOTTOM, DyeColors.BLACK)
//            pl.add(BannerPatternShapes.SQUARE_BOTTOM_LEFT, DyeColors.BLACK)
//            pl.add(BannerPatternShapes.SQUARE_TOP_RIGHT, DyeColors.BLACK)
//            pl.add(BannerPatternShapes.STRIPE_LEFT, DyeColors.BLACK)
//            pl.add(BannerPatternShapes.STRIPE_RIGHT, DyeColors.BLACK)
//            dm.set(pl)
//            itemData(dm)
//        }

        raw {
            itemDamageValue = 11
            nbt = """
{BlockEntityTag:{Patterns:[{Color:0,Pattern:"tts"},{Color:0,Pattern:"bts"},{Color:0,Pattern:"bl"},{Color:0,Pattern:"tr"},{Color:0,Pattern:"ls"},{Color:0,Pattern:"rs"}]}}
                    """.trimIndent()
        }
        hideMiscellaneous = true
        displayName = Text_colorized("|y|${plugin.module.name}")
        lore.addAll(
            Text_colorizedList(
                "|g|ЛКМ|y|: ${if(!plugin.module.enabled) "Вкл" else "Выкл"} модуль",
                "",
                "|g|Версия: |y|v" + plugin.container.version.orElse("null")
            )
        )
    }

    override fun onClick(event: ItemClickEvent) {
        when(event.type) {
            ItemClickEvent.Type.LEFT -> {
                plugin.system.arena.maybeLoc = event.player.location
                val newState = !plugin.module.enabled
                if(!plugin.module.toggle()) {
                    if(newState) {
                        event.player.sendMessage(ModuleMessages.cantEnable(plugin.module))
                    } else {
                        event.player.sendMessage(ModuleMessages.cantDisable(plugin.module))
                    }
                } else {
                    if(newState) {
                        event.player.sendMessage(ModuleMessages.successEnable(plugin.module))
                    } else {
                        event.player.sendMessage(ModuleMessages.successDisable(plugin.module))
                    }
                }
                event.updateItem = true
            }
            ItemClickEvent.Type.RIGHT -> {
                val dm = Sponge.getDataManager().getManipulatorBuilder(BannerData::class.java).get().create()

                val bc = dm.baseColor()
                bc.set(DyeColors.YELLOW)
                dm.set(bc)

                val pl = dm.patternsList()
                pl.add(BannerPatternShapes.TRIANGLES_TOP, DyeColors.BLACK)
                pl.add(BannerPatternShapes.TRIANGLES_BOTTOM, DyeColors.BLACK)
                pl.add(BannerPatternShapes.SQUARE_BOTTOM_LEFT, DyeColors.BLACK)
                pl.add(BannerPatternShapes.SQUARE_TOP_RIGHT, DyeColors.BLACK)
                pl.add(BannerPatternShapes.STRIPE_LEFT, DyeColors.BLACK)
                pl.add(BannerPatternShapes.STRIPE_RIGHT, DyeColors.BLACK)
                dm.set(pl)

                println(dm.toContainer().toJson())

                val its = ItemStack.builder()
                    .itemType(ItemTypes.LEATHER_CHESTPLATE)
                    .add(Keys.HIDE_MISCELLANEOUS, true)
                    .add(Keys.HIDE_ATTRIBUTES, true)
                    .build()
                println(its.toContainer().toJson())

                event.player.inventory.offer(its)

//                event.player.getItemInHand(HandTypes.MAIN_HAND).ifPresent {
//                    it.toContainer().get(DataQueries.UNSAFE_NBT).ifPresent {
//                        it as DataView
//                        println(it)
//                        println(it.toJson())
//                    }
//                }
            }
        }
    }

    fun String.toContainer(): DataContainer {
        return DataFormats.HOCON.read(this)
    }

    fun DataView.toJson(): String {
        val stringWriter = StringWriter()
        DataFormats.JSON.writeTo(stringWriter, this)
        return stringWriter.toString()
    }

}