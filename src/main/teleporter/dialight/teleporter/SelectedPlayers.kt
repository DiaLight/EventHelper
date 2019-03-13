package dialight.teleporter

import dialight.extensions.Server_getUser
import org.spongepowered.api.Sponge
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.entity.living.player.User
import java.util.*
import java.util.stream.Collectors

class SelectedPlayers {

    val selected = HashMap<UUID, String>()

    fun toggletagOffline(trg: UUID, name: String): Boolean {
        if (selected.remove(trg) != null) {
            return false
        }
        selected[trg] = name
        return true
    }

    fun tagOffline(trg: UUID, name: String): Boolean = selected.put(trg, name) == null
    fun untagOffline(trg: UUID, name: String): Boolean = selected.remove(trg) != null
    fun getAll(): Collection<UUID> = selected.keys
    fun getAllNames(): Collection<String> = selected.values


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

    fun forEach(action: (UUID, String) -> Unit) = selected.forEach(action)


}