package dialight.freezer

import com.flowpowered.math.vector.Vector3d
import dialight.extensions.Server_getPlayer
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.plugin.PluginContainer
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World
import java.util.*
import java.util.function.BiConsumer

class FrozenPlayers {

    class Frozen(val uniqueId: UUID, loc: Location<World>, private val byPlugin: Boolean = false) {

        private var location: Location<World>? = null


        private var lastKnownName: String? = null
        private var velocity: Vector3d? = null

        val name: String
            get() = if (this.lastKnownName == null) this.uniqueId.toString() else this.lastKnownName!!

        init {
            this.setLocation(loc)
        }

        constructor(uniqueId: UUID, location: Location<World>, name: String) : this(uniqueId, location) {
            this.lastKnownName = name
        }

        @JvmOverloads
        constructor(trg: Player, location: Location<World> = trg.location, byPlugin: Boolean = false) : this(trg.uniqueId, location, byPlugin) {
            this.lastKnownName = trg.name
            this.velocity = trg.velocity
        }

        constructor(trg: Player, invoker: PluginContainer) : this(trg, byPlugin = true) {

        }

        fun setLocation(l: Location<World>) {
            this.location = Location(l.extent, l.blockX.toDouble(), l.blockY.toDouble(), l.blockZ.toDouble())
        }

        fun updateName(name: String?) {
            if (name == null) return
            this.lastKnownName = name
        }

        fun updateVelocity(velocity: Vector3d?) {
            if (velocity == null) return
            this.velocity = velocity
        }

        fun updateAll(trg: Player) {
            this.updateName(trg.name)
            this.updateVelocity(trg.velocity)
        }

        fun getLocation(): Location<World> {
            return location!!.copy()
        }

        fun byPlugin(): Boolean {
            return byPlugin
        }

        override fun toString(): String {
            return this.name
        }

        fun recoverVelocity(trg: Player) {
            if (velocity == null) return
            trg.velocity = velocity!!
        }

    }

    val offlineFrozen: MutableMap<UUID, Frozen> = HashMap()
    val idFrozen: MutableMap<UUID, Frozen> = HashMap()

    @Deprecated("suppressMessages argument is deprected")
    fun freezeOnline(invoker: Player, trg: Player, suppressMessages: Boolean): Boolean {
        if (!this.idFrozen.containsKey(trg.uniqueId)) {
            val frozen = Frozen(trg)
            onFreezeOnline(frozen, trg)
            this.idFrozen[trg.uniqueId] = frozen
            FreezerMessages.freeze(invoker, trg)
            return true
        }
        return false
    }

    fun unfreezeOnline(invoker: Player, trg: Player): Boolean {
        val frozen = this.idFrozen.remove(trg.uniqueId)
        if (frozen != null) {
            onUnfreezeOnline(frozen, trg)
            FreezerMessages.unfreeze(invoker, trg)
            return true
        }
        return false
    }

    fun toggleOnline(invoker: Player, trg: Player): Boolean {
        var frozen: Frozen? = this.idFrozen.remove(trg.uniqueId)
        if (frozen == null) {
            frozen = Frozen(trg)
            onFreezeOnline(frozen, trg)
            this.idFrozen[trg.uniqueId] = frozen
            FreezerMessages.freeze(invoker, trg)
            return true
        } else {
            onUnfreezeOnline(frozen, trg)
            FreezerMessages.unfreeze(invoker, trg)
            return false
        }
    }

    fun freezeOffline(invoker: Player, trg: UUID, name: String): Boolean {
        if (!this.offlineFrozen.containsKey(trg)) {
            val frozen = Frozen(trg, invoker.location, name)
            this.onFreezeOffline(frozen)
            this.offlineFrozen[trg] = frozen
            FreezerMessages.freeze(invoker, frozen)
            return true
        }
        return false
    }

    fun unfreezeOffline(invoker: Player, trg: UUID, name: String): Boolean {
        val frozen = this.offlineFrozen.remove(trg)
        if (frozen != null) {
            frozen.updateName(name)
            this.onUnfreezeOffline(frozen)
            FreezerMessages.unfreeze(invoker, frozen)
            return true
        }
        return false
    }

    fun toggleOffline(invoker: Player, trg: UUID, name: String): Boolean {
        var frozen: Frozen? = this.offlineFrozen.remove(trg)
        if (frozen == null) {
            frozen = Frozen(trg, invoker.location, name)
            this.onFreezeOffline(frozen)
            this.offlineFrozen[trg] = frozen
            FreezerMessages.freeze(invoker, frozen)
            return true
        } else {
            frozen.updateName(name)
            this.onUnfreezeOffline(frozen)
            FreezerMessages.unfreeze(invoker, frozen)
            return false
        }
    }

