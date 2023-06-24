package space.impact.space.api.world.bodies

import net.minecraft.util.ResourceLocation
import space.impact.space.MODID
import space.impact.space.api.world.atmosphere.AtmosphericGas
import space.impact.space.api.world.world_math.ScalableDistance

class Orbit(
    val dimId: Int,
    val name: String,
    val parent: CelestialBody? = null,
) : CelestialBody {

    override fun getAtmosphere(): List<AtmosphericGas> = emptyList()
    override fun getLocalizedName(): String = name
    override fun getDimensionID(): Int = dimId
    override fun getRelativeDistanceFromCenter(): ScalableDistance = ScalableDistance(0f, 0f)
    override fun getBodyIcon(): ResourceLocation = ResourceLocation(MODID, "textures/gui/sol/orbit/$name.png")
}