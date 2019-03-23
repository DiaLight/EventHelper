package dialight.extensions

import com.flowpowered.math.vector.Vector3d
import org.spongepowered.api.block.BlockTypes
import org.spongepowered.api.entity.Entity
import org.spongepowered.api.entity.EntityType
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.zip.ZipFile
import sun.awt.windows.ThemeReader.getPosition
import org.spongepowered.api.util.blockray.BlockRayHit
import org.spongepowered.api.util.blockray.BlockRay


object Utils {

    fun matchWorld(world: File): Boolean {
        if (!world.isDirectory) return false
        if (File(world, "level.dat").exists()) return true
        val region = File(world, "region")
        if (!region.exists()) return false
        if (!region.isDirectory) return false
        return region.listFiles()!!.isNotEmpty()
    }


//    fun fastItemEquals(st: ItemStack, nd: ItemStack?): Boolean {
//        if (nd == null) return false
//        if (st.hashCode() != nd.hashCode()) return false
//        if (st.type != nd.type) return false
//        if (st.getItemMeta().getDisplayName() != nd.getItemMeta().getDisplayName()) return false
//        if (st.getEnchantments().size != nd.getEnchantments().size) return false
//        if (st.getItemMeta().getLore().size != nd.getItemMeta().getLore().size) return false
//        val lst = st.getItemMeta().getLore()
//        val lnd = nd.getItemMeta().getLore()
//        for (i in 0 until st.getItemMeta().getLore().size)
//            if (lst.get(i) != lnd.get(i)) return false
//
//        //return st.isSimilar(nd);
//        return true
//    }

//    fun fastItemRemove(inv: Inventory, st: ItemStack) {
//        for (i in 0 until inv.getContents().size)
//            if (fastItemEquals(st, inv.getContents()[i])) inv.clear(i)
//    }

    /**
     * Функция нахождения первого попавшегося блока
     * @param l Точка отсчета
     * @param v Шаг
     * @param i Количество шагов
     * @return Первый попавшийся блок
     */
    fun getTargetBlock(l: Location<World>, v: Vector3d, i: Int): Location<World> {
//        val blockRay = BlockRay.from(l).skipFilter(BlockRay.onlyAirFilter()).stopFilter(BlockRay.allFilter()).build()
//        val hitOpt = blockRay.end()
//        if (hitOpt.isPresent) {
//            val hit = hitOpt.get()
//            println(
//                "Found " + hit.location.blockType + " block at "
//                        + hit.location + " with intersection at " + hit.position
//            )
//        }
//        v.normalize()
        var r = l
        for (j in 1..i) {
            r = r.add(v)
            val b = r.block
            if (b.type != BlockTypes.AIR) break
        }
        return Location(r.extent, r.blockPosition)
    }

    /**
     * Ищет сущность по направлению куда смотрит player.
     * @param player Игрок от которого идет поиск.
     * @param distance Расстояние на котором ищутся сущности.
     * @param delta Ширина проверки.
     * @param types
     * @return Найденная сущность или null, если сущность не найдена.
     */

    fun getEnByDirection(player: Player, distance: Double, delta: Double, vararg types: EntityType): Entity? {
        val v = player.direction
        val l = player.eyeLocation.position.add(v).add(v)
        val half = distance / 2
        val center = l.add(v.clone().mul(half)) //Центр отрезка
        val entities: Collection<Entity> = if(types.isEmpty()) {
            player.world.getNearbyEntities(center, half)
        } else {
            val etypes = types.asList()
            player.getNearbyEntities {
                if(!etypes.contains(it.type)) return@getNearbyEntities false
                it.location.position.distance(center) <= half
            }
        }
        var closestEntity: Entity? = null
        var closestProj = delta
        for (en in entities) {
            val t = en.location.position.add(0.0, 1.0, 0.0)
            val curProj = projectionToRay(l, v, t)
            if (curProj <= closestProj) {
                closestProj = curProj
                closestEntity = en
            }  // Условие нахождения ближайшей к прямой сущности
        }
        return closestEntity
    }

    fun arrList(vararg args: String): ArrayList<String> {
        val ret = ArrayList<String>(args.size)
        for (arg in args) {
            ret.add(arg)
        }
        return ret
    }

    fun addAll(list: MutableList<String>, vararg args: String) {
        for (arg in args) {
            list.add(arg)
        }
    }

    fun topDiv(a: Int, b: Int): Int {
        return (a + (b - 1)) / b
    }

    @Throws(IOException::class)
    fun unzip(zipFile: File, dest: File) {
        ZipFile(zipFile).use { zip ->
            val entries = zip.entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement()
                val entryDest = File(dest, entry.name)
                if (entry.isDirectory) {
                    entryDest.mkdirs()
                } else {
                    entryDest.parentFile.mkdirs()
                    val `in` = zip.getInputStream(entry)
                    FileOutputStream(entryDest).use { out ->
                        //                        IOUtils.copy(in, out);
                        //                        IOUtils.closeQuietly(in);
                    }
                }
            }
        }
    }

}
