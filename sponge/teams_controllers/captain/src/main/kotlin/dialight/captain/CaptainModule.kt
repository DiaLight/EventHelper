package dialight.captain

import dialight.extensions.Server_getPlayers
import dialight.modulelib.AbstractModule
import dialight.teams.Server_getScoreboard
import org.spongepowered.api.Sponge
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.scheduler.Task
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.channel.MessageReceiver
import java.util.*


class CaptainModule(val plugin: CaptainPlugin) : AbstractModule(CaptainModule.ID, "Captain") {

    companion object {
        val ID = "captain"
    }

    override val enabled: Boolean get() { return plugin.system.running }

    override fun doEnable(invoker: MessageReceiver): Boolean {
        val sys = plugin.system
        if(sys.running) return false
        val players = LinkedList(Server_getPlayers())
        val teams = Server_getScoreboard().teams
        if(players.size <= teams.size) return false
        val rnd = Random(Calendar.getInstance().timeInMillis)
        for (team in teams) {
            val player = players.removeAt(rnd.nextInt(players.size))
            sys.addCaptain(player, team)
        }
        for (player in players) {
            sys.addMember(player)
        }
        (invoker as? Player)?.let { player -> sys.setArenaLocation(player.location) }
        return sys.start()
    }

    override fun doDisable(invoker: MessageReceiver): Boolean {
        val sys = plugin.system
        if(!sys.running) return false
        Task.builder().execute { task -> sys.finish() }.submit(plugin)
        return true
    }

}