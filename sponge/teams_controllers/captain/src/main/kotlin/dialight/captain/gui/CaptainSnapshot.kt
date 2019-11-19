package dialight.captain.gui

import dialight.captain.CaptainPlugin
import dialight.extensions.Server_getUsers
import dialight.extensions.itemStackOf
import dialight.guilib.events.ItemClickEvent
import dialight.guilib.snapshot.PlayersSnapshot
import dialight.guilib.snapshot.SortedPlayersPageBuilder
import jekarus.colorizer.Text_colorized
import org.spongepowered.api.entity.living.player.User
import org.spongepowered.api.item.inventory.property.Identifiable
import org.spongepowered.api.text.Text
import java.util.*

class CaptainSnapshot(val plugin: CaptainPlugin, id: Identifiable) : PlayersSnapshot<CaptainSnapshot.Page>(plugin.guilib!!, id) {


    class Builder(val plugin: CaptainPlugin, val id: Identifiable) : PlayersSnapshot.Builder(
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
            for (user in players.values) {
                slots.add(Page.Item(plugin, user.uniqueId, user.name))
            }
            return slots
        }

        fun build(): CaptainSnapshot {
            val slots = collect()
            val sorted = sort(slots)
            val snap = CaptainSnapshot(plugin, id)

            val maxLines = 6
            val maxColumns = 9
            val pages = SortedPlayersPageBuilder(maxColumns, maxLines - 1, sorted, chars).toList()
            for((name, slotCache) in pages) {
                val gui = Page(
                    snap,
                    Text_colorized(name),
                    maxColumns,
                    maxLines,
                    snap.pages.size,
                    pages.size
                )
                slotCache.forEach { index, slot ->
                    gui[index] = slot
                }
                snap.pages.add(gui)
            }
            if(snap.pages.isEmpty()) snap.pages.add(
                Page(
                    snap,
                    Text_colorized("Empty snapshot"),
                    maxColumns,
                    3,
                    0,
                    0
                )
            )
            return snap
        }
    }

    class Page(
        snap: CaptainSnapshot,
        title: Text,
        width: Int,
        height: Int,
        index: Int,
        total: Int
    ): PlayersSnapshot.Page(
        snap, title, width, height, index
    ) {

        class Item(val plugin: CaptainPlugin, uuid: UUID, name: String) : PlayersSnapshot.Page.Item(uuid, name) {

            override val item get() = getItem(0)

            fun getItem(i: Int) = itemStackOf {

            }

            override fun onClick(event: ItemClickEvent) {

            }

        }
    }

}