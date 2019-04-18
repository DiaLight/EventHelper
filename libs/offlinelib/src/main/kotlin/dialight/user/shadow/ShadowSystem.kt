package dialight.user.shadow

import com.flowpowered.math.vector.Vector3d
import com.flowpowered.math.vector.Vector3i
import com.sun.javafx.collections.ObservableMapWrapper
import dialight.extensions.*
import jekarus.colorizer.Text_colorized
import org.slf4j.Logger
import org.spongepowered.api.Sponge
import org.spongepowered.api.data.DataContainer
import org.spongepowered.api.data.DataQuery
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.entity.Entity
import org.spongepowered.api.entity.EntityArchetype
import org.spongepowered.api.entity.EntityTypes
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.entity.living.player.User
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.cause.EventContextKeys
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes
import org.spongepowered.api.event.game.state.GameStartedServerEvent
import org.spongepowered.api.item.inventory.Container
import org.spongepowered.api.plugin.PluginContainer
import org.spongepowered.api.text.Text
import org.spongepowered.api.world.World
import org.spongepowered.common.data.util.DataQueries
import java.util.*

class ShadowSystem(
    val plugin: PluginContainer,
    val logger: Logger
) {

    val shadows = ObservableMapWrapper<UUID, OfflineShadow>(HashMap())

    fun spawnShadow(shadow: OfflineShadow): Entity {
        val loc = shadow.resolveLocation()
        val humanoid = loc.createEntity(OfflineShadow.TYPE)
        humanoid.offer(Keys.AI_ENABLED, false)
        humanoid.offer(Keys.IS_SILENT, true)
        humanoid.offer(Keys.DISPLAY_NAME, Text.of(shadow.name))
        humanoid.offer(Keys.CUSTOM_NAME_VISIBLE, true)
//        val entity = DataContainer.createNew().getSerializable(DataQueries.UNSAFE_NBT, EntityArchetype::class.java).get()
//        println(humanoid.toContainer().toJson())
/*        """
{"ContentVersion":1,"EntityClass":"net.minecraft.entity.passive.EntityVillager",
"WorldUuid":"6cee4396-6d4c-4104-a13b-d967f15cd129",
"Position":{"X":23.5,"Y":10.0,"Z":-1181.5},
"Rotation":{"X":0.0,"Y":3.3499855995178223,"Z":0.0},
"Scale":{"X":1.0,"Y":1.0,"Z":1.0},
"EntityType":"minecraft:villager",
"UnsafeData":{
  "HurtByTimestamp":0,
  "ForgeData":{"SpongeData":{"CanGrief":1,"maxAir":300}},
  "Attributes":[
    {"Base":20.0,"Name":"generic.maxHealth"},
    {"Base":0.0,"Name":"generic.knockbackResistance"},
    {"Base":0.5,"Name":"generic.movementSpeed"},
    {"Base":0.0,"Name":"generic.armor"},
    {"Base":0.0,"Name":"generic.armorToughness"},
    {"Base":16.0,"Name":"generic.followRange"}
  ],
  "Riches":0,"Invulnerable":0,
  "FallFlying":0,"ForcedAge":0,
  "PortalCooldown":0,"AbsorptionAmount":0.0,
  "FallDistance":0.0,"DeathTime":0,
  "HandDropChances":[0.085,0.085],
  "PersistenceRequired":0,"Age":0,"Motion":[0.0,0.0,0.0],
  "Leashed":0,"UUIDLeast":-5617022144227334363,
  "Health":20.0,"LeftHanded":0,"Air":300,"OnGround":0,
  "Dimension":0,"Rotation":[3.3499856,0.0],"HandItems":[{},{}],
  "ArmorDropChances":[0.085,0.085,0.085,0.085],
  "Profession":0,"UUIDMost":1769072127689444128,
  "Pos":[23.5,10.0,-1181.5],"Fire":-1,
  "ArmorItems":[{},{},{},{}],"CanPickUpLoot":1,
  "HurtTime":0,"CareerLevel":0,"Career":0,
  "Inventory":[],"Willing":0
}
}""".trimIndent()*/

        Sponge.getCauseStackManager().pushCauseFrame().use { frame ->
            frame.addContext(EventContextKeys.SPAWN_TYPE, SpawnTypes.PLUGIN)
            loc.extent.spawnEntity(humanoid)
        }

        humanoid.setCreator(shadow.uuid)
        shadow.entity = humanoid
        return humanoid
    }

    fun createShadow(user: User) = createShadow(user.uniqueId, user.name)
    fun createShadow(uuid: UUID, name: String) {
        val shadow = OfflineShadow(uuid, name)
        if(shadows.putIfAbsent(uuid, shadow) == null) {
            val player = Server_getPlayer(uuid)
            if(player == null) {
                spawnShadow(shadow)
            }
        }
    }

    fun removeShadow(user: User) = removeShadow(user.uniqueId)
    fun removeShadow(uuid: UUID) {
        val shadow = shadows.remove(uuid) ?: return
        shadow.entity?.remove()
    }

    @Listener
    fun onServerStart(event: GameStartedServerEvent) {
        Sponge.getEventManager().registerListeners(plugin, ShadowSystemListener(this))
    }

    fun getShadow(entity: Entity): OfflineShadow? {
        val creator = entity.creator.getOrNull() ?: return null
        return getShadow(creator)
    }

    fun getShadow(entity: User) = getShadow(entity.uniqueId)
    fun getShadow(uuid: UUID): OfflineShadow? {
        val shadow = shadows[uuid] ?: return null
        return shadow
    }

}