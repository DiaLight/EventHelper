package dialight.teams.gui

import dialight.extensions.Server_getPlayer
import dialight.extensions.set
import dialight.guilib.snapshot.SnapshotGui
import dialight.teams.TeamsPlugin
import dialight.teams.event.ScoreboardEvent
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.scheduler.Task
import java.util.*

class TeamsGui(val plugin: TeamsPlugin) : SnapshotGui<TeamsSnapshot>() {

    override fun getSnapshot(player: Player) = TeamsSnapshot(plugin, player.uniqueId, id)

    init {
        plugin.selected.apply {
            onPut{ uuid, v ->
                Task.builder().execute { task ->
                    updateItems(uuid)
                }.submit(plugin)
            }
            onRemove { uuid, v ->
                Task.builder().execute { task ->
                    updateItems(uuid)
                }.submit(plugin)
            }
            onReplace { key, old, new ->

            }
        }
    }

    fun updateItems(uuid: UUID) {
        val snap = opened[uuid] ?: return
        snap.updateItems()
        val inv = plugin.guilib!!.guimap[uuid] ?: return
        val current = snap.current(uuid)
        var index = 0
        for(item in current) {
            inv[index] = item?.item
            index++
        }
    }

    @Listener
    fun onTeamAdd(e: ScoreboardEvent.Team.Add) {
        for((uuid, snap) in opened) {
            snap.updateItems()
        }
    }
    @Listener
    fun onTeamRemove(e: ScoreboardEvent.Team.Remove) {
        Task.builder().execute { task ->
            for ((uuid, snap) in opened) {
                val inv = plugin.guilib!!.guimap[uuid] ?: continue
                val current = snap.current(uuid)
                var index = 0
                for(item in current) {
                    inv[index] = item?.item
                    index++
                }
            }
        }.submit(plugin)
        for((uuid, snap) in opened) {
            snap.updateItems()
        }
    }
    @Listener
    fun onTeamUpdate(e: ScoreboardEvent.Team.UpdateInfo) {
        for((uuid, snap) in opened) {
            snap.updateItems()
        }
    }
    @Listener
    fun onTeamMemberAdd(e: ScoreboardEvent.TeamMember.Add) {
//        for((uuid, snap) in opened) {
//            snap.updateItems()
//        }
    }
    @Listener
    fun onTeamMemberRemove(e: ScoreboardEvent.TeamMember.Remove) {
//        for((uuid, snap) in opened) {
//            snap.updateItems()
//        }
    }

}
