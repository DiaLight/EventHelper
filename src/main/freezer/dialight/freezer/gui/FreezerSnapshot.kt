package dialight.freezer.gui

import dialight.extensions.*
import dialight.freezer.Freezer
import dialight.freezer.FreezerPlugin
import dialight.guilib.View
import dialight.guilib.events.ItemClickEvent
import dialight.guilib.simple.SimpleItem
import dialight.guilib.snapshot.Snapshot
import jekarus.colorizer.Text_colorized
import jekarus.colorizer.Text_colorizedList
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.data.type.DyeColors
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.entity.living.player.User
import org.spongepowered.api.item.ItemTypes
import org.spongepowered.api.item.inventory.ItemStackSnapshot
import org.spongepowered.api.item.inventory.property.Identifiable
import org.spongepowered.api.scheduler.Task
import org.spongepowered.api.text.Text
import org.spongepowered.api.world.Location
import java.util.*

class FreezerSnapshot(plugin: FreezerPlugin, id: Identifiable) : Snapshot(plugin.guilib!!, id) {

    class Builder(val plugin: FreezerPlugin, val id: Identifiable, val player: Player) : Snapshot.Builder(
        Arrays.asList(
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й',
            'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф',
            'х', 'ц', 'ч', 'ш', 'щ', 'ь', 'ы', 'ъ', 'э', 'ю', 'я',
            '_'
        )
    ) {


        private fun collect(): ArrayList<Page.Item> {
            val slots = ArrayList<Page.Item>()
            val players = HashMap<UUID, User>()
            for (user in Server_getUsers()) {
                players[user.uniqueId] = user
            }
            plugin.freezer.forEach { frozen ->
                val user = players.remove(frozen.uniqueId)
                slots.add(Page.Item(plugin, frozen.uniqueId, frozen.name, true, user!!.isOnline))
            }
            for (user in players.values) {
                slots.add(Page.Item(plugin, user.uniqueId, user.name, false, user.isOnline))
            }
            slots.sortWith(kotlin.Comparator { o1, o2 ->
                if (o1.tagged && !o2.tagged) return@Comparator -1
                if (!o1.tagged && o2.tagged) return@Comparator 1
                if (!o2.online) return@Comparator -1
                return@Comparator 0
            })
            return slots
        }

        fun build(): FreezerSnapshot {
            val slots = collect()
            val sorted = sort(slots)
            val snap = FreezerSnapshot(plugin, id)

            val maxLines = 6
            val maxColumns = 9
            val pages = Snapshot.PageBuilderIt(maxLines - 1, maxColumns, sorted, chars).toList()
            for((name, slotCache) in pages) {
//                var newMaxLines = 0
//                for((index, slot) in slotCache) {
//                    val line = index % maxColumns
//                    if(line > newMaxLines) newMaxLines = line
//                }
//                println(newMaxLines)
                val gui = Page(snap, Text_colorized(name), maxColumns, maxLines, snap.pages.size, pages.size)
                slotCache.forEach { index, slot ->
                    gui[index] = slot
                }
                snap.pages.add(gui)
            }
            if(snap.pages.isEmpty()) snap.pages.add(Page(snap, Text_colorized("Empty snapshot"), maxColumns, 3, 0, 0))
            return snap
        }

    }

