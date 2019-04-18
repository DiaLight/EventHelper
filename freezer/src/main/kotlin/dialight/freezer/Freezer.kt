package dialight.freezer

import com.google.common.collect.Iterators
import dialight.extensions.*
import dialight.freezer.events.FreezerEvent
import org.spongepowered.api.Sponge
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.plugin.PluginContainer
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World
import java.util.*

class Freezer {

    enum class Action {
        FREEZE,
        UNFREEZE,
        TOGGLE
    }

    enum class Group {
        ONLINE,
        OFFLINE,
        ALL
    }

    class Result : Iterable<FrozenPlayers.Frozen> {

        val freezed = ArrayList<FrozenPlayers.Frozen>()
        val unfreezed = ArrayList<FrozenPlayers.Frozen>()

        fun freeze(frozen: FrozenPlayers.Frozen) {
            freezed.add(frozen)
        }

        fun unfreeze(frozen: FrozenPlayers.Frozen) {
            unfreezed.add(frozen)
        }

        override fun iterator() = Iterators.concat(freezed.iterator(), unfreezed.iterator())

        fun sendReport(invoker: Player) {
            if (!freezed.isEmpty()) {
                invoker.sendMessage(FreezerMessages.tagged(freezed))
            }
            if (!unfreezed.isEmpty()) {
                invoker.sendMessage(FreezerMessages.untagged(unfreezed))
            }
        }

    }

    val frozen = FrozenPlayers()


    fun forEachOnline(action: (UUID, String) -> Unit) {
        for (frozen in frozen.map.values) {
            action(frozen.uuid, frozen.name)
        }
    }

    fun forEachOffline(action: (UUID, String) -> Unit) {
        for (frozen in frozen.map.values) {
            action(frozen.uuid, frozen.name)
        }
    }

    @Deprecated("Search for player by name is long time process. ")
    operator fun invoke(invoker: Player, action: Action, trgName: String): Result {
        val player = Server_getPlayer(trgName)
        if (player != null) return this.invoke(invoker, action, player)
        val user = Server_getUser(trgName)
        if(user != null) return this.invoke(invoker, action, user.uniqueId, user.name)
        FreezerMessages.notFound(invoker, trgName)
        return Result()
    }

    operator fun invoke(invoker: Player, action: Action, trg: UUID): Result {
        val player = Server_getPlayer(trg)
        if (player != null) return this.invoke(invoker, action, player)
        val offline = Server_getUser(trg)
        if(offline != null) return this.invoke(invoker, action, trg, offline.name)
        invoker.sendMessage(FreezerMessages.notFound(trg))
        return Result()
    }

    operator fun invoke(invoker: Player, action: Action, trg: Player): Result {
        val result = Result()
        when (action) {
            Action.FREEZE -> frozen.freezeOnline(invoker, trg, result)
            Action.UNFREEZE -> frozen.unfreezeOnline(invoker, trg, result)
            Action.TOGGLE -> frozen.toggleOnline(invoker, trg, result)
        }
        Sponge.getEventManager().post(FreezerEvent.ByPlayer(invoker, result))
        return result
    }

    operator fun invoke(invoker: Player, action: Action, trg: UUID, name: String): Result {
        val result = Result()
        when (action) {
            Action.FREEZE -> frozen.freezeOffline(invoker, trg, name, result)
            Action.UNFREEZE -> frozen.unfreezeOffline(invoker, trg, name, result)
            Action.TOGGLE -> frozen.toggleOffline(invoker, trg, name, result)
        }
        Sponge.getEventManager().post(FreezerEvent.ByPlayer(invoker, result))
        return result
    }

    operator fun invoke(invoker: Player, action: Action, group: Group): Result {
        val result = Result()
        when (group) {
            Group.OFFLINE -> when (action) {
                Action.FREEZE -> for (trg in Server_getOfflineUsers()) { frozen.freezeOffline(invoker, trg.uniqueId, trg.name, result) }
                Action.UNFREEZE -> frozen.unfreezeOfflineAll(invoker, result)
                Action.TOGGLE -> for (trg in Server_getOfflineUsers()) { frozen.toggleOffline(invoker, trg.uniqueId, trg.name, result) }
            }
            Group.ONLINE -> when (action) {
                Action.FREEZE -> for (trg in Server_getPlayers()) frozen.freezeOnline(invoker, trg, result)
                Action.UNFREEZE -> frozen.unfreezeOnlineAll(invoker, result)
                Action.TOGGLE -> for (trg in Server_getPlayers()) { frozen.toggleOnline(invoker, trg, result) }
            }
            Group.ALL -> when (action) {
                Action.FREEZE -> for (trg in Server_getUsers()) {
                    val player = trg.player.getOrNull()
                    if (player != null) {
                        frozen.freezeOnline(invoker, player, result)
                    } else {
                        frozen.freezeOffline(invoker, trg.uniqueId, trg.name, result)
                    }
                }
                Action.UNFREEZE -> for (trg in Server_getUsers()) {
                    val player = trg.player.getOrNull()
                    if (player != null) {
                        frozen.unfreezeOnline(invoker, player, result)
                    } else {
                        frozen.unfreezeOffline(invoker, trg.uniqueId, trg.name, result)
                    }
                }
                Action.TOGGLE -> for (trg in Server_getUsers()) {
                    val player = trg.player.getOrNull()
                    if (player != null) {
                        frozen.toggleOnline(invoker, player, result)
                    } else {
                        frozen.toggleOffline(invoker, trg.uniqueId, trg.name, result)
                    }
                }
            }
        }
        Sponge.getEventManager().post(FreezerEvent.ByPlayer(invoker, result))
        return result
    }


