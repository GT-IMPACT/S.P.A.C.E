package space.impact.space.addons.solar_system.venus.world

import net.minecraft.client.renderer.Tessellator
import net.minecraft.util.ResourceLocation
import space.impact.space.api.world.render.SkyProviderBase
import space.impact.space.api.world.world_math.Vector3

class SkyProviderVenus : SkyProviderBase() {

    override fun rendererSky(tessellator: Tessellator, f10: Float, partialTicks: Float) {}

    override fun enableBaseImages(): Boolean {
        return false
    }

    override fun sunSize(): Float {
        return 25.5f
    }

    override fun enableStar(): Boolean {
        return false
    }

    override fun sunImage(): ResourceLocation? {
        return null
    }

    override fun modeLight(): Int {
        return 2
    }

    override fun colorSunAura(): Vector3? {
        return null
    }

    override fun getAtmosphereColor(): Vector3 {
        val f = 0.5f
        return Vector3((0.35686275f * f).toDouble(), (0.23921569f * f).toDouble(), (0.03137255f * f).toDouble())
    }
}