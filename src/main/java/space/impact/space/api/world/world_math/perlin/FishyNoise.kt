package space.impact.space.api.world.world_math.perlin

import java.util.*


class FishyNoise(seed: Long) {
    var perm = IntArray(512)
    var grad2d = arrayOf(
        floatArrayOf(1f, 0f),
        floatArrayOf(.9239f, .3827f),
        floatArrayOf(.707107f, 0.707107f),
        floatArrayOf(.3827f, .9239f),
        floatArrayOf(0f, 1f),
        floatArrayOf(-.3827f, .9239f),
        floatArrayOf(-.707107f, 0.707107f),
        floatArrayOf(-.9239f, .3827f),
        floatArrayOf(-1f, 0f),
        floatArrayOf(-.9239f, -.3827f),
        floatArrayOf(-.707107f, -0.707107f),
        floatArrayOf(-.3827f, -.9239f),
        floatArrayOf(0f, -1f),
        floatArrayOf(.3827f, -.9239f),
        floatArrayOf(.707107f, -0.707107f),
        floatArrayOf(.9239f, -.3827f)
    )
    var grad3d = arrayOf(
        intArrayOf(1, 1, 0),
        intArrayOf(-1, 1, 0),
        intArrayOf(1, -1, 0),
        intArrayOf(-1, -1, 0),
        intArrayOf(1, 0, 1),
        intArrayOf(-1, 0, 1),
        intArrayOf(1, 0, -1),
        intArrayOf(-1, 0, -1),
        intArrayOf(0, 1, 1),
        intArrayOf(0, -1, 1),
        intArrayOf(0, 1, -1),
        intArrayOf(0, -1, -1),
        intArrayOf(1, 1, 0),
        intArrayOf(-1, 1, 0),
        intArrayOf(0, -1, 1),
        intArrayOf(0, -1, -1)
    )

    init {
        val rand = Random(seed)
        for (i in 0..255) {
            perm[i] = i // Fill up the random array with numbers 0-256
        }
        // Shuffle those numbers for the random effect
        for (i in 0..255) {
            val j = rand.nextInt(256)
            perm[i] = perm[i] xor perm[j]
            perm[j] = perm[i] xor perm[j]
            perm[i] = perm[i] xor perm[j]
        }
        System.arraycopy(perm, 0, perm, 256, 256)
    }

    fun noise2d(xx: Float, yy: Float): Float {
        var x = xx
        var y = yy
        var largeX = if (x > 0) x.toInt() else x.toInt() - 1
        var largeY = if (y > 0) y.toInt() else y.toInt() - 1
        x -= largeX.toFloat()
        y -= largeY.toFloat()
        largeX = largeX and 255
        largeY = largeY and 255
        val u = x * x * x * (x * (x * 6 - 15) + 10)
        val v = y * y * y * (y * (y * 6 - 15) + 10)
        val randY = perm[largeY] + largeX
        val randY1 = perm[largeY + 1] + largeX
        var grad2 = grad2d[perm[randY] and 15]
        val grad00 = grad2[0] * x + grad2[1] * y
        grad2 = grad2d[perm[randY1] and 15]
        val grad01 = grad2[0] * x + grad2[1] * (y - 1)
        grad2 = grad2d[perm[1 + randY1] and 15]
        val grad11 = grad2[0] * (x - 1) + grad2[1] * (y - 1)
        grad2 = grad2d[perm[1 + randY] and 15]
        val grad10 = grad2[0] * (x - 1) + grad2[1] * y
        val lerpX0 = grad00 + u * (grad10 - grad00)
        return lerpX0 + v * (grad01 + u * (grad11 - grad01) - lerpX0)
    }

    fun noise3d(xx: Float, yy: Float, zz: Float): Float {
        var x = xx
        var y = yy
        var z = zz
        var unitX = if (x > 0) x.toInt() else x.toInt() - 1
        var unitY = if (y > 0) y.toInt() else y.toInt() - 1
        var unitZ = if (z > 0) z.toInt() else z.toInt() - 1
        x -= unitX.toFloat()
        y -= unitY.toFloat()
        z -= unitZ.toFloat()
        unitX = unitX and 255
        unitY = unitY and 255
        unitZ = unitZ and 255
        val u = x * x * x * (x * (x * 6 - 15) + 10)
        val v = y * y * y * (y * (y * 6 - 15) + 10)
        val w = z * z * z * (z * (z * 6 - 15) + 10)
        val randZ = perm[unitZ] + unitY
        val randZ1 = perm[unitZ + 1] + unitY
        val randYZ = perm[randZ] + unitX
        val randY1Z = perm[1 + randZ] + unitX
        val randYZ1 = perm[randZ1] + unitX
        val randY1Z1 = perm[1 + randZ1] + unitX
        var grad3 = grad3d[perm[randYZ] and 15]
        val grad000 = grad3[0] * x + grad3[1] * y + grad3[2] * z
        grad3 = grad3d[perm[1 + randYZ] and 15]
        val grad100 = grad3[0] * (x - 1) + grad3[1] * y + grad3[2] * z
        grad3 = grad3d[perm[randY1Z] and 15]
        val grad010 = grad3[0] * x + grad3[1] * (y - 1) + grad3[2] * z
        grad3 = grad3d[perm[1 + randY1Z] and 15]
        val grad110 = grad3[0] * (x - 1) + grad3[1] * (y - 1) + grad3[2] * z
        z--
        grad3 = grad3d[perm[randYZ1] and 15]
        val grad001 = grad3[0] * x + grad3[1] * y + grad3[2] * z
        grad3 = grad3d[perm[1 + randYZ1] and 15]
        val grad101 = grad3[0] * (x - 1) + grad3[1] * y + grad3[2] * z
        grad3 = grad3d[perm[randY1Z1] and 15]
        val grad011 = grad3[0] * x + grad3[1] * (y - 1) + grad3[2] * z
        grad3 = grad3d[perm[1 + randY1Z1] and 15]
        val grad111 = grad3[0] * (x - 1) + grad3[1] * (y - 1) + grad3[2] * z
        val f1 = grad000 + u * (grad100 - grad000)
        val f2 = grad010 + u * (grad110 - grad010)
        val f3 = grad001 + u * (grad101 - grad001)
        val f4 = grad011 + u * (grad111 - grad011)
        val lerp1 = f1 + v * (f2 - f1)
        return lerp1 + w * (f3 + v * (f4 - f3) - lerp1)
    }
}