package dialight.user.shadow

import com.flowpowered.math.vector.Vector3d
import dialight.extensions.Server_getPlayer
import dialight.extensions.Server_getUser
import org.spongepowered.api.Sponge
import org.spongepowered.api.entity.Entity
import org.spongepowered.api.entity.EntityTypes
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.entity.living.player.User
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World
import java.util.*

class OfflineShadow(
    val uuid: UUID,
    val name: String
) {

    companion object {
        val TYPE = EntityTypes.VILLAGER
    }

    fun resolveLocation(): Location<World> {
        val world: World
        val position: Vector3d
        val player = Server_getPlayer(uuid)
        if(player != null) {
            world = player.world
            position = player.position
            return Location(world, position)
        }
        val user = Server_getUser(uuid)
        val srv = Sponge.getServer()
        if(user != null) {
            world = srv.getWorld(user.worldUniqueId.get()).get()
            position = user.position
            return Location(world, position)
        }
        return srv.getWorld(srv.defaultWorldName).get().spawnLocation
    }

    var entity: Entity? = null

    constructor(user: User) : this(user.uniqueId, user.name)


}