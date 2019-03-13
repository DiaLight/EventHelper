package dialight.extensions

import org.spongepowered.api.Sponge
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.entity.living.player.User
import org.spongepowered.api.service.user.UserStorageService
import java.util.*
import kotlin.streams.toList


fun Server_getUser(uuid: UUID): User? {
    val ouserStorage = Sponge.getServiceManager().provide(UserStorageService::class.java)
    if(!ouserStorage.isPresent) return null
    val userStorage = ouserStorage.get()
    val ouser = userStorage.get(uuid)
    if(!ouser.isPresent) return null
    return ouser.get()
}
fun Server_getUser(lastKnownName: String): User? {
    val ouserStorage = Sponge.getServiceManager().provide(UserStorageService::class.java)
    if(!ouserStorage.isPresent) return null
    val userStorage = ouserStorage.get()
    val ouser = userStorage.get(lastKnownName)
    if(!ouser.isPresent) return null
    return ouser.get()
}
fun Server_getUsers(): List<User> {
    val ouserStorage = Sponge.getServiceManager().provide(UserStorageService::class.java)
    if(!ouserStorage.isPresent) return emptyList()
    val userStorage = ouserStorage.get()
    return userStorage.all.stream()
        .map { userStorage.get(it) }
        .filter { it.isPresent }
        .map { it.get() }
        .toList()
}
fun Server_getOfflineUsers(): List<User> {
    val ouserStorage = Sponge.getServiceManager().provide(UserStorageService::class.java)
    if(!ouserStorage.isPresent) return emptyList()
    val userStorage = ouserStorage.get()
    return userStorage.all.stream()
        .map { userStorage.get(it) }
        .filter { it.isPresent }
        .map { it.get() }
        .filter { !it.isOnline }
        .toList()
}
fun Server_getPlayer(uuid: UUID): Player? {
    val oplayer = Sponge.getServer().getPlayer(uuid)
    if(!oplayer.isPresent) return null
    return oplayer.get()
}
fun Server_getPlayer(lastKnownName: String): Player? {
    val oplayer = Sponge.getServer().getPlayer(lastKnownName)
    if(!oplayer.isPresent) return null
    return oplayer.get()
}
fun Server_getPlayers(): Collection<Player> = Sponge.getServer().onlinePlayers
