package space.impact.space.api.world.bodies

import space.impact.space.api.world.atmosphere.AtmosphericGas
import space.impact.space.api.world.world_math.ScalableDistance

data class Moon(
    val name: String,
    val parent: CelestialBody? = null
): CelestialBody {

    override fun getAtmosphere(): List<AtmosphericGas> = emptyList()

    override fun getLocalizedName(): String = name

    override fun getDimensionID(): Int = 2

    override fun getRelativeDistanceFromCenter(): ScalableDistance = ScalableDistance(0f, 0f)
}
