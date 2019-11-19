package dialight.teams.mixin

import dialight.teams.mixin.interfaces.IMixinScoreboard
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.scoreboard.Scoreboard
import org.spongepowered.api.scoreboard.Team
import org.spongepowered.api.text.Text
import org.spongepowered.common.text.SpongeTexts

//import org.spongepowered.common.interfaces.IMixinScoreboard
//import org.spongepowered.common.interfaces.IMixinTeam
import org.spongepowered.common.mixin.core.scoreboard.MixinScorePlayerTeam

fun Scoreboard.getMemberTeam(member: String): Team? {
    this as IMixinScoreboard
    return this.getPlayersTeam(member) as? Team
}
fun Scoreboard.getPlayersTeam(player: Player) = getMemberTeam(player.name)
fun Scoreboard.getMemberTeam(member: Text) = getMemberTeam(SpongeTexts.toLegacy(member))

fun Scoreboard.removeMemberFromTeams(member: String): Boolean {
    this as IMixinScoreboard
    return this.removePlayerFromTeams(member)
}
fun Scoreboard.removePlayerFromTeams(player: Player) = removeMemberFromTeams(player.name)
fun Scoreboard.removeMemberFromTeams(member: Text) = removeMemberFromTeams(SpongeTexts.toLegacy(member))

