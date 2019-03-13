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
import org.spongepowered.api.entity.Transform
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.inventory.Inventory
import org.spongepowered.api.scheduler.Task
import sun.awt.windows.ThemeReader.getPosition
import java.util.*


//val Humanoid.eyeLocation: Location<World>
//    get() = location.add(.0, 1.0, .0)
val Entity.eyeLocation: Location<World>
    get() {
        val oprop = getProperty<EyeLocationProperty>(EyeLocationProperty::class.java)
        if(!oprop.isPresent) return location
        val prop = oprop.get()
        return Location(world, prop.value!!)
    }

var Entity.direction: Vector3d
    get() = Quaterniond.fromAxesAnglesDeg(rotation.x, -rotation.y, rotation.z).direction
    set(value) { rotation = Quaterniond.from(value.x, value.y, value.z, 1.0).axesAnglesDeg.mul(.0, -1.0, .0) }


fun Player.openInventoryLater(plugin: Any, inventory: Inventory) {
    Task.builder().execute { task -> openInventory(inventory) }.submit(plugin)
}
