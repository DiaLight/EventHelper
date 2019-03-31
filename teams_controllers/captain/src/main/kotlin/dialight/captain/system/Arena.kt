package dialight.captain.system

import com.flowpowered.math.vector.Vector3d
import com.flowpowered.math.vector.Vector3i
import dialight.extensions.Server_getPlayer
import dialight.extensions.Server_getPlayers
import org.spongepowered.api.Sponge
import org.spongepowered.api.block.BlockState
import org.spongepowered.api.block.BlockType
import org.spongepowered.api.block.BlockTypes
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.data.manipulator.mutable.DyeableData
import org.spongepowered.api.data.type.DyeColor
import org.spongepowered.api.data.type.DyeColors
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World
import java.util.*

class Arena(val sys: CaptainSystem) {

    var areaIsBuilt = false

    var maybeLoc: Location<World>? = null
    val blocks = ArrayList<Location<World>>()

    init {
        sys.members.apply {
            onPut { uuid, member -> onPlayersCountChange() }
            onRemove { uuid, member -> onPlayersCountChange() }
        }
    }
    private fun onPlayersCountChange() {
        if(!areaIsBuilt) return
        clear()
        build()
    }

    fun getSearchLocation(): Location<World> {
        if(maybeLoc != null) return maybeLoc!!
        val loc = Server_getPlayers().random().location
        maybeLoc = loc
        return loc
    }

    fun build(): Boolean {
        if(areaIsBuilt) return true
        blocks.clear()
        val (captains, toSort) = sys.collect()

        // build scheme
        val sch = Schematic()

        sch.block(Vector3i(0, -1, 0), BlockTypes.STAINED_GLASS, DyeColors.BLUE)
        for((st, m) in CirclePointIterator(toSort, 1.6, 6.0)) {
            if((st.index % 2) != 0) {
                val ipos = st.loc.add(st.forward).toInt()
                sch.block(ipos, BlockTypes.STAINED_GLASS, DyeColors.GREEN)
                sch.member(ipos.add(0, 1, 0), m)
            } else {
                val ipos = st.loc.sub(st.up).toInt()
                sch.block(ipos, BlockTypes.STAINED_GLASS, DyeColors.RED)
                sch.member(ipos.add(0, 1, 0), m)
            }
        }
        var pos = Vector3i(-2, 1, -captains.size + 1)
        for(cap in captains) {
            sch.block(pos, BlockTypes.STAINED_GLASS, DyeColors.YELLOW)
            sch.member(pos.add(0, 1, 0), cap)
            pos = pos.add(0, 0, 2)
        }

        // search to place building
        val size = sch.getSize()
        val startLoc = getSearchLocation()
        var cur = startLoc.sub(size.x / 2.0, 0.0, size.z / 2.0)
        var firstGood = cur.y
        val maxY = cur.extent.blockMax.y
        var found = false
        while(cur.y < maxY) {
            var goodLayer = true
            for (x in (0..(size.x - 1))) {
                for (z in (0..(size.z - 1))) {
                    val btype = cur.add(x.toDouble(), 0.0, z.toDouble()).block.type
                    if (btype != BlockTypes.AIR) {
                        goodLayer = false
                        break
                    }
                }
                if(!goodLayer) break
            }
            if(goodLayer) {
                if((cur.y - firstGood) >= size.y) {
                    found = true
                    break
                }
            }
            cur = cur.add(0.0, 1.0, 0.0)
            if(!goodLayer) {
                firstGood = cur.y
            }
        }
        if(!found) return false
        val floc = cur.sub(0.0, size.y.toDouble(), 0.0).add(sch.centerOffset())

        // apply
        for((dl, block) in sch.blocks.entries) {
            val loc = floc.add(dl)
            loc.block = block
            blocks.add(loc)
        }
        for ((dl, m) in sch.members.entries) {
            val loc = Location<World>(floc.extent, floc.add(dl).blockPosition).add(0.5, 0.0, 0.5)
            val player = Server_getPlayer(m.uuid)
            player?.location = loc
        }

        areaIsBuilt = true
        return true
    }

    fun clear() {
        if(!areaIsBuilt) return

        for(loc in blocks) {
            loc.blockType = BlockTypes.AIR
        }

        areaIsBuilt = false
    }

    class Schematic {
        val blocks = HashMap<Vector3i, BlockState>()
        val members = HashMap<Vector3i, CaptainSystem.Member>()

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

        fun member(vec: Vector3i, member: CaptainSystem.Member) {
            members[vec] = member
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

}
