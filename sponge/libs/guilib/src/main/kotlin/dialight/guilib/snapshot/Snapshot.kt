package dialight.guilib.snapshot

import dialight.guilib.GuiPlugin
import dialight.guilib.IdentifiableView
import dialight.guilib.View
import dialight.guilib.events.GuiOutsideClickEvent
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.inventory.property.Identifiable
import org.spongepowered.api.scheduler.Task
import org.spongepowered.api.text.Text
import java.util.*

open class Snapshot<T : Snapshot.Page>(
    val guiplugin: GuiPlugin,
    val id: Identifiable
) {

    val pages = ArrayList<T>()

    val opened = hashMapOf<UUID, State>()

    fun state(player: Player): State = state(player.uniqueId)
    fun state(uuid: UUID): State = opened.getOrPut(uuid) { State() }

    fun current(player: Player): T = state(player).current
    fun current(uuid: UUID): T = state(uuid).current

    fun backward(player: Player): Boolean {
        val state = state(player)
        val prevIndex = state.pageIndex - 1
        if(prevIndex < 0) return false
        state.pageIndex = prevIndex
        state.changingPage = true
        guiplugin.openView(player, state.current)
        return true
    }

    fun forward(player: Player): Boolean {
        val state = state(player)
        val nextIndex = state.pageIndex + 1
        if(nextIndex >= pages.size) return false
        state.pageIndex = nextIndex
        state.changingPage = true
        guiplugin.openView(player, state.current)
        return true
    }

    inner class State {

        var pageIndex = 0

        val current: T
            get() {
                if(pageIndex >= pages.size) pageIndex = pages.size - 1
                return pages[pageIndex]
            }
        var changingPage = false

    }

    open class Page(
        val snap: Snapshot<*>,
        title: Text,
        width: Int,
        height: Int,
        val index: Int
    ) : IdentifiableView(
        snap.guiplugin, snap.id, title, width, height
    ) {

        override fun onOutsideClick(event: GuiOutsideClickEvent) {
            when(event.type) {
                GuiOutsideClickEvent.Type.LEFT -> {
                    snap.backward(event.player)
                }
                GuiOutsideClickEvent.Type.MIDDLE -> {
                    Task.builder().execute { task -> guiplugin.guistory.openPrev(event.player) }.submit(snap.guiplugin)
                }
                GuiOutsideClickEvent.Type.RIGHT -> {
                    snap.forward(event.player)
                }
            }
        }

    }

}