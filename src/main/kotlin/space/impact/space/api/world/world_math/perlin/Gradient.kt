package space.impact.space.api.world.world_math.perlin

import java.util.*

class Gradient(seed: Long, val numOctaves: Int, val persistence: Float) : NoiseModule() {

    private val noiseGen: FishyNoise
    private val offsetX: Float
    private val offsetY: Float
    private val offsetZ: Float

    init {
        val rand = Random(seed)
        offsetX = rand.nextFloat() / 2 + 0.01f
        offsetY = rand.nextFloat() / 2 + 0.01f
        offsetZ = rand.nextFloat() / 2 + 0.01f
        noiseGen = FishyNoise(seed)
    }

    override fun getNoise(xx: Float): Float {
        var i = xx
        i *= frequencyX
        var value = 0f
        var curAmplitude: Float = this.amplitude
        for (n in 0 until numOctaves) {
            value += noiseGen.noise2d(i + offsetX, offsetY) * curAmplitude
            i *= 2f
            curAmplitude *= this.persistence
        }
        return value
    }

    override fun getNoise(xx: Float, yy: Float): Float {
        var i = xx
        var j = yy
        if (numOctaves == 1) {
            return (noiseGen.noise2d(i * frequencyX + offsetX, j * frequencyY + offsetY) * this.amplitude)
        }
        i *= frequencyX
        j *= frequencyY
        var value = 0f
        var curAmplitude: Float = this.amplitude
        for (n in 0 until numOctaves) {
            value += noiseGen.noise2d(i + offsetX, j + offsetY) * curAmplitude
            i *= 2f
            j *= 2f
            curAmplitude *= this.persistence
        }
        return value
    }

    override fun getNoise(xx: Float, yy: Float, zz: Float): Float {
        var i = xx
        var j = yy
        var k = zz
        if (numOctaves == 1) {
            return noiseGen.noise3d(i * frequencyX + offsetX, j * frequencyY + offsetY, k * frequencyZ + offsetZ) * this.amplitude
        }
        i *= frequencyX
        j *= frequencyY
        k *= frequencyZ
        var value = 0f
        var curAmplitude: Float = this.amplitude
        for (n in 0 until numOctaves) {
            value += noiseGen.noise3d(i + offsetX, j + offsetY, k + offsetZ) * curAmplitude
            i *= 2f
            j *= 2f
            k *= 2f
            curAmplitude *= this.persistence
        }
        return value
    }
}