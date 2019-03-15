package dialight.freezer

import com.flowpowered.math.vector.Vector3d
import dialight.extensions.Server_getPlayer
import dialight.extensions.getOrNull
import org.spongepowered.api.Sponge
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.entity.living.player.User
import org.spongepowered.api.plugin.PluginContainer
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World
import java.util.*

class FrozenPlayers {

    companion object {
        fun alignLocation(loc: Location<World>) = Location(
            loc.extent,
            loc.blockX.toDouble() + 0.5,
            loc.blockY.toDouble(),
            loc.blockZ.toDouble() + 0.5
        )
    }

    class Frozen(
        val uuid: UUID,
        val name: String,
        location: Location<World>,
        val byPlugin: Boolean = false) {

        companion object {
            fun of(trg: User, byPlugin: Boolean = false): Frozen? {
                val worldId = trg.worldUniqueId.getOrNull() ?: return null
                val world = Sponge.getServer().getWorld(worldId).getOrNull() ?: return null
                return Frozen(trg, Location(world, trg.position), byPlugin)
            }
        }

        var location: Location<World> = alignLocation(location)
            set(value) { field = alignLocation(value) }

        private var velocity: Vector3d? = null

        constructor(
            trg: Player, location: Location<World> = trg.location, byPlugin: Boolean = false
        ) : this(trg.uniqueId, trg.name, location, byPlugin) {
            this.velocity = trg.velocity
        }

        constructor(
            trg: User, location: Location<World>, byPlugin: Boolean = false
        ) : this(trg.uniqueId, trg.name, location, byPlugin)

        fun updateVelocity(velocity: Vector3d?) {
            if (velocity == null) return
            this.velocity = velocity
        }

        fun updateAll(trg: Player) {
            this.updateVelocity(trg.velocity)
        }

        override fun toString(): String {
            return this.name
        }

        fun recoverVelocity(trg: Player) {
            if (velocity == null) return
            trg.velocity = velocity!!
        }

    }

    val map: MutableMap<UUID, Frozen> = HashMap()

    fun freezeOnline(invoker: Player, trg: Player, result: Freezer.Result): Boolean {
        if (this.map.containsKey(trg.uniqueId)) return false
        val frozen = Frozen(trg)
        result.freeze(frozen)
        this.map[trg.uniqueId] = frozen
        FreezerMessages.freeze(invoker, trg)
        return true
    }

    fun unfreezeOnline(invoker: Player, trg: Player, result: Freezer.Result): Boolean {
        val frozen = this.map.remove(trg.uniqueId) ?: return false
        result.unfreeze(frozen)
        FreezerMessages.unfreeze(invoker, trg)
        return true
    }

    fun toggleOnline(invoker: Player, trg: Player, result: Freezer.Result): Boolean {
        var frozen: Frozen? = this.map.remove(trg.uniqueId)
        if (frozen == null) {
            frozen = Frozen(trg)
            result.freeze(frozen)
            this.map[trg.uniqueId] = frozen
            FreezerMessages.freeze(invoker, trg)
            return true
        } else {
            result.unfreeze(frozen)
            FreezerMessages.unfreeze(invoker, trg)
            return false
        }
    }

    fun freezeOffline(invoker: Player, trg: UUID, name: String, result: Freezer.Result): Boolean {
        if (this.map.containsKey(trg)) return false
        val frozen = Frozen(trg, name, invoker.location)
        result.freeze(frozen)
        this.map[trg] = frozen
        FreezerMessages.freeze(invoker, frozen)
        return true
    }

    fun unfreezeOffline(invoker: Player, trg: UUID, name: String, result: Freezer.Result): Boolean {
        val frozen = this.map.remove(trg) ?: return false
        result.unfreeze(frozen)
        FreezerMessages.unfreeze(invoker, frozen)
        return true
    }

    fun toggleOffline(invoker: Player, trg: UUID, name: String, result: Freezer.Result): Boolean {
        var frozen: Frozen? = this.map.remove(trg)
        if (frozen == null) {
            frozen = Frozen(trg, name, invoker.location)
            this.map[trg] = frozen
            result.freeze(frozen)
            FreezerMessages.freeze(invoker, frozen)
            return true
        } else {
            result.unfreeze(frozen)
            FreezerMessages.unfreeze(invoker, frozen)
            return false
        }
    }

