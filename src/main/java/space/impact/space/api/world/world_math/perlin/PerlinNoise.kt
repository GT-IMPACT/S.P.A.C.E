package space.impact.space.api.world.world_math.perlin

import kotlin.math.abs

class PerlinNoise(
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
        val topLeft = rand.smartVecGen2d(squareStartX, squareStartZ)
        val topRight = rand.smartVecGen2d(squareStartX + xs, squareStartZ)
        val bottomLeft = rand.smartVecGen2d(squareStartX, squareStartZ + zs)
        val bottomRight = rand.smartVecGen2d(squareStartX + xs, squareStartZ + zs)
        val distanceToTopLeft = doubleArrayOf(xs * pointInQuadX, zs * pointInQuadZ)
        val distanceToTopRight = doubleArrayOf(xs * pointInQuadX - xs, zs * pointInQuadZ)
        val distanceToBottomLeft = doubleArrayOf(xs * pointInQuadX, zs * pointInQuadZ - zs)
        val distanceToBottomRight = doubleArrayOf(xs * pointInQuadX - xs, zs * pointInQuadZ - zs)
        val dotTopLeft = dot2d(topLeft!!, distanceToTopLeft)
        val dotTopRight = dot2d(topRight!!, distanceToTopRight)
        val dotBottomLeft = dot2d(bottomLeft!!, distanceToBottomLeft)
        val dotBottomRight = dot2d(bottomRight!!, distanceToBottomRight)
        val line1 = lerp3d(dotTopLeft, dotTopRight, autoSmooth1d(pointInQuadX))
        val line2 = lerp3d(dotBottomLeft, dotBottomRight, autoSmooth1d(pointInQuadX))
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
        val topLeft = rand.smartVecGen2f(squareStartX, squareStartZ)
        val topRight = rand.smartVecGen2f(squareStartX + xs, squareStartZ)
        val bottomLeft = rand.smartVecGen2f(squareStartX, squareStartZ + zs)
        val bottomRight = rand.smartVecGen2f(squareStartX + xs, squareStartZ + zs)
        val distanceToTopLeft = floatArrayOf(xs * pointInQuadX, zs * pointInQuadZ)
        val distanceToTopRight = floatArrayOf(xs * pointInQuadX - xs, zs * pointInQuadZ)
        val distanceToBottomLeft = floatArrayOf(xs * pointInQuadX, zs * pointInQuadZ - zs)
        val distanceToBottomRight = floatArrayOf(xs * pointInQuadX - xs, zs * pointInQuadZ - zs)
        val dotTopLeft = dot2f(topLeft!!, distanceToTopLeft)
        val dotTopRight = dot2f(topRight!!, distanceToTopRight)
        val dotBottomLeft = dot2f(bottomLeft!!, distanceToBottomLeft)
        val dotBottomRight = dot2f(bottomRight!!, distanceToBottomRight)
        val line1 = lerp3f(dotTopLeft, dotTopRight, autoSmooth1f(pointInQuadX))
        val line2 = lerp3f(dotBottomLeft, dotBottomRight, autoSmooth1f(pointInQuadX))
        return lerp3f(line1, line2, autoSmooth1f(pointInQuadZ))
    }

}