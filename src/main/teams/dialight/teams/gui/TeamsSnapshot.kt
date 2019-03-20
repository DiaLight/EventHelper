package dialight.teams.gui

import dialight.extensions.ItemStackBuilderEx
import dialight.extensions.Server_getUser
import dialight.extensions.dyeColor
import dialight.guilib.View
import dialight.guilib.events.ItemClickEvent
import dialight.guilib.simple.SimpleItem
import dialight.guilib.simple.TextInputGui
import dialight.guilib.snapshot.SequentialPageBuilder
import dialight.guilib.snapshot.Snapshot
import dialight.teams.Server_getScoreboard
import dialight.teams.TeamsMessages
import dialight.teams.TeamsPlugin
import dialight.teleporter.Teleporter
import dialight.teleporter.TeleporterTool
import jekarus.colorizer.Text_colorized
import jekarus.colorizer.Text_colorizedList
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.entity.living.player.User
import org.spongepowered.api.item.ItemTypes
import org.spongepowered.api.item.inventory.property.Identifiable
import org.spongepowered.api.text.Text
import java.util.stream.Collectors

class TeamsSnapshot(val plugin: TeamsPlugin, id: Identifiable) : Snapshot<TeamsSnapshot.Page>(plugin.guilib!!, id) {

    val addTeamItem = SimpleItem(ItemStackBuilderEx(ItemTypes.NETHER_STAR)
        .name(Text_colorized("Добавить команду"))
        .lore(Text_colorizedList(
            ""
        ))
        .build()) {
        when(it.type) {
            ItemClickEvent.Type.LEFT -> {
                plugin.guilib!!.openGui(it.player, AddTeamGui(plugin))
            }
            ItemClickEvent.Type.RIGHT -> {
                val gui = TextInputGui(plugin.guilib!!, Identifiable(), Text_colorized("TextInput"))
                gui.first = SimpleItem(ItemStackBuilderEx(ItemTypes.WOOL)
                    .name(Text_colorized("First"))
                    .lore(Text_colorizedList(
                        "hello"
                    ))
                    .build())
                gui.second = SimpleItem(ItemStackBuilderEx(ItemTypes.ACTIVATOR_RAIL)
                    .name(Text_colorized("sec"))
                    .lore(Text_colorizedList(
                        "hello"
                    ))
                    .build())
//                gui.result = SimpleItem(ItemStackBuilderEx(ItemTypes.GLASS)
//                    .name(Text_colorized("res"))
//                    .lore(Text_colorizedList(
//                        "hello"
//                    ))
//                    .build())
                plugin.guilib!!.openGui(it.player, gui)
            }
        }
    }

    init {
        updateItems()
    }

    fun updateItems() {
        val items = arrayListOf<View.Item>()
        val scoreboard = Server_getScoreboard()
        for(team in scoreboard.teams) {
            items += SimpleItem(ItemStackBuilderEx(ItemTypes.WOOL)
                .name(Text_colorized(team.name))
                .also {
                    offer(Keys.DYE_COLOR, team.color.dyeColor)
                }
                .lore(Text_colorizedList(
                    "|g|ЛКМ|y|: выбрать текущую команду",
                    "|y| и игроков в телепортере",
                    "|g|ПКМ|y|: выбрать игроков в телепортере",
                    "|g|СКМ|y|: удалить команду"
                ))
                .build()) {
                val users = team.members.stream()
                    .map { Server_getUser(it.toPlain()) }
                    .filter { it != null }
                    .map { it!! }
                    .collect(Collectors.toList())
                when(it.type) {
                    ItemClickEvent.Type.LEFT -> {
                        plugin.teleporter?.let { teleporter ->
                            val selected = plugin.selected.value
                            plugin.selected.value = null
                            teleporter.teleporter.invoke(it.player, Teleporter.Action.UNTAG, Teleporter.Group.ALL)
                            teleporter.teleporter.invoke(it.player, Teleporter.Action.TAG, users)
                            if(selected == null || selected.name != team.name) {
                                plugin.selected.value = team
                                it.player.sendMessage(TeamsMessages.selectedTeam(team))
                            } else {
                                it.player.sendMessage(TeamsMessages.unselectedTeam(selected))
                            }
                        }
                    }
                    ItemClickEvent.Type.RIGHT -> {
                        plugin.teleporter?.let { teleporter ->
                            teleporter.teleporter.invoke(it.player, Teleporter.Action.UNTAG, Teleporter.Group.ALL)
                            teleporter.teleporter.invoke(it.player, Teleporter.Action.TAG, users)
                        }
                    }
                    ItemClickEvent.Type.MIDDLE -> {
                        if(team.unregister()) {
                            it.player.sendMessage(TeamsMessages.removeTeam(team))
                        }
                    }
                }
            }
        }
        items.add(addTeamItem)

        pages.clear()
        val maxLines = 6
        val maxColumns = 9
        val pagesitems = SequentialPageBuilder(maxColumns, maxLines - 1, items).toList()
        var index = 0
        for(pageitems in pagesitems) {
            val page = Page(this, Text_colorized("Команды ${index + 1}/${pagesitems.size}"), maxColumns, maxLines, index, pagesitems.size)
            pageitems.forEach { slotIndex, slot ->
                page[slotIndex] = slot
            }
            pages += page
            index++
        }
    }

    class Page(
        snap: TeamsSnapshot,
        title: Text,
        width: Int,
        height: Int,
        index: Int,
        total: Int
    ): Snapshot.Page(snap, title, width, height, index) {

    }



}