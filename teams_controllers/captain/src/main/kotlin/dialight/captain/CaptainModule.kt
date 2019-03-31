package dialight.captain

import dialight.captain.system.CaptainSystem
import dialight.extensions.Server_getPlayers
import dialight.modulelib.AbstractModule
import org.spongepowered.api.Sponge
import kotlin.streams.toList

class CaptainModule(val plugin: CaptainPlugin) : AbstractModule(CaptainModule.ID, "Captain") {

    companion object {
        val ID = "captain"
    }

    override fun onEnable(): Boolean {
        val sys = plugin.system
        sys.interrupt()
        sys.teams.clear()
        sys.teams.addAll(
            Sponge.getServer().serverScoreboard.get().teams.stream()
                .map { CaptainSystem.CSTeam(it.name) }
                .toList()
        )
        sys.members.clear()
        sys.members.putAll(
            Server_getPlayers().stream()
                .map { Pair(it.uniqueId, CaptainSystem.Member(it.uniqueId, it.name)) }
                .toList()
        )
        sys.start()
        return true
    }

    override fun onDisable(): Boolean {
        plugin.system.interrupt()
        return true
    }

}