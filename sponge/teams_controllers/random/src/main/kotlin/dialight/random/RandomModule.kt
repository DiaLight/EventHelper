package dialight.random

import dialight.extensions.Server_getPlayers
import dialight.extensions.getOrNull
import dialight.modulelib.ActionModule
import org.spongepowered.api.Sponge
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.channel.MessageReceiver
import java.util.*

class RandomModule(val plugin: RandomPlugin) : ActionModule(ID, "Random") {

    companion object {
        val ID = "random"
    }

    val rnd = Random(Calendar.getInstance().timeInMillis)

    override fun onAction(invoker: MessageReceiver): Boolean {
        val sb = Sponge.getServer().serverScoreboard.getOrNull() ?: return false
        if(sb.teams.isEmpty()) return false
        val online = LinkedList(Server_getPlayers())
        if(online.isEmpty()) return false
        val players_in_team = online.size / sb.teams.size
        for(team in sb.teams) {
            team.members.clear()
            for (i in (0..(players_in_team - 1))) {
                val player = online.removeAt(rnd.nextInt(online.size))
                team.addMember(Text.of(player.name))
            }
        }
        val teamsLeft = LinkedList(sb.teams)
        for(player in online) {
            val team = teamsLeft.removeAt(rnd.nextInt(teamsLeft.size))
            team.addMember(Text.of(player.name))
        }
//        if(!online.isEmpty()) throw Exception("Random teams controller is broken")
        return true
    }


}