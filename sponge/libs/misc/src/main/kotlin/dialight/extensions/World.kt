package dialight.extensions

import com.flowpowered.math.vector.Vector3d
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World

fun Vector3d.fmtString() = String.format("Vec{%.2f %.2f %.2f}", x, y, z)
fun Location<World>.fmtString() = String.format("Loc{%.2f %.2f %.2f}", x, y, z)


