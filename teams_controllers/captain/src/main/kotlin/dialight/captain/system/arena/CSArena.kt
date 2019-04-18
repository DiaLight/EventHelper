package dialight.captain.system.arena

import com.flowpowered.math.vector.Vector3i
import dialight.captain.CaptainPlugin
import dialight.captain.system.CirclePointIterator
import dialight.captain.system.members.CSUsers
import dialight.extensions.Server_getPlayers
import dialight.extensions.dyeColor
import org.spongepowered.api.block.BlockTypes
import org.spongepowered.api.data.type.DyeColors
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World
import java.util.*
import kotlin.collections.HashMap

class CSArena(
    val users: CSUsers
) {

    var areaIsBuilt = false

    var maybeLoc: Location<World>? = null
    val blocks = ArrayList<Location<World>>()
    lateinit var active: Location<World>

    fun rebuild() {
        if(!areaIsBuilt) return
        clear()
        build()
    }

    fun getSearchLocation(): Location<World> {
        if(maybeLoc != null) return maybeLoc!!
        val loc = users.resolveArenaLocation()
        maybeLoc = loc
        return loc
    }

    fun build(): Boolean {
        if(areaIsBuilt) return true
        blocks.clear()

        // build scheme
        val sch = Schematic()

        sch.block(Vector3i(0, -1, 0), BlockTypes.SEA_LANTERN)
        sch.actve = Vector3i(0, 0, 0)
        for((st, m) in CirclePointIterator(users.getUnselected(), 1.6, 6.0)) {
            if((st.index % 2) != 0) {
                val ipos = st.loc.add(st.forward).toInt()
                sch.block(ipos, BlockTypes.STAINED_GLASS)
                sch.user(ipos.add(0, 1, 0), m)
            } else {
                val ipos = st.loc.sub(st.up).toInt()
                sch.block(ipos, BlockTypes.STAINED_GLASS)
                sch.user(ipos.add(0, 1, 0), m)
            }
        }
        val members_captains = users.getCaptains()
        var pos = Vector3i(-2, 1, -members_captains.size + 1)
        for(cap in members_captains) {
            sch.block(pos, BlockTypes.WOOL, cap.dyeColor)
            sch.user(pos.add(0, 1, 0), cap)
            pos = pos.add(0, 0, 2)
        }

        // search space to place building
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

        // apply building
        for((dl, block) in sch.blocks.entries) {
            val loc = floc.add(dl)
            loc.block = block
            blocks.add(loc)
        }
        for ((vec, user) in sch.users.entries) {
            val loc = Location<World>(floc.extent, floc.add(vec).blockPosition).add(0.5, 0.0, 0.5)
            user.cs_location = loc
        }
        active = Location<World>(floc.extent, floc.add(sch.actve).blockPosition).add(0.5, 0.0, 0.5)

        areaIsBuilt = true
        return true
    }

    fun clear() {
        if(!areaIsBuilt) return

        for(loc in blocks) {
            loc.blockType = BlockTypes.AIR
        }
        maybeLoc = null

        areaIsBuilt = false
    }

}