    fun unfreezeOnlineAll(invoker: Player, result: Freezer.Result) {
        for (frozen in this.idFrozen.values) {
            val trg = Server_getPlayer(frozen.uniqueId)
            if (trg != null) {
                onUnfreezeOnline(frozen, trg)
                FreezerMessages.unfreeze(invoker, trg)
                result.unfreeze(frozen.name)
            }
        }
        this.idFrozen.clear()
    }

    fun unfreezeOfflineAll(invoker: Player, result: Freezer.Result) {
        for (frozen in this.offlineFrozen.values) {
            this.onUnfreezeOffline(frozen)
            FreezerMessages.unfreeze(invoker, frozen)
            result.unfreeze(frozen.name)
        }
        this.offlineFrozen.clear()
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

    fun freezeOnline(invoker: PluginContainer, trg: Player): Boolean {
        if (!this.idFrozen.containsKey(trg.uniqueId)) {
            val frozen = Frozen(trg, invoker)
            onFreezeOnline(frozen, trg)
            this.idFrozen[trg.uniqueId] = frozen
            FreezerMessages.freeze(invoker, trg)
            return true
        }
        return false
    }

    fun unfreezeOnline(invoker: PluginContainer, trg: Player): Boolean {
        val frozen = this.idFrozen.remove(trg.uniqueId)
        if (frozen != null) {
            onUnfreezeOnline(frozen, trg)
            FreezerMessages.unfreeze(invoker, trg)
            return true
        }
        return false
    }

    fun toggleOnline(invoker: PluginContainer, trg: Player): Boolean {
        var frozen: Frozen? = this.idFrozen.remove(trg.uniqueId)
        if (frozen == null) {
            frozen = Frozen(trg, invoker)
            onFreezeOnline(frozen, trg)
            this.idFrozen[trg.uniqueId] = frozen
            FreezerMessages.freeze(invoker, trg)
            return true
        } else {
            onUnfreezeOnline(frozen, trg)
            FreezerMessages.unfreeze(invoker, trg)
            return false
        }
    }

    fun freezeOffline(invoker: PluginContainer, loc: Location<World>, trg: UUID, name: String): Boolean {
        if (!this.offlineFrozen.containsKey(trg)) {
            val frozen = Frozen(trg, loc, name)
            this.onFreezeOffline(frozen)
            this.offlineFrozen[trg] = frozen
            FreezerMessages.freeze(invoker, frozen)
            return true
        }
        return false
    }

    fun unfreezeOffline(invoker: PluginContainer, trg: UUID, name: String): Boolean {
        val frozen = this.offlineFrozen.remove(trg)
        if (frozen != null) {
            frozen.updateName(name)
            this.onUnfreezeOffline(frozen)
            FreezerMessages.unfreeze(invoker, frozen)
            return true
        }
        return false
    }

    fun toggleOffline(invoker: PluginContainer, loc: Location<World>, trg: UUID, name: String): Boolean {
        var frozen: Frozen? = this.offlineFrozen.remove(trg)
        if (frozen == null) {
            frozen = Frozen(trg, loc, name)
            this.onFreezeOffline(frozen)
            this.offlineFrozen[trg] = frozen
            FreezerMessages.freeze(invoker, frozen)
            return true
        } else {
            frozen.updateName(name)
            this.onUnfreezeOffline(frozen)
            FreezerMessages.unfreeze(invoker, frozen)
            return false
        }
    }

    fun unfreezeOnlineAll(invoker: PluginContainer, result: Freezer.Result) {
        for (frozen in this.idFrozen.values) {
            val trg = Server_getPlayer(frozen.uniqueId)
            if (trg != null) {
                onUnfreezeOnline(frozen, trg)
                FreezerMessages.unfreeze(invoker, trg)
                result.unfreeze(frozen.name)
            }
        }
        this.idFrozen.clear()
    }

    fun unfreezeOfflineAll(invoker: PluginContainer, result: Freezer.Result) {
        for (frozen in this.offlineFrozen.values) {
            this.onUnfreezeOffline(frozen)
            FreezerMessages.unfreeze(invoker, frozen)
            result.unfreeze(frozen.name)
        }
        this.offlineFrozen.clear()
    }

}