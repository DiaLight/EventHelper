package dialight.captain.system

import com.flowpowered.math.vector.Vector3d

class CirclePointIterator<T>(
    objs: Collection<T>,
    val dist: Double,
    minRadius: Double
) : Iterable<Pair<CirclePointIterator.Point, T>>, Iterator<Pair<CirclePointIterator.Point, T>> {

    val r = Math.max(Math.ceil(objs.size / Math.PI) * dist, minRadius)
    val it = objs.iterator()

    var angle = .0
    var index = 0
    var mirror = false
    var last = Point()
    var middle = true

    init {
//        println("objs.size: ${objs.size}")
    }

    override fun iterator() = this

    private fun Vector3d.align(): Vector3d = toInt().toDouble().add(.5, .0, .5)

    data class Point(
        val index: Int = 0,
        val loc: Vector3d = Vector3d(),
        val forward: Vector3d = Vector3d(),
        val right: Vector3d = Vector3d(),
        val up: Vector3d = Vector3d(.0 ,1.0, .0)
    )

    fun mirror(st: Point): Point {
        return Point(
            st.index,
            st.loc.mul(1.0, 1.0, -1.0).add(.0, .0, 1.0),
            st.forward.mul(1.0, 1.0, -1.0),
            st.right
        )
    }

    override fun hasNext() = it.hasNext()

    override fun next(): Pair<Point, T> {
        if(mirror) {
            mirror = false
            if(!middle) return Pair(mirror(last), it.next())
            middle = false
        }

        val sideAngle = angle + (Math.PI / 2)
        val side = Vector3d(
            Math.cos(sideAngle),
            0.0,
            Math.sin(sideAngle)
        )
        val forward = Vector3d(
            Math.cos(angle),
            0.0,
            Math.sin(angle)
        )

        var pos = forward.mul(r)
        pos = pos.align()

        if (!middle) {
            if(index == 0) {
                pos = pos.add(side.mul(dist / 2))
            } else {
                pos = pos.add(side.mul(dist))
            }
            pos = pos.align()
        }

//        println("$index  $pos  ${angle.fmt}")
        val point = Point(index++, pos, forward, side)
        last = point

        angle = Math.atan(pos.z / pos.x)
        mirror = true
        return Pair(point, it.next())
    }


    private val Vector3d.fmt get() = String.format("[%.2f %.2f %.2f]", x, y, z)

    private val Double.fmt get() = String.format("%.2f", this)

}
