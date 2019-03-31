package dialight.freezer.gui

import dialight.extensions.*
import dialight.freezer.Freezer
import dialight.freezer.FreezerPlugin
import dialight.freezer.FrozenPlayers
import dialight.guilib.events.ItemClickEvent
import dialight.guilib.simple.SimpleItem
import dialight.guilib.snapshot.PlayersSnapshot
import dialight.guilib.snapshot.SortedPlayersPageBuilder
import jekarus.colorizer.Text_colorized
import jekarus.colorizer.Text_colorizedList
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.data.type.DyeColors
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.entity.living.player.User
import org.spongepowered.api.item.ItemTypes
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.api.item.inventory.property.Identifiable
import org.spongepowered.api.scheduler.Task
import org.spongepowered.api.text.Text
import org.spongepowered.api.world.Location
import java.util.*

class FreezerSnapshot(val plugin: FreezerPlugin, id: Identifiable) : PlayersSnapshot<FreezerSnapshot.Page>(plugin.guilib!!, id) {

    fun update(result: Freezer.Result) {
        for(frz in result) {
            update(frz.uuid)
        }
    }

    fun update(uuid: UUID): Boolean {
        val frozen = plugin.freezer.frozen
        for(page in pages) {
            if(page.update(frozen, uuid)) return true
        }
        return false
    }