    fun unfreezeOnlineAll(invoker: Player, result: Freezer.Result) {
        for (frozen in this.map.values) {
            val trg = Server_getPlayer(frozen.uuid)
            if (trg != null) {
                FreezerMessages.unfreeze(invoker, trg)
                result.unfreeze(frozen)
            }
        }
        this.map.clear()
    }

    fun unfreezeOfflineAll(invoker: Player, result: Freezer.Result) {
        for (frozen in this.map.values) {
            FreezerMessages.unfreeze(invoker, frozen)
            result.unfreeze(frozen)
        }
        this.map.clear()
    }


    @Deprecated("Use event API")
    open protected fun onFreezeOnline(frozen: Frozen, trg: Player) {}


    @Deprecated("Use event API")
    open protected fun onUnfreezeOnline(frozen: Frozen, trg: Player) {}


    @Deprecated("Use event API")
    open protected fun onFreezeOffline(frozen: Frozen) {}


    @Deprecated("Use event API")
    open protected fun onUnfreezeOffline(frozen: Frozen) {}

    //////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////Module/invoker/////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////

    fun freezeOnline(invoker: PluginContainer, trg: Player, result: Freezer.Result): Boolean {
        if (this.map.containsKey(trg.uniqueId)) return false
        val frozen = Frozen(trg, byPlugin = true)
        result.freeze(frozen)
        this.map[trg.uniqueId] = frozen
        FreezerMessages.freeze(invoker, trg)
        return true
    }

    fun unfreezeOnline(invoker: PluginContainer, trg: Player, result: Freezer.Result): Boolean {
        val frozen = this.map.remove(trg.uniqueId) ?: return false
        result.unfreeze(frozen)
        FreezerMessages.unfreeze(invoker, trg)
        return true
    }

    fun toggleOnline(invoker: PluginContainer, trg: Player, result: Freezer.Result): Boolean {
        var frozen: Frozen? = this.map.remove(trg.uniqueId)
        if (frozen == null) {
            frozen = Frozen(trg, byPlugin = true)
            result.freeze(frozen)
            this.map[trg.uniqueId] = frozen
            FreezerMessages.freeze(invoker, trg)
            return true
        } else {
            result.unfreeze(frozen)
            FreezerMessages.unfreeze(invoker, trg)
            return false
        }
    }

    fun freezeOffline(invoker: PluginContainer, loc: Location<World>, trg: UUID, name: String, result: Freezer.Result): Boolean {
        if (this.map.containsKey(trg)) return false
        val frozen = Frozen(trg, name, loc)
        result.freeze(frozen)
        this.map[trg] = frozen
        FreezerMessages.freeze(invoker, frozen)
        return true
    }

    fun unfreezeOffline(invoker: PluginContainer, trg: UUID, name: String, result: Freezer.Result): Boolean {
        val frozen = this.map.remove(trg) ?: return false
        result.unfreeze(frozen)
        FreezerMessages.unfreeze(invoker, frozen)
        return true
    }

    fun toggleOffline(invoker: PluginContainer, loc: Location<World>, trg: UUID, name: String, result: Freezer.Result): Boolean {
        var frozen: Frozen? = this.map.remove(trg)
        if (frozen == null) {
            frozen = Frozen(trg, name, loc)
            result.freeze(frozen)
            this.map[trg] = frozen
            FreezerMessages.freeze(invoker, frozen)
            return true
        } else {
            result.unfreeze(frozen)
            FreezerMessages.unfreeze(invoker, frozen)
            return false
        }
    }

    fun unfreezeOnlineAll(invoker: PluginContainer, result: Freezer.Result) {
        for (frozen in this.map.values) {
            val trg = Server_getPlayer(frozen.uuid)
            if (trg != null) {
                FreezerMessages.unfreeze(invoker, trg)
                result.unfreeze(frozen)
            }
        }
        this.map.clear()
    }

    fun unfreezeOfflineAll(invoker: PluginContainer, result: Freezer.Result) {
        for (frozen in this.map.values) {
            this.onUnfreezeOffline(frozen)
            FreezerMessages.unfreeze(invoker, frozen)
            result.unfreeze(frozen)
        }
        this.map.clear()
    }

}