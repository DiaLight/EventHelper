package dialight.captain.system

import dialight.captain.CaptainPlugin
import dialight.captain.system.members.Captain
import dialight.captain.system.members.Member
import dialight.captain.system.members.CSUsers
import dialight.extensions.Server_getPlayer
import org.spongepowered.api.boss.BossBarColors
import org.spongepowered.api.boss.BossBarOverlays
import org.spongepowered.api.boss.ServerBossBar
import org.spongepowered.api.text.Text
import java.util.*

class SelectPlayerSession(
    val plugin: CaptainPlugin,
    val members: CSUsers
) {

    private val bar = ServerBossBar.builder()
        .name(Text.of())
        .color(BossBarColors.WHITE)
        .playEndBossMusic(false)
        .percent(1.0f)
        .overlay(BossBarOverlays.PROGRESS)
        .build()

    private val timer = RepeatableTimerTask(plugin, "captain")
    private val selectListeners = ArrayList<(Captain, Member?) -> Unit>()

    var captain = Captain.EMPTY
        private set

    private var selected: Member? = null
    private var interrupted = false

    init {
        members.onAdd {
            Server_getPlayer(it.uuid)?.let { player ->
                bar.addPlayer(player)
            }
        }
        members.onRemove {
            Server_getPlayer(it.uuid)?.let { player ->
                bar.removePlayer(player)
            }
        }
        timer.onTick(this::tick)
        timer.onFinish(this::finish)
    }

    private fun tick(index: Int, limit: Int) {
        if(this.interrupted) return
        bar.percent = (limit - index).toFloat() / limit.toFloat()
    }

    private fun finish() {
        if(this.interrupted) return
        for(op in selectListeners) op(captain, selected)
    }

    fun onFinishSession(op: (Captain, Member?) -> Unit) { selectListeners += op }

    fun start(captain: Captain, seconds: Int) {
        this.interrupted = false
        timer.start(seconds)
        this.selected = null
        this.captain = captain
    }

    fun interrupt() {
        this.interrupted = true
        this.captain = Captain.EMPTY
        timer.stop()
    }

    fun select(member: Member) {
        this.selected = member
        timer.stop()
    }

}