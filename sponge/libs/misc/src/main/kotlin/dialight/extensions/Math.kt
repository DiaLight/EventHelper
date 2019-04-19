package dialight.extensions

import com.flowpowered.math.vector.Vector3d


/**
 * Находит расстояние от цели до прямой
 * @param location Точка на прямой
 * @param direction Вектор от точки задающий прямую
 * @param point Точка-цель
 * @return
 */
fun projectionToRay(location: Vector3d, direction: Vector3d, point: Vector3d): Double {
    val x = point.x
    val y = point.y
    val z = point.z
    val x0 = location.x
    val y0 = location.y
    val z0 = location.z
    val vx = direction.x
    val vy = direction.y
    val vz = direction.z
    val a = Math.pow((y0 - y) * vz - (z0 - z) * vy, 2.0) + Math.pow(
        (z0 - z) * vx - (x0 - x) * vz,
        2.0
    ) + Math.pow((x0 - x) * vy - (y0 - y) * vx, 2.0)
    val b = Math.pow(vx, 2.0) + Math.pow(vy, 2.0) + Math.pow(vz, 2.0)
    return Math.pow(a / b, 0.5)
}