    //////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////plugin/API////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////

    operator fun invoke(invoker: PluginContainer, loc: Location<World>, action: Action, trg: UUID): Result {
        val player = Server_getPlayer(trg)
        if (player != null) return this.invoke(invoker, loc, action, player)
        val offline = Server_getUser(trg)
        if(offline != null) return this.invoke(invoker, loc, action, trg, offline.name)
        return Result()
    }

    private operator fun invoke(invoker: PluginContainer, loc: Location<World>, action: Action, trg: UUID, name: String): Result {
        val result = Result()
        when (action) {
            Action.FREEZE -> frozen.freezeOffline(invoker, loc, trg, name, result)
            Action.UNFREEZE -> frozen.unfreezeOffline(invoker, trg, name, result)
            Action.TOGGLE -> frozen.toggleOffline(invoker, loc, trg, name, result)
        }
        Sponge.getEventManager().post(FreezerEvent.ByPlugin(invoker, result))
        return result
    }

    operator fun invoke(invoker: PluginContainer, loc: Location<World>, action: Action, trg: Player): Result {
        val result = Result()
        when (action) {
            Action.FREEZE -> frozen.freezeOnline(invoker, loc, trg, result)
            Action.UNFREEZE -> frozen.unfreezeOnline(invoker, trg, result)
            Action.TOGGLE -> frozen.toggleOnline(invoker, loc, trg, result)
        }
        Sponge.getEventManager().post(FreezerEvent.ByPlugin(invoker, result))
        return result
    }

    operator fun invoke(invoker: PluginContainer, action: Action, trg: Player): Result {
        val result = Result()
        when (action) {
            Action.FREEZE -> frozen.freezeOnline(invoker, trg, result)
            Action.UNFREEZE -> frozen.unfreezeOnline(invoker, trg, result)
            Action.TOGGLE -> frozen.toggleOnline(invoker, trg, result)
        }
        Sponge.getEventManager().post(FreezerEvent.ByPlugin(invoker, result))
        return result
    }

    operator fun invoke(invoker: PluginContainer, loc: Location<World>?, action: Action, group: Group): Result {
        val result = Result()
        when (group) {
            Group.OFFLINE -> when (action) {
                Action.FREEZE -> {
                    if (loc == null) {
                        throw NullPointerException("location can not be null then using Action.FREEZE ang Group.OFFLINE")
                    }
                    for (trg in Server_getOfflineUsers()) { frozen.freezeOffline(invoker, loc, trg.uniqueId, trg.name, result) }
                }
                Action.UNFREEZE -> frozen.unfreezeOfflineAll(invoker, result)
                Action.TOGGLE -> {
                    if (loc == null) {
                        throw NullPointerException("location can not be null then using Action.TOGGLE ang Group.OFFLINE")
                    }
                    for (trg in Server_getOfflineUsers()) { frozen.toggleOffline(invoker, loc, trg.uniqueId, trg.name, result) }
                }
            }
            Group.ONLINE -> when (action) {
                Action.FREEZE -> for (trg in Server_getPlayers()) { frozen.freezeOnline(invoker, trg, result) }
                Action.UNFREEZE -> frozen.unfreezeOnlineAll(invoker, result)
                Action.TOGGLE -> for (trg in Server_getPlayers()) { frozen.toggleOnline(invoker, trg, result) }
            }
            Group.ALL -> when (action) {
                Action.FREEZE -> {
                    if (loc == null) {
                        throw NullPointerException("location can not be null then using Action.FREEZE ang Group.ALL")
                    }
                    for (trg in Server_getUsers()) {
                        val player = trg.player.getOrNull()
                        if (player != null) {
                            frozen.freezeOnline(invoker, player, result)
                        } else {
                            frozen.freezeOffline(invoker, loc, trg.uniqueId, trg.name, result)
                        }
                    }
                }
                Action.UNFREEZE -> for (trg in Server_getUsers()) {
                    val player = trg.player.getOrNull()
                    if (player != null) {
                        frozen.unfreezeOnline(invoker, player, result)
                    } else {
                        frozen.unfreezeOffline(invoker, trg.uniqueId, trg.name, result)
                    }
                }
                Action.TOGGLE -> {
                    if (loc == null) {
                        throw NullPointerException("location can not be null then using Action.TOGGLE ang Group.ALL")
                    }
                    for (trg in Server_getUsers()) {
                        val player = trg.player.getOrNull()
                        if (player != null) {
                            frozen.toggleOnline(invoker, player, result)
                        } else {
                            frozen.toggleOffline(invoker, loc, trg.uniqueId, trg.name, result)
                        }
                    }
                }
            }
        }
        Sponge.getEventManager().post(FreezerEvent.ByPlugin(invoker, result))
        return result
    }
    
    fun forEach(op: (FrozenPlayers.Frozen) -> Unit) = frozen.map.values.forEach(op)
    
}