package dialight.captain.system.members

import dialight.extensions.dyeColor
import dialight.extensions.getOrNull
import org.spongepowered.api.Sponge
import org.spongepowered.api.data.type.DyeColor
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.entity.living.player.User
import org.spongepowered.api.scoreboard.Team
import org.spongepowered.api.text.format.TextColor
import org.spongepowered.api.text.format.TextColors
import java.util.*


class Captain(
    uuid: UUID, name: String, saved: SavedState,
    val teamName: String, val textColor: TextColor,
    val members: HashMap<UUID, Member> = hashMapOf()
) : CSUser(uuid, name, saved) {

    val team: Team?
        get() {
            val sb = Sponge.getServer().serverScoreboard.get()
            return sb.getTeam(teamName).getOrNull()
        }
    val dyeColor: DyeColor
        get() = textColor.dyeColor

    constructor(user: User, team: Team) : this(user.uniqueId, user.name, SavedState(user), team.name, team.color)
    constructor(player: Player, team: Team) : this(player.uniqueId, player.name, SavedState(player), team.name, team.color)

    companion object {
        val EMPTY = Captain(UUID.randomUUID(), "", SavedState(), "", TextColors.NONE)
    }
}
