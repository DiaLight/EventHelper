package dialight.extensions

import com.flowpowered.math.vector.Vector3d
import org.spongepowered.api.data.property.entity.EyeLocationProperty
import org.spongepowered.api.entity.Entity
import org.spongepowered.api.entity.living.Humanoid
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World
import com.flowpowered.math.TrigMath
import com.flowpowered.math.imaginary.Quaterniond
import com.google.common.base.Preconditions
import org.spongepowered.api.Sponge
import org.spongepowered.api.block.BlockType
import org.spongepowered.api.block.BlockTypes
import org.spongepowered.api.entity.Transform
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.entity.living.player.User
import org.spongepowered.api.item.inventory.Inventory
import org.spongepowered.api.scheduler.Task
import sun.awt.windows.ThemeReader.getPosition
import java.util.*


//val Humanoid.eyeLocation: Location<World>
//    get() = location.add(.0, 1.0, .0)
val Entity.eyeLocation: Location<World>
    get() {
        val prop = getProperty<EyeLocationProperty>(EyeLocationProperty::class.java).getOrNull() ?: return location
        return Location(world, prop.value!!)
    }

var Entity.direction: Vector3d
    get() = rotationToDirection(rotation)
    set(value) { rotation = directionToRotation(value) }

fun rotationToDirection(vector: Vector3d): Vector3d {
    return Quaterniond.fromAxesAnglesDeg(vector.x, -vector.y, vector.z).direction
}
fun directionToRotation(vector: Vector3d): Vector3d {
    return Quaterniond.from(vector.x, vector.y, vector.z, 1.0).axesAnglesDeg.mul(.0, -1.0, .0)
}

private fun rotationToDirection_fast(vector: Vector3d): Vector3d {
    val yaw = Math.toRadians(vector.y)
    val pitch = Math.toRadians(vector.x)
    val x = Math.sin(pitch) * Math.cos(yaw)
    val y = Math.sin(pitch) * Math.sin(yaw)
    val z = Math.cos(pitch)
    return Vector3d(x, y, z)
}

private fun directionToRotation_fast(vector: Vector3d): Vector3d {
    val yaw = Math.atan2(vector.z, vector.x)
    val sqrt = Math.sqrt(vector.z * vector.z + vector.x * vector.x)
    val pitch = Math.atan2(sqrt, vector.y) + Math.PI
    return Vector3d(
        Math.toDegrees(pitch),
        Math.toDegrees(yaw),
        0.0
    )
}

private fun rotationToDirection_j(vector: Vector3d): Vector3d {
    val yaw = Math.toRadians(vector.y)
    val pitch = Math.toRadians(vector.x)
    val y = -Math.sin(pitch)
    val xz = Math.cos(pitch)
    val x = -xz * Math.sin(yaw)
    val z = xz * Math.cos(yaw)
    return Vector3d(x, y, z)
}

val User.location: Location<World>?
    get() {
        val wuuid = worldUniqueId.getOrNull() ?: return null
        val world = Sponge.getServer().getWorld(wuuid).getOrNull() ?: return null
        return Location(world, position)
    }


fun BlockType.isSolid() = when(this) {
    BlockTypes.AIR,
    BlockTypes.ACACIA_DOOR,
    BlockTypes.BIRCH_DOOR,
    BlockTypes.DARK_OAK_DOOR,
    BlockTypes.ACACIA_DOOR,
    BlockTypes.GRASS,
    BlockTypes.TALLGRASS,
    BlockTypes.GRASS_PATH
    -> false
    else -> true
}

fun Player.teleportSafe(loc: Location<World>) {

    val feet = loc.block
    val head = loc.add(.0, 1.0, .0).block
    if(feet.type.isSolid() || feet.type.isSolid()) {
        setLocationSafely(loc)
        return
    }
    location = loc
}
