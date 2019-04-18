package dialight.captain.system.members

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.entity.living.player.User
import org.spongepowered.api.scoreboard.Team
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World
import java.util.*


class Member(uuid: UUID, name: String, saved: SavedState = SavedState()) : CSUser(uuid, name, saved) {

    var selected = false

    constructor(user: User) : this(user.uniqueId, user.name, SavedState(user))
    constructor(player: Player) : this(player.uniqueId, player.name, SavedState(player))

}
