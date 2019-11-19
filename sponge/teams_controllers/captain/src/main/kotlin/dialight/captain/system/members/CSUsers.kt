package dialight.captain.system.members

import dialight.captain.CaptainPlugin
import dialight.extensions.Server_getPlayer
import dialight.extensions.Server_getPlayers
import dialight.freezer.Freezer
import dialight.observable.map.ObservableMapWrapper
import org.spongepowered.api.Sponge
import org.spongepowered.api.entity.Entity
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.entity.living.player.User
import org.spongepowered.api.scoreboard.Team
import org.spongepowered.api.text.Text
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World
import java.lang.Exception
import java.util.*
import java.util.stream.Stream
import kotlin.streams.toList

class CSUsers(
    val plugin: CaptainPlugin
) : Iterable<CSUser> {

    private val users = ObservableMapWrapper<UUID, CSUser>(HashMap())
    private val unselected = LinkedList<Member>()
    private val captains = ObservableMapWrapper<String, Captain>(HashMap())

    private val rnd = Random()

    override fun iterator(): Iterator<CSUser> = users.values.iterator()

    fun onAdd(op: (CSUser) -> Unit) = users.onPut { uuid, csUser -> op(csUser) }
    fun onRemove(op: (CSUser) -> Unit) = users.onRemove { uuid, csUser -> op(csUser) }

    fun onAddCaptain(op: (Captain) -> Unit) = captains.onPut { name, cap -> op(cap) }
    fun onRemoveCaptain(op: (Captain) -> Unit) = captains.onRemove { uuid, cap -> op(cap) }

    private fun add(member: Member) {
        if(users.putIfAbsent(member.uuid, member) == null) {
            unselected.add(member)
        }
    }
    fun add(user: User) = add(Member(user))
    fun add(player: Player) = add(Member(player))

    private fun addCaptain(captain: Captain, team: Team) {
        if(users.putIfAbsent(captain.uuid, captain) == null) {
            captains[team.name] = captain
        }
    }
    fun addCaptain(user: User, team: Team) = addCaptain(Captain(user, team), team)
    fun addCaptain(player: Player, team: Team) = addCaptain(Captain(player, team), team)

    fun getUnselected(): Collection<Member> = unselected
    fun getUnselectedEntities(): Stream<Entity> {
        return unselected.stream().map {
            val player = it.player
            if(player != null) return@map player
            val shadow = plugin.offlinelib.shadow.getShadow(it.uuid)
            shadow?.entity
        }.filter { it != null }.map { it!! }
    }
    fun hasUnselected(): Boolean = !unselected.isEmpty()
    fun chooseRandom(): Member {
        if(unselected.isEmpty()) throw Exception("There is no players to random select")
        return unselected.elementAt(rnd.nextInt(unselected.size))
    }
    fun getCaptains(): Collection<Captain> = captains.values

    fun getUnselected(player: Player) = getUnselected(player.uniqueId)
    fun getUnselected(uuid: UUID): Member? {
        val member = users[uuid] ?: return null
        if (member !is Member) return null
        if(member.selected) return null
        return member
    }

    fun getCaptain(player: Player): Captain? {
        val captain = users[player.uniqueId] ?: return null
        return captain as? Captain
    }

    fun select(captain: Captain, member: Member) {
        if(!unselected.remove(member)) throw Exception("Can't select unknown user")
        captain.members[member.uuid] = member
        captain.team?.addMember(Text.of(member.name))
        member.selected = true
    }

    fun clear() {
        for(user in this) {
            user.saved.restore(plugin, user)
        }
        users.clear()
        unselected.clear()
        captains.clear()
    }

    fun broadcastMessage(msg: Text) {
        for(user in this) {
            Server_getPlayer(user.uuid)?.sendMessage(msg)
        }
    }

    fun resolveArenaLocation(): Location<World> {
        for(user in this) {
            val loc = user.resolveLocation()
            return loc
        }
        val srv = Sponge.getServer()
        val defWorld = srv.getWorld(srv.defaultWorldName).get()
        return defWorld.spawnLocation
    }

}