    class Builder(val plugin: FreezerPlugin, val id: Identifiable) : PlayersSnapshot.Builder(
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
                val user = players.remove(frozen.uuid)
                slots.add(Page.Item(plugin, frozen.uuid, frozen.name))
            }
            for (user in players.values) {
                slots.add(Page.Item(plugin, user.uniqueId, user.name))
            }
            slots.sortWith(kotlin.Comparator { o1, o2 ->
                val o1tg = o1.tagged != null
                val o2tg = o2.tagged != null
                if (o1tg && !o2tg) return@Comparator -1
                if (!o1tg && o2tg) return@Comparator 1
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
            val pages = SortedPlayersPageBuilder(maxColumns, maxLines - 1, sorted, chars).toList()
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
        index: Int,
        total: Int
    ): PlayersSnapshot.Page(
        snap, title, width, height, index
    ) {

        init {
            val botleft = (height - 1) * width
            val botmid = botleft + width / 2
            val pageTitle = Text_colorized("Страница ${index + 1}/$total")
            val descriptionItem = SimpleItem(itemStackOf(ItemTypes.STAINED_GLASS_PANE) {
                dyeColor = DyeColors.LIGHT_BLUE
                displayName = Text_colorized("Замораживатель")
                lore.addAll(
                    Text_colorizedList(
                        "|y|Страница ${index + 1}/$total",
                        "|r|Для верного отображения",
                        "|r|заголовков столбцов",
                        "|r|используйте шрифт Unicode.",
                        "|w|Выделение",
                        "|g|ЛКМ|y|: Выделить всех",
                        "|g|ПКМ|y|: Снять со всех выделение",
                        "|g|Shift|y|+|g|ЛКМ|y|: Выделить всех, кто онлайн",
                        "|g|Shift|y|+|g|ПКМ|y|: Выделить всех, кто офлайн",
                        "|w|Навигация",
                        "|g|ЛКМ снаружи инвертаря|y|:",
                        "|y| Перейти на предыдущую страницу",
                        "|g|ПКМ снаружи инвертаря|y|:",
                        "|y| Перейти на следующую страницу",
                        "|g|СКМ снаружи инвертаря|y|:",
                        "|y| Вернуться назад",
                        "",
                        "|g|Плагин: |y|Замораживатель",
                        "|g|Версия: |y|v" + snap.guiplugin.container.version.orElse("null")
                    )
                )
            }) {
                when(it.type) {
                    ItemClickEvent.Type.LEFT -> {
                        snap.plugin.freezer.invoke(it.player, Freezer.Action.FREEZE, Freezer.Group.ALL)
                    }
                    ItemClickEvent.Type.SHIFT_LEFT -> {
                        snap.plugin.freezer.invoke(it.player, Freezer.Action.FREEZE, Freezer.Group.ONLINE)
                    }
                    ItemClickEvent.Type.RIGHT -> {
                        snap.plugin.freezer.invoke(it.player, Freezer.Action.UNFREEZE, Freezer.Group.ALL)
                    }
                    ItemClickEvent.Type.SHIFT_RIGHT -> {
                        snap.plugin.freezer.invoke(it.player, Freezer.Action.FREEZE, Freezer.Group.OFFLINE)
                    }
                }
            }
            val returnItem = SimpleItem(itemStackOf(ItemTypes.CHEST) {
                displayName = pageTitle
                lore.addAll(
                    Text_colorizedList(
                        "|g|ЛКМ|y|: Вернуться назад"
                    )
                )
            }) {
                when(it.type) {
                    ItemClickEvent.Type.LEFT -> {
                        Task.builder().execute { task -> guiplugin.guistory.openPrev(it.player) }.submit(snap.guiplugin)
                    }
                }
            }
            val backwardItem = SimpleItem(itemStackOf(ItemTypes.ARROW) {
                displayName = pageTitle
                lore.addAll(
                    Text_colorizedList(
                        "|g|ЛКМ|y|: Перейти на Предыдущую страницу"
                    )
                )
            }) {
                when(it.type) {
                    ItemClickEvent.Type.LEFT -> {
                        snap.backward(it.player)
                    }
                }
            }
            val forwardItem = SimpleItem(itemStackOf(ItemTypes.ARROW) {
                displayName = pageTitle
                lore.addAll(Text_colorizedList(
                    "|g|ЛКМ|y|: Перейти на следующую страницу"
                ))
            }) {
                when(it.type) {
                    ItemClickEvent.Type.LEFT -> {
                        snap.forward(it.player)
                    }
                }
            }
            for (itemId in botleft..(botleft + width - 1)) {
                val item = if(itemId == botmid) {
                    returnItem
                } else if(index != 0 && itemId == botmid - 1) {
                    backwardItem
                } else if(index + 1 != total && itemId == botmid + 1) {
                    forwardItem
                } else {
                    descriptionItem
                }
                this[itemId] = item
            }
        }

        fun update(frozen: FrozenPlayers, uuid: UUID): Boolean {
            val (index, item) = this[uuid] ?: return false
            item as Item
            inventory[index] = item.getItem(frozen)
            return true
        }

        class Item(val plugin: FreezerPlugin, uuid: UUID, name: String) : PlayersSnapshot.Page.Item(uuid, name) {

            val tagged: FrozenPlayers.Frozen?
                get() = plugin.freezer.frozen.map[uuid]

            val online: Boolean
                get() = Server_getUser(uuid)?.isOnline ?: false


            override val item get() = getItem(plugin.freezer.frozen)

            fun getItem(frzs: FrozenPlayers) = itemStackOf {
                val tagged = frzs.map[uuid] != null
                type = if (online) {
                    if (tagged) {
                        ItemTypes.SNOWBALL
                    } else {
                        ItemTypes.COAL
                    }
                } else {
                    if (tagged) {
                        ItemTypes.PACKED_ICE
                    } else {
                        ItemTypes.COAL_ORE
                    }
                }
                displayName = Text_colorized(if(online) this@Item.name else "${this@Item.name} |r|(Офлайн)")
                lore.addAll(Text_colorizedList(
                    if (tagged) {
                        "|g|ЛКМ|y|: Заморозить игрока"
                    } else {
                        "|g|ЛКМ|y|: Разморозить игрока"
                    },
                    "|g|shift|y|+|g|ПКМ|y|: Телепортироваться к игроку"
                ))
            }

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
                when (event.type) {
                    ItemClickEvent.Type.LEFT -> {
                        val result = plugin.freezer.invoke(event.player, Freezer.Action.TOGGLE, uuid)
                    }
//                    ItemClickEvent.Type.RIGHT -> {
//                        event.player.sendMessage(DefMes.notImplementedYet)
//                    }
                    ItemClickEvent.Type.SHIFT_RIGHT -> {
                        if (!teleport(event.player)) {
                            event.player.sendMessage(Text_colorized("|r|Не могу телепортировать к игроку"))
                        }
                    }
                }
            }
        }
    }

}
