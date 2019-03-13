package dialight.freezer

import dialight.extensions.*
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.entity.living.player.User
import org.spongepowered.api.plugin.PluginContainer
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World
import java.util.*
import java.util.function.BiConsumer

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

    class Result {

        private val freezed = ArrayList<String>()
        private val unfreezed = ArrayList<String>()

        fun freeze(name: String) {
            freezed.add(name)
        }

        fun unfreeze(name: String) {
            unfreezed.add(name)
        }

        fun sendReport(invoker: Player) {
            if (!freezed.isEmpty()) {
                invoker.sendMessage(FreezerMessages.tagged(freezed))
            }
            if (!unfreezed.isEmpty()) {
                invoker.sendMessage(FreezerMessages.untagged(unfreezed))
            }
        }

        fun getFreezed(): List<String> {
            return freezed
        }

        fun getUnfreezed(): List<String> {
            return unfreezed
        }

    }

    val frozen = FrozenPlayers()


    fun forEachOnline(action: (UUID, String) -> Unit) {
        for (frozen in frozen.idFrozen.values) {
            action(frozen.uniqueId, frozen.name)
        }
    }

    fun forEachOffline(action: (UUID, String) -> Unit) {
        for (frozen in frozen.offlineFrozen.values) {
            action(frozen.uniqueId, frozen.name)
        }
    }

    @Deprecated("Search for player by name is long time process. ")
    operator fun invoke(invoker: Player, action: Action, trgName: String): Result {
        val trg = Server_getUser(trgName)
        if(trg == null) {
            FreezerMessages.notFound(invoker, trgName)
            return Result()
        }
        return this.___invoke(invoker, action, trg)
    }

    private fun ___invoke(invoker: Player, action: Action, trg: User): Result {
        val player = trg.player.getOrNull()
        if (player != null) {
            return this.invoke(invoker, action, player)
        }
        return this.invoke(invoker, action, trg.uniqueId, trg.name)
    }

    operator fun invoke(invoker: Player, action: Action, trg: UUID): Result {
        val player = Server_getPlayer(trg)
        if (player != null) {
            return this.invoke(invoker, action, player)
        }
        val offline = Server_getUser(trg)
        if(offline != null) {
            return this.invoke(invoker, action, trg, offline.name)
        }
        invoker.sendMessage(FreezerMessages.notFound(trg))
        return Result()
    }

    operator fun invoke(invoker: Player, action: Action, trg: Player): Result {
        val result = Result()
        when (action) {
            Action.FREEZE -> if (frozen.freezeOnline(invoker, trg, false)) {
                result.freeze(trg.name)
            }
            Action.UNFREEZE -> if (frozen.unfreezeOnline(invoker, trg)) {
                result.unfreeze(trg.name)
            }
            Action.TOGGLE -> if (frozen.toggleOnline(invoker, trg)) {
                result.freeze(trg.name)
            } else {
                result.unfreeze(trg.name)
            }
        }
        return result
    }

    operator fun invoke(invoker: Player, action: Action, trg: UUID, name: String): Result {
        val result = Result()
        when (action) {
            Action.FREEZE -> if (frozen.freezeOffline(invoker, trg, name)) {
                result.freeze(name)
            }
            Action.UNFREEZE -> if (frozen.unfreezeOffline(invoker, trg, name)) {
                result.unfreeze(name)
            }
            Action.TOGGLE -> if (frozen.toggleOffline(invoker, trg, name)) {
                result.freeze(name)
            } else {
                result.unfreeze(name)
            }
        }
        return result
    }

    operator fun invoke(invoker: Player, action: Action, group: Group): Result {
        val result = Result()
        when (group) {
            Group.OFFLINE -> when (action) {
                Action.FREEZE -> for (trg in Server_getOfflineUsers()) {
                    if (frozen.freezeOffline(invoker, trg.uniqueId, trg.name)) {
                        result.freeze(trg.name)
                    }
                }
                Action.UNFREEZE -> frozen.unfreezeOfflineAll(invoker, result)
                Action.TOGGLE -> for (trg in Server_getOfflineUsers()) {
                    if (frozen.toggleOffline(invoker, trg.uniqueId, trg.name)) {
                        result.freeze(trg.name)
                    } else {
                        result.unfreeze(trg.name)
                    }
                }
            }
            Group.ONLINE -> when (action) {
                Action.FREEZE -> for (trg in Server_getPlayers()) {
                    if (frozen.freezeOnline(invoker, trg, true)) {
                        result.freeze(trg.name)
                    }
                }
                Action.UNFREEZE -> frozen.unfreezeOnlineAll(invoker, result)
                Action.TOGGLE -> for (trg in Server_getPlayers()) {
                    if (frozen.toggleOnline(invoker, trg)) {
                        result.freeze(trg.name)
                    } else {
                        result.unfreeze(trg.name)
                    }
                }
            }
            Group.ALL -> when (action) {
                Action.FREEZE -> for (trg in Server_getUsers()) {
                    val player = trg.player.getOrNull()
                    if (player != null) {
                        if (frozen.freezeOnline(invoker, player, true)) {
                            result.freeze(trg.name)
                        }
                    } else {
                        if (frozen.freezeOffline(invoker, trg.uniqueId, trg.name)) {
                            result.freeze(trg.name)
                        }
                    }
                }
                Action.UNFREEZE -> for (trg in Server_getUsers()) {
                    val player = trg.player.getOrNull()
                    if (player != null) {
                        if (frozen.unfreezeOnline(invoker, player)) {
                            result.unfreeze(trg.name)
                        }
                    } else {
                        if (frozen.unfreezeOffline(invoker, trg.uniqueId, trg.name)) {
                            result.unfreeze(trg.name)
                        }
                    }
                }
                Action.TOGGLE -> for (trg in Server_getUsers()) {
                    val player = trg.player.getOrNull()
                    if (player != null) {
                        if (frozen.toggleOnline(invoker, player)) {
                            result.freeze(trg.name)
                        } else {
                            result.unfreeze(trg.name)
                        }
                    } else {
                        if (frozen.toggleOffline(invoker, trg.uniqueId, trg.name)) {
                            result.freeze(trg.name)
                        } else {
                            result.unfreeze(trg.name)
                        }
                    }
                }
            }
        }
        return result
    }


    //////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////plugin/API////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////

    operator fun invoke(invoker: PluginContainer, action: Action, trg: Player): Result {
        val result = Result()
        when (action) {
            Action.FREEZE -> if (frozen.freezeOnline(invoker, trg)) {
                result.freeze(trg.name)
            }
            Action.UNFREEZE -> if (frozen.unfreezeOnline(invoker, trg)) {
                result.unfreeze(trg.name)
            }
            Action.TOGGLE -> if (frozen.toggleOnline(invoker, trg)) {
                result.freeze(trg.name)
            } else {
                result.unfreeze(trg.name)
            }
        }
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
                    for (trg in Server_getOfflineUsers()) {
                        if (frozen.freezeOffline(invoker, loc, trg.uniqueId, trg.name)) {
                            result.freeze(trg.name)
                        }
                    }
                }
                Action.UNFREEZE -> frozen.unfreezeOfflineAll(invoker, result)
                Action.TOGGLE -> {
                    if (loc == null) {
                        throw NullPointerException("location can not be null then using Action.TOGGLE ang Group.OFFLINE")
                    }
                    for (trg in Server_getOfflineUsers()) {
                        if (frozen.toggleOffline(invoker, loc, trg.uniqueId, trg.name)) {
                            result.freeze(trg.name)
                        } else {
                            result.unfreeze(trg.name)
                        }
                    }
                }
            }
            Group.ONLINE -> when (action) {
                Action.FREEZE -> for (trg in Server_getPlayers()) {
                    if (frozen.freezeOnline(invoker, trg)) {
                        result.freeze(trg.name)
                    }
                }
                Action.UNFREEZE -> frozen.unfreezeOnlineAll(invoker, result)
                Action.TOGGLE -> for (trg in Server_getPlayers()) {
                    if (frozen.toggleOnline(invoker, trg)) {
                        result.freeze(trg.name)
                    } else {
                        result.unfreeze(trg.name)
                    }
                }
            }
            Group.ALL -> when (action) {
                Action.FREEZE -> {
                    if (loc == null) {
                        throw NullPointerException("location can not be null then using Action.FREEZE ang Group.ALL")
                    }
                    for (trg in Server_getUsers()) {
                        val player = trg.player.getOrNull()
                        if (player != null) {
                            if (frozen.freezeOnline(invoker, player)) {
                                result.freeze(trg.name)
                            }
                        } else {
                            if (frozen.freezeOffline(invoker, loc, trg.uniqueId, trg.name)) {
                                result.freeze(trg.name)
                            }
                        }
                    }
                }
                Action.UNFREEZE -> for (trg in Server_getUsers()) {
                    val player = trg.player.getOrNull()
                    if (player != null) {
                        if (frozen.unfreezeOnline(invoker, player)) {
                            result.unfreeze(trg.name)
                        }
                    } else {
                        if (frozen.unfreezeOffline(invoker, trg.uniqueId, trg.name)) {
                            result.unfreeze(trg.name)
                        }
                    }
                }
                Action.TOGGLE -> {
                    if (loc == null) {
                        throw NullPointerException("location can not be null then using Action.TOGGLE ang Group.ALL")
                    }
                    for (trg in Server_getUsers()) {
                        val player = trg.player.getOrNull()
                        if (player != null) {
                            if (frozen.toggleOnline(invoker, player)) {
                                result.freeze(trg.name)
                            } else {
                                result.unfreeze(trg.name)
                            }
                        } else {
                            if (frozen.toggleOffline(invoker, loc, trg.uniqueId, trg.name)) {
                                result.freeze(trg.name)
                            } else {
                                result.unfreeze(trg.name)
                            }
                        }
                    }
                }
            }
        }
        return result
    }
    
    
    
}