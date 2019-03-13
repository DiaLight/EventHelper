package dialight.teleporter.gui

import dialight.extensions.*
import dialight.guilib.GuiPlugin
import dialight.guilib.IdentifiableView
import dialight.guilib.View
import dialight.guilib.events.GuiOutsideClickEvent
import dialight.guilib.events.ItemClickEvent
import dialight.guilib.simple.SimpleItem
import dialight.teleporter.Teleporter
import dialight.teleporter.TeleporterPlugin
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

// не перемещать курсор
// разный шрифт - разное смещение
// эмеральд заменяет итем в руке
// таггер выделить всех/очистить всех. Выделить онлайн/оффлайн

class TaggerSnapshot(val plugin: TeleporterPlugin) {

    val pages = ArrayList<Page>()

    companion object {
        val chars = Arrays.asList(
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й',
            'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф',
            'х', 'ц', 'ч', 'ш', 'щ', 'ь', 'ы', 'ъ', 'э', 'ю', 'я',
            '_'
        )
    }

    var pageIndex = 0

    val current: Page
        get() = pages[pageIndex]
    var changingPage = false

    private class PageBuilderIt(
        val maxLines: Int,
        val maxColumns: Int,
        val sorted: Map<Char, List<Page.Item>>
    ): Iterator<Pair<String, HashMap<Int, Page.Item>>> {

        private var currentChar: Char = ' '
        private val charsIt = chars.iterator()
        private var slotsIt: Iterator<Page.Item> = emptyArray<Page.Item>().iterator()

        override fun hasNext(): Boolean {
            if(slotsIt.hasNext()) return true
            nextChar()
            return slotsIt.hasNext()
        }

        override fun next(): Pair<String, HashMap<Int, Page.Item>> {
            val builder = ColumnBuilderIt(maxLines, maxColumns)
            while(builder.hasNext() && hasNext()) {
                builder.next(slotsIt, currentChar)
            }
            return Pair(builder.nameBuilder.toString(), builder.slotCache)
        }

        fun nextChar() {
            while(charsIt.hasNext()) {
                currentChar = charsIt.next()
                val slots = sorted[currentChar]
                if(slots != null) {
                    slotsIt = slots.iterator()
                    break
                }
            }
        }

        class ColumnBuilderIt(
            val maxLines: Int,
            val maxColumns: Int
        ) {
            var displayChar: Char = ' '
            val nameBuilder = StringBuilder()
            val slotCache = HashMap<Int, Page.Item>()
            var column = 0

            fun hasNext(): Boolean {
                return column != maxColumns
            }

            fun next(slotsIt: Iterator<Page.Item>, currentChar: Char) {
                displayChar = currentChar
                while(slotsIt.hasNext() && column != maxColumns) {
                    nameBuilder.append("  ").append(displayChar)
                    nameBuilder.append(if (column % 2 == 0) " " else "  ")
                    displayChar = ' '
                    var row = 0
                    while (slotsIt.hasNext()) {
                        val slot = slotsIt.next()
                        slotCache[column + row * maxColumns] = slot
                        row++
                        if (row == maxLines) {
                            break
                        }
                    }
                    column++
                }
            }
        }
    }
    
    class Builder(val plugin: TeleporterPlugin, val id: Identifiable, val player: Player) {
        
