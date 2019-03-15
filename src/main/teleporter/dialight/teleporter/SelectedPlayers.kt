package dialight.teleporter

import dialight.extensions.Server_getUser
import org.spongepowered.api.Sponge
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.entity.living.player.User
import java.util.*
import java.util.stream.Collectors

class SelectedPlayers {

    val selected = HashMap<UUID, Teleporter.Selected>()

    fun toggletagOffline(sel: Teleporter.Selected): Boolean {
        if (selected.remove(sel.uuid) != null) {
            return false
        }
        selected[sel.uuid] = sel
        return true
    }

    fun tagOffline(sel: Teleporter.Selected): Boolean = selected.put(sel.uuid, sel) == null
    fun untagOffline(trg: UUID): Teleporter.Selected? = selected.remove(trg)
    fun untagOffline(sel: Teleporter.Selected): Boolean = selected.remove(sel.uuid) != null


    val offline: Collection<User>
        get() = selected.keys.stream()
            .map { Server_getUser(it) }
            .filter { it != null }
            .filter { !it!!.isOnline }
            .collect(
                Collectors.toCollection<User, ArrayList<User>> { ArrayList() }
            )

    val online: Collection<Player>
        get() {
            val srv = Sponge.getServer()
            return selected.keys.stream()
                .map { srv.getPlayer(it) }
                .filter { it.isPresent }
                .map { it.get() }
                .collect(Collectors.toCollection<Player, ArrayList<Player>> { ArrayList() })
        }

    fun forEach(action: (Teleporter.Selected) -> Unit) = selected.values.forEach(action)


}