    class Page(
        snap: FreezerSnapshot,
        title: Text,
        width: Int,
        height: Int,
        pageIndex: Int,
        total: Int
    ): Snapshot.Page(
        snap, title, width, height, pageIndex, total
    ) {

        init {
            val botleft = (height - 1) * width
            val botmid = botleft + width / 2
            val pageTitle = Text_colorized("Страница ${pageIndex + 1}/$total")
            val descriptionItem = SimpleItem(ItemStackBuilderEx(ItemTypes.STAINED_GLASS_PANE)
                .also {
                    offer(Keys.DYE_COLOR, DyeColors.LIGHT_BLUE)
                }
                .name(Text_colorized("Замораживатель"))
                .lore(
                    Text_colorizedList(
                        "|y|Страница ${pageIndex + 1}/$total",
                        "|r|Для корректного обображения",
                        "|r|заголовков столбцов, используйте",
                        "|r|узкий шрифт Майнкрафта.",
                        "|g|ЛКМ снаружи инвертаря|y|:",
                        "|y| Перейти на предыдущую страницу",
                        "|g|ПКМ снаружи инвертаря|y|:",
                        "|y| Перейти на следующую страницу",
                        "|g|СКМ снаружи инвертаря|y|:",
                        "|y| Вернуться назад",
                        "",
                        "|g|Плагин: |y|Замораживатель",
                        "|g|Версия: |y|v" + snap.plugin.container.version.orElse("null")
                    )
                )
                .build()) {

            }
            val returnItem = SimpleItem(
                ItemStackBuilderEx(ItemTypes.CHEST)
                    .name(pageTitle)
                    .lore(
                        Text_colorizedList(
                            "|g|ЛКМ|y|: Вернуться назад"
                        )
                    )
                    .build()) {
                when(it.type) {
                    ItemClickEvent.Type.LEFT -> {
                        Task.builder().execute { task -> guiplugin.guistory.openPrev(it.player) }.submit(snap.plugin)
                    }
                }
            }
            val backwardItem = SimpleItem(
                ItemStackBuilderEx(ItemTypes.ARROW)
                    .name(pageTitle)
                    .lore(
                        Text_colorizedList(
                            "|g|ЛКМ|y|: Перейти на Предыдущую страницу"
                        )
                    )
                    .build()) {
                when(it.type) {
                    ItemClickEvent.Type.LEFT -> {
                        snap.backward(it.player)
                    }
                }
            }
            val forwardItem = SimpleItem(
                ItemStackBuilderEx(ItemTypes.ARROW)
                    .name(pageTitle)
                    .lore(
                        Text_colorizedList(
                            "|g|ЛКМ|y|: Перейти на следующую страницу"
                        )
                    )
                    .build()) {
                when(it.type) {
                    ItemClickEvent.Type.LEFT -> {
                        snap.forward(it.player)
                    }
                }
            }
            for (index in botleft..(botleft + width - 1)) {
                val item = if(index == botmid) {
                    returnItem
                } else if(pageIndex != 0 && index == botmid - 1) {
                    backwardItem
                } else if(pageIndex + 1 != total && index == botmid + 1) {
                    forwardItem
                } else {
                    descriptionItem
                }
                this[index] = item
            }
        }

        class Item(val plugin: FreezerPlugin, val uuid: UUID, name: String, var tagged: Boolean, val online: Boolean) : Snapshot.Page.Item(name) {

            override val item: ItemStackSnapshot
                get() = ItemStackBuilderEx(
                    if(online) {
                        if(tagged) {
                            ItemTypes.ICE
                        } else {
                            ItemTypes.COAL
                        }
                    } else {
                        if(tagged) {
                            ItemTypes.PACKED_ICE
                        } else {
                            ItemTypes.COAL_ORE
                        }
                    }
                )
                    .name(Text_colorized(if(online) name else "$name |r|(Офлайн)"))
                    .lore(
                        Text_colorizedList(
                            if (tagged) {
                                "|g|ЛКМ|y|: Заморозить игрока"
                            } else {
                                "|g|ЛКМ|y|: Разморозить игрока"
                            },
                            "|g|shift|y|+|g|ПКМ|y|: Телепортироваться к игроку"
                        )
                    )
                    .build()


            private fun teleport(player: Player): Boolean {
                val trg = Server_getPlayer(uuid)
                if (trg != null) {
                    Task.builder().execute { task -> player.location = trg.location }.submit(plugin)
                    return true
                }
                val usr = Server_getUser(uuid) ?: return false
                val oworldId = usr.worldUniqueId
                if(!oworldId.isPresent) return false
                val worldId = oworldId.get()
                val oToWorld = player.worldUniqueId
                if(!oToWorld.isPresent) return false
                val toWorld = oToWorld.get()
                if(toWorld != worldId) return false
                Task.builder().execute { task -> player.location = Location(player.world, usr.position) }.submit(plugin)
                return true
            }

            override fun onClick(event: ItemClickEvent) {
                when(event.type) {
                    ItemClickEvent.Type.LEFT -> {
                        val result = plugin.freezer.invoke(event.player, Freezer.Action.TOGGLE, uuid)
                        if (!result.getFreezed().isEmpty()) {
                            tagged = true
                        } else if (!result.getUnfreezed().isEmpty()) {
                            tagged = false
                        }
                        event.updateItem = true
                    }
//                    ItemClickEvent.Type.RIGHT -> {
//                        event.player.sendMessage(DefMes.notImplementedYet)
//                    }
                    ItemClickEvent.Type.SHIFT_RIGHT -> {
                        if(!teleport(event.player)) {
                            event.player.sendMessage(Text_colorized("|r|Не могу телепортировать к игроку"))
                        }
                    }
                }
            }
        }
    }

}
