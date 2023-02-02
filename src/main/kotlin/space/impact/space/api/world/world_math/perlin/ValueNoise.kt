package space.impact.space.api.world.world_math.perlin

import kotlin.math.abs

class ValueNoise(
    seed: Long,
    persistence: Double,
    scaleX: Double,
    scaleY: Double,
    scaleZ: Double,
    octaves: Int,
    height: Int,
    interpolation: Int = 2,
) : GenNoiseBase(seed, persistence, scaleX, scaleY, scaleZ, octaves, height, interpolation) {

    override fun noiseOctave2d(x: Double, z: Double): Double {
        val squareStartX = x.toLong()
        val squareStartZ = z.toLong()
        var xs = 1L
        var zs = 1L
        if (abs(x) != x) xs = -1L
        if (abs(z) != z) zs = -1L
        val pointInQuadX = abs(x) - abs(squareStartX)
        val pointInQuadZ = abs(z) - abs(squareStartZ)
        val topLeft = rand.smartGen2d(squareStartX, squareStartZ)
        val topRight = rand.smartGen2d(squareStartX + xs, squareStartZ)
        val bottomLeft = rand.smartGen2d(squareStartX, squareStartZ + zs)
        val bottomRight = rand.smartGen2d(squareStartX + xs, squareStartZ + zs)
        val line1 = lerp3d(topLeft, topRight, autoSmooth1d(pointInQuadX))
        val line2 = lerp3d(bottomLeft, bottomRight, autoSmooth1d(pointInQuadX))
        return lerp3d(line1, line2, autoSmooth1d(pointInQuadZ))
    }

    override fun noiseOctave2f(x: Float, z: Float): Float {
        val squareStartX = x.toInt()
        val squareStartZ = z.toInt()
        var xs = 1
        var zs = 1
        if (abs(x) != x) xs = -1
        if (abs(z) != z) zs = -1
        val pointInQuadX = abs(x) - abs(squareStartX)
        val pointInQuadZ = abs(z) - abs(squareStartZ)
        val topLeft = rand.smartGen2f(squareStartX, squareStartZ)
        val topRight = rand.smartGen2f(squareStartX + xs, squareStartZ)
        val bottomLeft = rand.smartGen2f(squareStartX, squareStartZ + zs)
        val bottomRight = rand.smartGen2f(squareStartX + xs, squareStartZ + zs)
        val line1 = lerp3f(topLeft, topRight, autoSmooth1f(pointInQuadX))
        val line2 = lerp3f(bottomLeft, bottomRight, autoSmooth1f(pointInQuadX))
        return lerp3f(line1, line2, autoSmooth1f(pointInQuadZ))
    }

}