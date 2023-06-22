package space.impact.space.api.world.bodies

import net.minecraft.util.ResourceLocation
import space.impact.space.api.world.atmosphere.AtmosphericGas
import space.impact.space.api.world.world_math.ScalableDistance

interface CelestialBody : Comparable<CelestialBody> {

    fun getAtmosphere(): List<AtmosphericGas>

    fun getLocalizedName(): String

    fun getDimensionID(): Int

    fun getRelativeDistanceFromCenter(): ScalableDistance

    fun getBodyIcon(): ResourceLocation

    override fun compareTo(other: CelestialBody): Int {
        val thisDistance = getRelativeDistanceFromCenter()
        val otherDistance = other.getRelativeDistanceFromCenter()
        return when {
            otherDistance.unscale < thisDistance.unscale -> 1
            otherDistance.unscale > thisDistance.unscale -> -1
            else -> 0
        }
    }
}