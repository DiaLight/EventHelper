package dialight.captain.system.members

import dialight.extensions.Server_getPlayer
import dialight.extensions.Server_getUser
import dialight.extensions.location
import org.spongepowered.api.Sponge
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.entity.living.player.User
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World
import java.util.*

abstract class CSUser(val uuid: UUID, val name: String, val saved: SavedState) {

    lateinit var cs_location: Location<World>

    val player: Player?
        get() = Server_getPlayer(uuid)

    val user: User?
        get() = Server_getUser(uuid)

    fun resolveLocation(): Location<World> {
        val player = Server_getPlayer(uuid)
        if(player != null) return player.location
        val user = Server_getUser(uuid)
        if(user != null) {
            val loc = user.location
            if(loc != null) return loc
        }
        val srv = Sponge.getServer()
        val defWorld = srv.getWorld(srv.defaultWorldName).get()
        return defWorld.spawnLocation
    }

    override fun hashCode() = name.hashCode()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return uuid == (other as CSUser).uuid
    }

}