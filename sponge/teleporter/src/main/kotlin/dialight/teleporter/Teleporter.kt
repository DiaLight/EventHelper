package dialight.teleporter

import com.google.common.collect.Iterators
import dialight.extensions.*
import dialight.teleporter.event.TeleporterEvent
import org.spongepowered.api.Sponge
import org.spongepowered.api.entity.living.player.Player
import java.util.*
import java.util.UUID
import org.spongepowered.api.entity.living.player.User
import java.util.stream.Stream


class Teleporter {

    enum class Action {
        TAG,
        UNTAG,
        TOGGLE
    }

    enum class Group {
        ONLINE,
        OFFLINE,
        ALL
    }

    class Result : Iterable<Selected> {

        val selected = ArrayList<Selected>()
        val unselected = ArrayList<Selected>()

        fun select(sel: Selected) {
            selected.add(sel)
        }

        fun unselect(sel: Selected) {
            unselected.add(sel)
        }

        override fun iterator() = Iterators.concat(selected.iterator(), unselected.iterator())


        fun sendReport(invoker: Player) {
            if (!selected.isEmpty()) {
                invoker.sendMessage(TeleporterMessages.selected(selected))
            }
            if (!unselected.isEmpty()) {
                invoker.sendMessage(TeleporterMessages.unselected(unselected))
            }
        }

        fun add(result: Result) {
            this.selected.addAll(result.selected)
            this.unselected.addAll(result.unselected)
        }
    }

    private val registry = HashMap<UUID, SelectedPlayers>()
    operator fun get(invoker: Player) = get(invoker.uniqueId)
    operator fun get(uuid: UUID) = registry.getOrPut(uuid) { SelectedPlayers() }
    
    @Deprecated("Search for player by name is long time process. ")
    operator fun invoke(invoker: Player, action: Action, trgName: String): Result {
        val user = Server_getUser(trgName)
        if(user != null) return invoke(invoker, action, user.uniqueId, user.name)
        invoker.sendMessage(TeleporterMessages.notFound(trgName))
        return Result()
    }

    operator fun invoke(invoker: Player, action: Action, trg: UUID): Result {
        val user = Server_getUser(trg)
        if(user != null) return this.invoke(invoker, action, trg, user.name)
        invoker.sendMessage(TeleporterMessages.notFound(trg))
        return Result()
    }

    operator fun invoke(invoker: Player, action: Action, trg: Player) =
        invoke(invoker, action, trg.uniqueId, trg.name)

    operator fun invoke(invoker: Player, action: Action, trg: UUID, name: String?): Result {
        if(name == null) return invoke(invoker, action, trg)
        val result = Result()
        val selections = get(invoker)
        val sel = Selected(trg, name)
        when (action) {
            Action.TAG -> if (selections.tagOffline(sel)) {
                result.select(sel)
            }
            Action.UNTAG -> if (selections.untagOffline(sel)) {
                result.unselect(sel)
            }
            Action.TOGGLE -> if (selections.toggletagOffline(sel)) {
                result.select(sel)
            } else {
                result.unselect(sel)
            }
        }
        Sponge.getEventManager().post(TeleporterEvent(invoker, result))
        return result
    }

    data class Selected(
        val uuid: UUID,
        val name: String
    ) {

        constructor(trg: User) : this(trg.uniqueId, trg.name)

        fun getPlayer(): Player? = Server_getPlayer(uuid)

        fun getUser(): User = Server_getUser(uuid)!!

    }

    operator fun invoke(invoker: Player, action: Action, group: Group): Result {
        val stream: Stream<Selected>
        when (group) {
            Group.OFFLINE -> stream =
                Server_getUsers().stream().filter { !it.isOnline }
                    .map { Selected(it) }
            Group.ONLINE -> stream =
                Server_getPlayers().stream().map { Selected(it) }
            Group.ALL -> stream =
                Server_getUsers().stream().map { Selected(it) }
            else -> stream = Stream.empty()
        }
        return invoke(invoker, action, stream)
    }
    operator fun invoke(invoker: Player, action: Action, list: Collection<User>): Result {
        return invoke(invoker, action, list.stream().map { Selected(it) })
    }
    private fun invoke(invoker: Player, action: Action, stream: Stream<Selected>): Result {
        val result = Result()
        val players = get(invoker)
        when (action) {
            Action.TAG -> stream.forEach {
                if (players.tagOffline(it)) {
                    result.select(it)
                }
            }
            Action.UNTAG -> stream.forEach {
                if (players.untagOffline(it)) {
                    result.unselect(it)
                }
            }
            Action.TOGGLE -> stream.forEach {
                if (players.toggletagOffline(it)) {
                    result.select(it)
                } else {
                    result.unselect(it)
                }
            }
        }
        Sponge.getEventManager().post(TeleporterEvent(invoker, result))
        return result
    }

    fun forEach(player: Player, action: (Selected) -> Unit) = get(player).forEach(action)

    fun sendTargetsReport(invoker: Player) {
        val targets = get(invoker)
        if (targets.selected.isEmpty()) {
            invoker.sendMessage(TeleporterMessages.PlayersBaseIsEmpty)
        } else {
            invoker.sendMessage(TeleporterMessages.targets(targets.selected.values))
        }
    }

}