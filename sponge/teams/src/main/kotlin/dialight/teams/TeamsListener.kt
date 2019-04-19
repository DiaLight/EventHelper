package dialight.teams

import dialight.extensions.getOrNull
import dialight.teams.event.ScoreboardEvent
import dialight.teleporter.event.TeleporterEvent
import org.spongepowered.api.Sponge
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.command.SendCommandEvent
import org.spongepowered.api.event.filter.cause.First
import org.spongepowered.api.text.Text

class TeamsListener(val plugin: TeamsPlugin) {

    @Listener
    fun onTeleporterAction(e: TeleporterEvent, @First player: Player) {
        val team = plugin.selected[player.uniqueId] ?: return
        for(sel in e.result.selected) {
            team.addMember(Text.of(sel.name))
            player.sendMessage(TeamsMessages.addTeamPlayer(team, sel.name))
        }
        for(unsel in e.result.unselected) {
            team.removeMember(Text.of(unsel.name))
            player.sendMessage(TeamsMessages.removeTeamPlayer(team, unsel.name))
        }
    }

}