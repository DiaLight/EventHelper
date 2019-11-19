package dialight.teleporter.gui

import dialight.extensions.*
import dialight.guilib.events.ItemClickEvent
import dialight.guilib.simple.SimpleItem
import dialight.guilib.snapshot.PlayersSnapshot
import dialight.guilib.snapshot.SortedPlayersPageBuilder
import dialight.teleporter.SelectedPlayers
import dialight.teleporter.Teleporter
import dialight.teleporter.TeleporterPlugin
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

class TeleporterSnapshot(val plugin: TeleporterPlugin, id: Identifiable, val uuid: UUID, val name: String) : PlayersSnapshot<TeleporterSnapshot.Page>(plugin.guilib!!, id) {

    val selected: SelectedPlayers
        get() = plugin.teleporter[uuid]

    fun update(result: Teleporter.Result) {
        val sels = selected
        for(sel in result) {
            update(sel.uuid, sels)
        }
    }

    fun update(uuid: UUID, sels: SelectedPlayers = selected): Boolean {
        for(page in pages) {
            if(page.update(sels, uuid)) return true
        }
        return false
    }

    class Builder(val plugin: TeleporterPlugin, val id: Identifiable, val player: Player) : PlayersSnapshot.Builder(
        Arrays.asList(
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й',
            'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф',
            'х', 'ц', 'ч', 'ш', 'щ', 'ь', 'ы', 'ъ', 'э', 'ю', 'я',
            '_'
        )
    ) {

        private val snap = TeleporterSnapshot(plugin, id, player.uniqueId, player.name)

        private fun collect(): ArrayList<Page.Item> {
            val slots = ArrayList<Page.Item>()
            val players = HashMap<UUID, User>()
            for (user in Server_getUsers()) {
                players[user.uniqueId] = user
            }
            plugin.teleporter.forEach(player) {
                val user = players.remove(it.uuid)
                slots.add(Page.Item(snap, it.uuid, it.name))
            }
            for (user in players.values) {
                slots.add(Page.Item(snap, user.uniqueId, user.name))
            }
            slots.sortWith(kotlin.Comparator { o1, o2 ->
                val o1sel = o1.selected != null
                val o2sel = o2.selected != null
                if (o1sel && !o2sel) return@Comparator -1
                if (!o1sel && o2sel) return@Comparator 1
                if (!o2.online) return@Comparator -1
                return@Comparator 0
            })
            return slots
        }

        fun build(): TeleporterSnapshot {
            val slots = collect()
            val sorted = sort(slots)

            val maxLines = 6
            val maxColumns = 9
            val pages = SortedPlayersPageBuilder(maxColumns, maxLines - 1, sorted, chars).toList()
            for((name, slotCache) in pages) {
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
        snap: TeleporterSnapshot,
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
                displayName = Text_colorized("Телепорт")
                lore.addAll(Text_colorizedList(
                    "|y|Страница ${index + 1}/$total",
                    "|r|Для верного отображения",
                    "|r|заголовков столбцов",
                    "|r|используйте шрифт Unicode.",
                    "|w|Выделение",
                    "|a|ЛКМ|y|: Выделить всех",
                    "|a|ПКМ|y|: Снять со всех выделение",
                    "|a|Shift|y|+|a|ЛКМ|y|: Выделить всех, кто онлайн",
                    "|a|Shift|y|+|a|ПКМ|y|: Выделить всех, кто офлайн",
                    "|w|Навигация",
                    "|a|ЛКМ снаружи инвертаря|y|:",
                    "|y| Перейти на предыдущую страницу",
                    "|a|ПКМ снаружи инвертаря|y|:",
                    "|y| Перейти на следующую страницу",
                    "|a|СКМ снаружи инвертаря|y|:",
                    "|y| Вернуться назад",
                    "",
                    "|g|Плагин: |y|Телепорт",
                    "|g|Версия: |y|v" + snap.guiplugin.container.version.orElse("null")
                ))
            }) {
                when(it.type) {
                    ItemClickEvent.Type.LEFT -> {
                        snap.plugin.teleporter.invoke(it.player, Teleporter.Action.TAG, Teleporter.Group.ALL)
                    }
                    ItemClickEvent.Type.SHIFT_LEFT -> {
                        snap.plugin.teleporter.invoke(it.player, Teleporter.Action.TAG, Teleporter.Group.ONLINE)
                    }
                    ItemClickEvent.Type.RIGHT -> {
                        snap.plugin.teleporter.invoke(it.player, Teleporter.Action.UNTAG, Teleporter.Group.ALL)
                    }
                    ItemClickEvent.Type.SHIFT_RIGHT -> {
                        snap.plugin.teleporter.invoke(it.player, Teleporter.Action.TAG, Teleporter.Group.OFFLINE)
                    }
                }
            }
            val returnItem = SimpleItem(itemStackOf(ItemTypes.CHEST) {
                displayName = pageTitle
                lore.addAll(Text_colorizedList(
                    "|a|ЛКМ|y|: Вернуться назад"
                ))
            }) {
                when(it.type) {
                    ItemClickEvent.Type.LEFT -> {
                        Task.builder().execute { task -> guiplugin.guistory.openPrev(it.player) }.submit(snap.guiplugin)
                    }
                }
            }
            val backwardItem = SimpleItem(itemStackOf(ItemTypes.ARROW) {
                displayName = pageTitle
                lore.addAll(Text_colorizedList(
                    "|a|ЛКМ|y|: Перейти на предыдущую страницу"
                ))
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
                    "|a|ЛКМ|y|: Перейти на следующую страницу"
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

        fun update(sels: SelectedPlayers, uuid: UUID): Boolean {
            val (index, item) = this[uuid] ?: return false
            item as Item
            inventory[index] = item.getItem(sels)
            return true
        }

        class Item(val snap: TeleporterSnapshot, uuid: UUID, name: String) : PlayersSnapshot.Page.Item(uuid, name) {

            val selected: Teleporter.Selected?
                get() = snap.selected[uuid]

            val online: Boolean
                get() = Server_getUser(uuid)?.isOnline ?: false

            override val item get() = getItem(snap.selected)

            fun getItem(sels: SelectedPlayers) = itemStackOf {
                val selected = sels[uuid]
                type = if (online) {
                    if (selected != null) {
                        ItemTypes.DIAMOND
                    } else {
                        ItemTypes.COAL
                    }
                } else {
                    if (selected != null) {
                        ItemTypes.DIAMOND_ORE
                    } else {
                        ItemTypes.COAL_ORE
                    }
                }

                displayName = Text_colorized(if (online) this@Item.name else "${this@Item.name} |r|(Офлайн)")
                lore.addAll(Text_colorizedList(
                    if (selected != null) {
                        "|a|ЛКМ|y|: отменить выбор"
                    } else {
                        "|a|ЛКМ|y|: выбрать игрока"
                    },
//                        "~|g|ПКМ|y|: Телепортировать игрока к другому игроку",
                    "|a|Shift|y|+|a|ПКМ|y|: телепортироваться к игроку"
                ))
            }

            private fun teleport(player: Player): Boolean {
                val trg = Server_getPlayer(uuid)
                if (trg != null) {
                    Task.builder().execute { task -> player.location = trg.location }.submit(snap.plugin)
                    return true
                }
                val usr = Server_getUser(uuid) ?: return false
                val worldId = usr.worldUniqueId.getOrNull() ?: return false
                val toWorld = player.worldUniqueId.getOrNull() ?: return false
                if(toWorld != worldId) return false
                Task.builder().execute { task -> player.location = Location(player.world, usr.position) }.submit(snap.plugin)
                return true
            }

            override fun onClick(event: ItemClickEvent) {
                when (event.type) {
                    ItemClickEvent.Type.LEFT -> {
                        val result = snap.plugin.teleporter.invoke(event.player, Teleporter.Action.TOGGLE, uuid)
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
