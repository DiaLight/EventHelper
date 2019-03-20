package dialight.teams.gui

import dialight.extensions.Server_getPlayer
import dialight.extensions.set
import dialight.guilib.snapshot.SnapshotGui
import dialight.teams.TeamsPlugin
import dialight.teams.event.ScoreboardEvent
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.scheduler.Task

class TeamsGui(val plugin: TeamsPlugin) : SnapshotGui<TeamsSnapshot>() {

    private val snap = TeamsSnapshot(plugin, id)

    override fun createSnapshot(player: Player) = snap

    @Listener
    fun onTeamAdd(e: ScoreboardEvent.Team.Add) {
        snap.updateItems()
//        Task.builder().execute { task ->
//            for ((uuid, snap) in opened) {
//                val player = Server_getPlayer(uuid) ?: continue
//                val current = this.snap.current(uuid)
//                plugin.guilib!!.openView(player, current)
//            }
//        }.delayTicks(1).submit(plugin)
    }
    @Listener
    fun onTeamRemove(e: ScoreboardEvent.Team.Remove) {
        snap.updateItems()
//        Task.builder().execute { task ->
//            for ((uuid, snap) in opened) {
//                val player = Server_getPlayer(uuid) ?: continue
//                val current = this.snap.current(uuid)
//                plugin.guilib!!.openView(player, current)
//            }
//        }.delayTicks(1).submit(plugin)
    }
    @Listener
    fun onTeamUpdate(e: ScoreboardEvent.Team.UpdateInfo) {

    }
    @Listener
    fun onTeamMemberAdd(e: ScoreboardEvent.TeamMember.Add) {

    }
    @Listener
    fun onTeamMemberRemove(e: ScoreboardEvent.TeamMember.Remove) {

    }

}