        private fun collect(): ArrayList<Page.Item> {
            val slots = ArrayList<Page.Item>()
            val players = HashMap<UUID, User>()
            for (user in Server_getUsers()) {
                players[user.uniqueId] = user
            }
            plugin.teleporter.forEach(player) { uuid, name ->
                val user = players.remove(uuid)
                slots.add(Page.Item(plugin, uuid, name, true, user!!.isOnline))
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
        
        private fun sort(slots: ArrayList<Page.Item>): HashMap<Char, MutableList<Page.Item>> {
            val sorted = HashMap<Char, MutableList<Page.Item>>()
            for (slot in slots) {
                val c = findChar(slot.name)
                val charSlots = sorted.getOrPut(c) { ArrayList() }
                charSlots.add(slot)
            }
            return sorted
        }
        
        private fun findChar(name: String): Char {
            for (c in name.toLowerCase()) {
                if (chars.contains(c)) return c
            }
            return '_'
        }
        
        fun build(): TaggerSnapshot {
            val slots = collect()
            val sorted = sort(slots)
            val snap = TaggerSnapshot(plugin)

            val maxLines = 6
            val maxColumns = 9
            val pages = PageBuilderIt(maxLines - 1, maxColumns, sorted).toList()
            for((name, slotCache) in pages) {
//                var newMaxLines = 0
//                for((index, slot) in slotCache) {
//                    val line = index % maxColumns
//                    if(line > newMaxLines) newMaxLines = line
//                }
//                println(newMaxLines)
                val gui = Page(plugin.guilib!!, id, snap, Text_colorized(name), maxColumns, maxLines, snap.pages.size, pages.size)
                slotCache.forEach { index, slot ->
                    gui[index] = slot
                }
                snap.pages.add(gui)
            }
            if(snap.pages.isEmpty()) snap.pages.add(Page(plugin.guilib!!, id, snap, Text_colorized("Empty snapshot"), maxColumns, 3, 0, 0))
            return snap
        }

    }

    class Page(plugin: GuiPlugin,
               id: Identifiable,
               val snap: TaggerSnapshot,
               title: Text,
               width: Int,
               height: Int,
               pageIndex: Int,
               total: Int
    ): IdentifiableView(
        plugin, id, title, width, height
    ) {

        init {
            val botleft = (height - 1) * width
            val botmid = botleft + width / 2
            val pageTitle = Text_colorized("Страница ${pageIndex + 1}/$total")
            val descriptionItem = SimpleItem(ItemStackBuilderEx(ItemTypes.STAINED_GLASS_PANE)
                .also {
                    offer(Keys.DYE_COLOR, DyeColors.LIGHT_BLUE)
                }
                .name(Text_colorized("Таггер"))
                .lore(Text_colorizedList(
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
                    "|g|Плагин: |y|Телепортер",
                    "|g|Версия: |y|v" + plugin.container.version.orElse("null")
                ))
                .build()) {

            }
            val returnItem = SimpleItem(ItemStackBuilderEx(ItemTypes.CHEST)
                .name(pageTitle)
                .lore(Text_colorizedList(
                    "|g|ЛКМ|y|: Вернуться назад"
                ))
                .build()) {
                when(it.type) {
                    ItemClickEvent.Type.LEFT -> {
                        Task.builder().execute { task -> guiplugin.guistory.openPrev(it.player) }.submit(plugin)
                    }
                }
            }
            val backwardItem = SimpleItem(ItemStackBuilderEx(ItemTypes.ARROW)
                .name(pageTitle)
                .lore(Text_colorizedList(
                    "|g|ЛКМ|y|: Перейти на Предыдущую страницу"
                ))
                .build()) {
                when(it.type) {
                    ItemClickEvent.Type.LEFT -> {
                        snap.backward(it.player)
                    }
                }
            }
            val forwardItem = SimpleItem(ItemStackBuilderEx(ItemTypes.ARROW)
                .name(pageTitle)
                .lore(Text_colorizedList(
                    "|g|ЛКМ|y|: Перейти на следующую страницу"
                ))
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

        override fun onOutsideClick(event: GuiOutsideClickEvent) {
            when(event.type) {
                GuiOutsideClickEvent.Type.LEFT -> {
                    snap.backward(event.player)
                }
                GuiOutsideClickEvent.Type.MIDDLE -> {
                    Task.builder().execute { task -> guiplugin.guistory.openPrev(event.player) }.submit(snap.plugin)
                }
                GuiOutsideClickEvent.Type.RIGHT -> {
                    snap.forward(event.player)
                }
            }
        }

        class Item(val plugin: TeleporterPlugin, val uuid: UUID, val name: String, var tagged: Boolean, val online: Boolean) : View.Item {

            override val item: ItemStackSnapshot
                get() = ItemStackBuilderEx(
                    if(online) {
                        if(tagged) {
                            ItemTypes.DIAMOND
                        } else {
                            ItemTypes.COAL
                        }
                    } else {
                        if(tagged) {
                            ItemTypes.DIAMOND_ORE
                        } else {
                            ItemTypes.COAL_ORE
                        }
                    }
                )
                .name(Text_colorized(if(online) name else "$name |r|(Офлайн)"))
                .lore(
                    Text_colorizedList(
                        if (tagged) {
                            "|g|ЛКМ|y|: Убрать пометку с игрока"
                        } else {
                            "|g|ЛКМ|y|: Пометить игрока"
                        },
//                        "~|g|ПКМ|y|: Телепортировать игрока к другому игроку",
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
                        val result = plugin.teleporter.invoke(event.player, Teleporter.Action.TOGGLE, uuid)
                        if (!result.getTagged().isEmpty()) {
                            tagged = true
                        } else if (!result.getUntagged().isEmpty()) {
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

    fun backward(player: Player): Boolean {
        val prevIndex = pageIndex - 1
        if(prevIndex < 0) return false
        pageIndex = prevIndex
        changingPage = true
        plugin.guilib!!.openView(player, current)
        return true
    }

    fun forward(player: Player): Boolean {
        val nextIndex = pageIndex + 1
        if(nextIndex >= pages.size) return false
        pageIndex = nextIndex
        changingPage = true
        plugin.guilib!!.openView(player, current)
        return true
    }

}