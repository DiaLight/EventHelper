package dialight.teleporter

import dialight.extensions.*
import dialight.teleporter.TeleporterMessages
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

    class Result {

        private val tagged = ArrayList<String>()
        private val untagged = ArrayList<String>()

        fun tag(name: String) {
            tagged.add(name)
        }

        fun untag(name: String) {
            untagged.add(name)
        }

        fun sendReport(invoker: Player) {
            if (!tagged.isEmpty()) {
                invoker.sendMessage(TeleporterMessages.tagged(tagged))
            }
            if (!untagged.isEmpty()) {
                invoker.sendMessage(TeleporterMessages.untagged(untagged))
            }
        }

        fun getTagged(): List<String> = tagged
        fun getUntagged(): List<String> = untagged

        fun add(result: Result) {
            this.tagged.addAll(result.tagged)
            this.untagged.addAll(result.untagged)
        }
    }

    private val registry = HashMap<UUID, SelectedPlayers>()
    operator fun get(invoker: Player): SelectedPlayers {
        return registry.getOrPut(invoker.uniqueId) { SelectedPlayers() }
    }
    
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

    operator fun invoke(invoker: Player, action: Action, trg: UUID, name: String): Result {
        val result = Result()
        val players = get(invoker)
        when (action) {
            Action.TAG -> if (players.tagOffline(trg, name)) {
                result.tag(name)
            }
            Action.UNTAG -> if (players.untagOffline(trg, name)) {
                result.untag(name)
            }
            Action.TOGGLE -> if (players.toggletagOffline(trg, name)) {
                result.tag(name)
            } else {
                result.untag(name)
            }
        }
        return result
    }

    private inner class Pair constructor(trg: User) {
        val uuid: UUID = trg.uniqueId
        val name: String = trg.name
    }

    operator fun invoke(invoker: Player, action: Action, group: Group): Result {
        val result = Result()
        val stream: Stream<Pair>
        when (group) {
            Group.OFFLINE -> stream =
                Server_getUsers().stream().filter { !it.isOnline }
                    .map { Pair(it) }
            Group.ONLINE -> stream =
                Server_getPlayers().stream().map { Pair(it) }
            Group.ALL -> stream =
                Server_getUsers().stream().map { Pair(it) }
            else -> stream = Stream.empty()
        }
        val players = get(invoker)
        when (action) {
            Action.TAG -> stream.forEach { p ->
                if (players.tagOffline(p.uuid, p.name)) {
                    result.tag(p.name)
                }
            }
            Action.UNTAG -> stream.forEach { p ->
                if (players.untagOffline(p.uuid, p.name)) {
                    result.untag(p.name)
                }
            }
            Action.TOGGLE -> stream.forEach { p ->
                if (players.toggletagOffline(p.uuid, p.name)) {
                    result.tag(p.name)
                } else {
                    result.untag(p.name)
                }
            }
        }
        return result
    }

    fun forEach(player: Player, action: (UUID, String) -> Unit) = get(player).forEach(action)

    fun sendTargetsReport(invoker: Player) {
        val targets = get(invoker)
        if (targets.selected.isEmpty()) {
            invoker.sendMessage(TeleporterMessages.PlayersBaseIsEmpty)
        } else {
            invoker.sendMessage(TeleporterMessages.targets(targets.selected.values))
        }
    }
}