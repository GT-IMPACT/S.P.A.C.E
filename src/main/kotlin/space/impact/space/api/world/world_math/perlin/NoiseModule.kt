package space.impact.space.api.world.world_math.perlin

abstract class NoiseModule {

    var frequencyX: Float = 1f
    var frequencyY: Float = 1f
    var frequencyZ: Float = 1f
    var amplitude: Float = 1f

    abstract fun getNoise(xx: Float): Float

    abstract fun getNoise(xx: Float, yy: Float): Float

    abstract fun getNoise(xx: Float, yy: Float, zz: Float): Float

    open fun setFrequency(frequency: Float) {
        frequencyX = frequency
        frequencyY = frequency
        frequencyZ = frequency
    }
}
