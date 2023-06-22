package space.impact.space.api.world.world_math.perlin

import net.minecraft.util.MathHelper
import kotlin.math.pow

class WhiteNoise(genSeed: Long) {

    val seed = genSeed.toDouble().pow(11.0) * 17L + 514L

    fun gen2d(x: Long, z: Long): Double {
        var n = (seed + x * 4L + z * 341L).toLong()
        n = n shl 13L.toInt() xor n
        return 1.0 - (n * (n * n * 15731L + 789221L) + 1376312589L and 0x7FFFFFFFL) / 1.073741824E9
    }

    fun gen2f(x: Int, z: Int): Float {
        var n = seed.toInt() + x * 4 + z * 341
        n = n shl 13 xor n
        return 1.0f - (n * (n * n * 15731 + 789221) + 1376312589 and Int.MAX_VALUE) / 1.07374182E9f
    }

    fun vecGen2d(x: Long, z: Long): DoubleArray? {
        val angle = gen2d(x, z) * Math.PI
        return doubleArrayOf(MathHelper.cos(angle.toFloat()).toDouble(), MathHelper.sin(angle.toFloat()).toDouble())
    }

    fun vecGen2f(x: Int, z: Int): FloatArray? {
        val angle = gen2f(x, z) * Math.PI.toFloat()
        return floatArrayOf(MathHelper.cos(angle), MathHelper.sin(angle))
    }

    fun smartGen2d(x: Long, z: Long): Double {
        return gen2d(x, z)
    }

    fun smartGen2f(x: Int, z: Int): Float {
        return gen2f(x, z)
    }

    fun smartVecGen2d(x: Long, z: Long): DoubleArray? {
        return vecGen2d(x, z)
    }

    fun smartVecGen2f(x: Int, z: Int): FloatArray? {
        return vecGen2f(x, z)
    }
}