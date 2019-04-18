package dialight.teams.gui

import dialight.extensions.Server_getPlayers
import dialight.extensions.Server_getUser
import dialight.extensions.itemStackOf
import dialight.guilib.View
import dialight.guilib.events.ItemClickEvent
import dialight.guilib.simple.SimpleItem
import dialight.guilib.snapshot.SequentialPageBuilder
import dialight.guilib.snapshot.Snapshot
import dialight.teams.Server_getScoreboard
import dialight.teams.TeamsMessages
import dialight.teams.TeamsPlugin
import dialight.teams.mixin.removeMemberFromTeams
import dialight.teleporter.Teleporter
import jekarus.colorizer.Text_colorized
import jekarus.colorizer.Text_colorizedList
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.item.ItemTypes
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.api.item.inventory.property.Identifiable
import org.spongepowered.api.scoreboard.Team
import org.spongepowered.api.text.Text
import java.util.*
import java.util.stream.Collectors

class TeamsSnapshot(val plugin: TeamsPlugin, val uuid: UUID, id: Identifiable) : Snapshot<TeamsSnapshot.Page>(plugin.guilib!!, id) {

    val addTeamItem = SimpleItem(itemStackOf(ItemTypes.NETHER_STAR) {
        displayName = Text_colorized("Добавить команду")
        lore.addAll(Text_colorizedList(
            "|a|ЛКМ|y|: добавить команду",
            "|a|Shift|y|+|a|ЛКМ|y|: удалить все команды",
            "|a|Shift|y|+|a|ПКМ|y|: очистить все команды"
        ))
    }) {
        when(it.type) {
            ItemClickEvent.Type.LEFT -> {
                plugin.guilib!!.openGui(it.player, AddTeamGui(plugin))
            }
            ItemClickEvent.Type.SHIFT_LEFT -> {
                val sb = Server_getScoreboard()
                for(team in sb.teams) {
                    team.unregister()
                }
            }
            ItemClickEvent.Type.SHIFT_RIGHT -> {
                val sb = Server_getScoreboard()
                for(team in sb.teams) {
                    for(member in team.members) {
                        sb.removeMemberFromTeams(member)
                    }
                }
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
            items += Item(team)
        }
        items.add(addTeamItem)

        pages.clear()
        val maxLines = 6
        val maxColumns = 9
        val pagesitems = SequentialPageBuilder(maxColumns, maxLines - 1, maxColumns, items).toList()
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

    inner class Item(val team: Team) : View.Item {

        override val item get() = itemStackOf {
            val selected = plugin.selected[uuid] == team
            type = if(selected) ItemTypes.LEATHER_HELMET else ItemTypes.LEATHER_CHESTPLATE
            displayName = Text_colorized("|a|${team.name}")
            color = team.color.color
            hideAttributes = true
            hideMiscellaneous = true
            if(selected) {
                lore.addAll(Text_colorizedList(
                    "|a|ЛКМ|y|: убрать выделение"
                ))
            } else {
                lore.addAll(Text_colorizedList(
                    "|a|ЛКМ|y|: выбрать команду, ",
                    "|y| чтобы затем управлять её",
                    "|y| участниками через телепортер"
                ))
            }
            lore.addAll(Text_colorizedList(
                "|a|Shift|y|+|a|ЛКМ|y|: добавить всех в команду",
                "|a|Shift|y|+|a|ПКМ|y|: очистить команду",
                "|a|СКМ|y|: удалить команду"
            ))
        }

        override fun onClick(event: ItemClickEvent) {
            val users = team.members.stream()
                .map { Server_getUser(it.toPlain()) }
                .filter { it != null }
                .map { it!! }
                .collect(Collectors.toList())
            when(event.type) {
                ItemClickEvent.Type.LEFT -> {
                    plugin.teleporter?.let { teleporter ->
                        val selected = plugin.selected.remove(event.player.uniqueId)
                        teleporter.teleporter.invoke(event.player, Teleporter.Action.UNTAG, Teleporter.Group.ALL)
                        teleporter.teleporter.invoke(event.player, Teleporter.Action.TAG, users)
                        if(selected == null || selected.name != team.name) {
                            plugin.selected[event.player.uniqueId] = team
                            event.player.sendMessage(TeamsMessages.selectedTeam(team))
                        } else {
                            event.player.sendMessage(TeamsMessages.unselectedTeam(selected))
                        }
                    }
                }
                ItemClickEvent.Type.SHIFT_LEFT -> {
                    for(player in Server_getPlayers()) {
                        team.addMember(player.teamRepresentation)
                    }
                }
                ItemClickEvent.Type.MIDDLE -> {
                    if(team.unregister()) {
                        plugin.teleporter?.let { teleporter ->
                            val selected = plugin.selected[event.player.uniqueId]
                            if(selected != null && selected.name == team.name) {
                                plugin.selected.remove(event.player.uniqueId)
                                teleporter.teleporter.invoke(event.player, Teleporter.Action.UNTAG, Teleporter.Group.ALL)
                                event.player.sendMessage(TeamsMessages.unselectedTeam(selected))
                            }
                        }
                        event.player.sendMessage(TeamsMessages.removeTeam(team))
                    }
                }
                ItemClickEvent.Type.SHIFT_RIGHT -> {
                    for(member in team.members) {
                        team.removeMember(member)
                    }
                }
            }
        }

    }


}