package dialight.captain.system.members

import dialight.captain.CaptainPlugin
import dialight.extensions.location
import org.spongepowered.api.Sponge
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.entity.living.player.User
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World

class SavedState(
    val loc: Location<World>? = null
) {

    fun restore(plugin: CaptainPlugin, user: CSUser) {
        loc?.let {
            plugin.teleporter.teleport(plugin.container, user.uuid, it)
        }
    }

    constructor(player: Player) : this(player.location)
    constructor(user: User) : this(user.location)

}