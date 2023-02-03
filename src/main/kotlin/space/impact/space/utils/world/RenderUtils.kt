package space.impact.space.utils.world

import kotlin.math.cos

object RenderUtils {

    fun calculateCelestialAngle(worldtime: Long, ticks: Float, daylenght: Float): Float {
        val j = (worldtime.toFloat() % daylenght).toInt()
        var f1 = (j.toFloat() + ticks) / daylenght - 0.25f
        if (f1 < 0.0f) {
            ++f1
        }
        if (f1 > 1.0f) {
            --f1
        }
        val f2 = f1
        f1 = 1.0f - ((cos(f1.toDouble() * Math.PI) + 1.0) / 2.0).toFloat()
        f1 = f2 + (f1 - f2) / 3.0f
        return f1
    }

}