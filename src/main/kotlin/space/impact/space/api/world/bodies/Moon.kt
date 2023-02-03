package space.impact.space.api.world.bodies

import net.minecraft.util.ResourceLocation
import space.impact.space.ASSETS
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

    override fun getBodyIcon(): ResourceLocation = ResourceLocation(ASSETS, "textures/gui/sol/moons/$name.png")
}
