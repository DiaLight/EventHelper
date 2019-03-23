package dialight.extensions

import org.spongepowered.api.Sponge
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.entity.living.player.User
import org.spongepowered.api.service.user.UserStorageService
import java.util.*
import kotlin.streams.toList

private fun Server_getUserStorage() = Sponge.getServiceManager().provide(UserStorageService::class.java).getOrNull()

fun Server_getUser(uuid: UUID): User? {
    val userStorage = Server_getUserStorage() ?: return null
    return userStorage.get(uuid).getOrNull()
}
fun Server_getUser(lastKnownName: String): User? {
    val userStorage = Server_getUserStorage() ?: return null
    return userStorage.get(lastKnownName).getOrNull()
}
fun Server_getUsers(): List<User> {
    val userStorage = Server_getUserStorage() ?: return emptyList()
    return userStorage.all.stream()
        .map { userStorage.get(it) }
        .filter { it.isPresent }
        .map { it.get() }
        .toList()
}
fun Server_getOfflineUsers(): List<User> {
    val userStorage = Server_getUserStorage() ?: return emptyList()
    return userStorage.all.stream()
        .map { userStorage.get(it) }
        .filter { it.isPresent }
        .map { it.get() }
        .filter { !it.isOnline }
        .toList()
}
fun Server_getPlayer(uuid: UUID) = Sponge.getServer().getPlayer(uuid).getOrNull()
fun Server_getPlayer(lastKnownName: String) = Sponge.getServer().getPlayer(lastKnownName).getOrNull()

fun Server_getPlayers(): Collection<Player> = Sponge.getServer().onlinePlayers
