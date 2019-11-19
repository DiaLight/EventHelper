package dialight.maingui.gui

import com.flowpowered.math.vector.Vector2i
import dialight.guilib.View
import java.util.HashMap

class MainPageBuilder(
    val width: Int,
    val height: Int,
    val items: List<View.Item>
) : Iterator<HashMap<Vector2i, View.Item>>, Iterable<HashMap<Vector2i, View.Item>> {

    private var itemsIt: Iterator<View.Item> = items.iterator()


    override fun iterator() = this
    override fun hasNext(): Boolean {
        return itemsIt.hasNext()
    }

    override fun next(): HashMap<Vector2i, View.Item> {
        val bld = Builder()
        bld.render()
        return bld.map
    }

    private inner class Builder {

        val center = Vector2i((width + 1) / 2 - 1, height / 2 - 1)
        val map = HashMap<Vector2i, View.Item>()
        var left = 0
        var up = 0

        init {
//            println("size $width $height")
//            println("canter$center")
        }

        fun render() {
            if(!up()) return
            if(!left()) return
            if(!up()) return
            if(!left()) return
            if(!up()) return
            if(!left()) return
        }

        fun next(pos: Vector2i): Boolean {
            if(map.containsKey(pos)) return true
//            println("  next ${pos.x} ${pos.y}")
            if(!itemsIt.hasNext()) return false
            map[pos] = itemsIt.next()
            return true
        }

        fun left(): Boolean {
            left++
//            println("- left $left  up $up  center$center")
            for (y in (0..(up - 1))) {
//                println("y $y")
//                println(" up")
                if(!next(center.sub(left, y))) return false
                if(!next(center.add(left, y + 1))) return false
                if(!next(center.sub(-left, y))) return false
                if(!next(center.add(-left, y + 1))) return false
            }
            return true
        }

        fun up(): Boolean {
            up++
//            println("- up $up  left $left  center$center")
//            println(" mid")
            if(!next(center.sub(0, up - 1))) return false
            if(!next(center.add(0, up))) return false
            for (x in (1..left)) {
//                println("x $x")
                if(!next(center.sub(x, up - 1))) return false
                if(!next(center.add(x, up))) return false
                if(!next(center.sub(-x, up - 1))) return false
                if(!next(center.add(-x, up))) return false
            }
            return true
        }

    }

}