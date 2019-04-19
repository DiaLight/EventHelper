package dialight.captain.system

import dialight.captain.CaptainPlugin
import dialight.captain.system.members.CSUsers
import dialight.extensions.*
import dialight.scheduler.Delta
import dialight.scheduler.Ticker
import dialight.user.shadow.OfflineShadow
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.entity.Entity
import org.spongepowered.api.entity.EntityTypes
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.entity.MoveEntityEvent
import org.spongepowered.api.event.filter.cause.First
import org.spongepowered.api.scheduler.Task
import org.spongepowered.api.world.World
import java.util.*
import kotlin.streams.toList

class CaptainSystemListener(val plugin: CaptainPlugin, val session: SelectPlayerSession, val users: CSUsers, ticker: Ticker) {

    private val delta = Delta(ticker)
    private var lastGlowing: UUID? = null

    @Listener
    fun onMove(event: MoveEntityEvent, @First player: Player) {
        if(player.uniqueId != session.captain.uuid) return
        if(delta.check() < 5) return
        delta.point()
        val dir = rotationToDirection(event.toTransform.rotation)
        val loc = event.toTransform.location.position.add(dir).add(dir).add(0.0, 1.0, 0.0)
        var trg: Entity? = Utils.getEnByDirection(loc, dir, 20.0, 1.5, users.getUnselectedEntities().toList())
        if(trg != null) {
            when(trg.type) {
                OfflineShadow.TYPE -> {
                    val shadow = plugin.offlinelib.shadow.getShadow(trg)
                    if(shadow != null) {
                        val entity = shadow.entity
                        if(entity != null) {
                            trg = entity
                        }
                    }
                }
            }
        }
        Task.builder().execute { task ->
            if(trg != null) {
                if(trg.uniqueId != lastGlowing) {
                    clearLast(player.world)
                    trg.offer(Keys.GLOWING, true)
                    lastGlowing = trg.uniqueId
                }
            } else {
                clearLast(player.world)
            }
        }.submit(plugin)
    }

    fun clearLast(world: World) {
        val last = lastGlowing ?: return
        lastGlowing = null
        val trg = world.getEntity(last).getOrNull() ?: return
        trg.offer(Keys.GLOWING, false)
    }

}