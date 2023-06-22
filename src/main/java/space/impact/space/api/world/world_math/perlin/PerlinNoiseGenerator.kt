package space.impact.space.api.world.world_math.perlin

import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow

object PerlinNoiseGenerator {

    fun numberNoise2D(seed: Long, x: Long, y: Long): Double {
        var n = x + y * 31L + seed * 6L
        n = n shl 13L.toInt() xor n
        return 1.0 - (n * (15732L * n xor 0xC0B4FL) + 1376312589L and 0x7FFFFFFFL) / 1.073741824E9
    }

    fun smoothNoise2D(seed: Long, x: Long, y: Long): Double {
        val corners = (numberNoise2D(seed, x - 1L, y - 1L) + numberNoise2D(seed, x + 1L, y - 1L) + numberNoise2D(seed, x - 1L, y + 1L) + numberNoise2D(seed, x + 1L, y + 1L)) / 16.0
        val sides = (numberNoise2D(seed, x - 1L, y) + numberNoise2D(seed, x + 1L, y) + numberNoise2D(seed, x, y - 1L) + numberNoise2D(seed, x, y + 1L)) / 8.0
        val center: Double = numberNoise2D(seed, x, y) / 4.0
        return corners + sides + center
    }

    fun cosineInterpolate(a: Double, b: Double, x: Double): Double {
        val f = (1.0 - cos(x * Math.PI)) * 0.5
        return a * (1.0 - f) + b * f
    }

    fun cosineInterpolatedNoise2D(seed: Long, x: Double, y: Double): Double {
        val lx = x.toLong()
        val ly = y.toLong()
        val fx = abs(x) - abs(lx)
        val fy = abs(y) - abs(ly)
        val v1: Double = smoothNoise2D(seed, lx, ly)
        val v2: Double = smoothNoise2D(seed, if (abs(x) == x) lx + 1L else lx - 1L, ly)
        val v3: Double = smoothNoise2D(seed, lx, if (abs(y) == y) ly + 1L else ly - 1L)
        val v4: Double = smoothNoise2D(seed, if (abs(x) == x) lx + 1L else lx - 1L, if (abs(y) == y) ly + 1L else ly - 1L)
        val i1: Double = cosineInterpolate(v1, v2, fx)
        val i2: Double = cosineInterpolate(v3, v4, fx)
        return cosineInterpolate(i1, i2, fy)
    }

    fun perlinNoise2D(seed: Long, x: Double, y: Double, persistence: Double, numberOfOctaves: Int): Double {
        var total = 0.0
        for (i in 0..numberOfOctaves) {
            val frequency = (1 shl i).toDouble()
            val amplitude = persistence.pow(i.toDouble())
            total += cosineInterpolatedNoise2D(seed, x * frequency, y * frequency) * amplitude
        }
        return total
    }
}