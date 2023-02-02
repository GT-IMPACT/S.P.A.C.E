package space.impact.space.api.world.world_math.perlin

abstract class GenNoiseBase(
    val seed: Long,
    val persistence: Double,
    val scaleX: Double,
    val scaleY: Double,
    val scaleZ: Double,
    val octaves: Int,
    val height: Int,
    val interpolation: Int = 2,
    val rand: WhiteNoise = WhiteNoise(seed)
) {

    open fun lerp3d(a: Double, b: Double, n: Double): Double {
        return a + (b - a) * n
    }

    open fun lerp3f(a: Float, b: Float, n: Float): Float {
        return a + (b - a) * n
    }

    open fun smoothstep1d(n: Double): Double {
        return n * n * (3.0 - 2.0 * n)
    }

    open fun smoothstep1f(n: Float): Float {
        return n * n * (3.0f - 2.0f * n)
    }

    open fun smootherstep1d(n: Double): Double {
        return n * n * n * (n * (n * 6.0 - 15.0) + 10.0)
    }

    open fun smootherstep1f(n: Float): Float {
        return n * n * n * (n * (n * 6.0f - 15.0f) + 10.0f)
    }

    open fun autoSmooth1d(n: Double): Double {
        when (interpolation) {
            2 -> return smootherstep1d(n)
            1 -> return smoothstep1d(n)
        }
        return n
    }

    open fun autoSmooth1f(n: Float): Float {
        when (interpolation) {
            2 -> return smootherstep1f(n)
            1 -> return smoothstep1f(n)
        }
        return n
    }

    open fun dot2d(vec1: DoubleArray, vec2: DoubleArray): Double {
        return vec1[0] * vec2[0] + vec1[1] * vec2[1]
    }

    open fun dot2f(vec1: FloatArray, vec2: FloatArray): Float {
        return vec1[0] * vec2[0] + vec1[1] * vec2[1]
    }

    abstract fun noiseOctave2d(x: Double, z: Double): Double

    abstract fun noiseOctave2f(x: Float, z: Float): Float

    open fun genNoise2d(x: Double, z: Double): Double {
        var result = 0.0
        var amplitudeMultiplier = 1.0
        var nowX = x
        var nowZ = z
        for (i in 0 until octaves) {
            result += noiseOctave2d(nowX / scaleX, nowZ / scaleZ) * amplitudeMultiplier
            amplitudeMultiplier *= persistence
            nowX *= 2.0
            nowZ *= 2.0
        }
        return height + result * scaleY
    }

    open fun genNoise2f(x: Float, z: Float): Float {
        var result = 0.0f
        var amplitudeMultiplier = 1.0f
        var nowX = x
        var nowZ = z
        for (i in 0 until octaves) {
            result += noiseOctave2f(nowX / scaleX.toFloat(), nowZ / scaleZ.toFloat()) * amplitudeMultiplier
            amplitudeMultiplier *= persistence.toFloat()
            nowX *= 2.0f
            nowZ *= 2.0f
        }
        return height + result * scaleY.toFloat()
    }
}