package dialight.captain.system.arena

import com.flowpowered.math.vector.Vector3i
import dialight.captain.system.members.CSUser
import org.spongepowered.api.Sponge
import org.spongepowered.api.block.BlockState
import org.spongepowered.api.block.BlockType
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.data.manipulator.mutable.DyeableData
import org.spongepowered.api.data.type.DyeColor
import java.util.*


class Schematic {
    val blocks = HashMap<Vector3i, BlockState>()
    val users = HashMap<Vector3i, CSUser>()
    lateinit var actve: Vector3i

    fun block(pos: Vector3i, type: BlockType) {
        blocks[pos] = BlockState.builder().blockType(type).build()
    }
    fun block(pos: Vector3i, block: BlockState) {
        blocks[pos] = block
    }
    fun block(pos: Vector3i, type: BlockType, color: DyeColor) {
        val dyeableData = Sponge.getDataManager().getManipulatorBuilder(DyeableData::class.java).get().create()
        dyeableData.set(Keys.DYE_COLOR, color)
        val state = BlockState.builder()
            .blockType(type)
            .add(dyeableData)
            .build()
        blocks[pos] = state
    }

    fun user(vec: Vector3i, user: CSUser) {
        users[vec] = user
    }

    fun getMinMax(): Pair<Vector3i, Vector3i> {
        var minX = Integer.MAX_VALUE
        var minY = Integer.MAX_VALUE
        var minZ = Integer.MAX_VALUE
        var maxX = Integer.MIN_VALUE
        var maxY = Integer.MIN_VALUE
        var maxZ = Integer.MIN_VALUE
        for((vec, block) in blocks.entries) {
            if(vec.x < minX) minX = vec.x
            if(vec.x > maxX) maxX = vec.x
            if(vec.y < minY) minY = vec.y
            if(vec.y > maxY) maxY = vec.y
            if(vec.z < minZ) minZ = vec.z
            if(vec.z > maxZ) maxZ = vec.z
        }
        return Pair(Vector3i(minX, minY, minZ), Vector3i(maxX, maxY, maxZ))
    }
    fun getSize(): Vector3i {
        val (min, max) = getMinMax()
        return max.sub(min)
    }
    fun centerOffset(): Vector3i {
        val (min, max) = getMinMax()
        return min.mul(-1)
    }

}