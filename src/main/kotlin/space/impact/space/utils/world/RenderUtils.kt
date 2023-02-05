package space.impact.space.utils.world

import cpw.mods.fml.client.FMLClientHandler
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.util.MathHelper
import net.minecraft.util.Vec3
import net.minecraft.world.World
import net.minecraftforge.client.ForgeHooksClient
import space.impact.space.addons.solar_system.earth.moons.moon.world.WorldProviderMoon
import space.impact.space.api.world.world_math.Vector3
import kotlin.math.cos

object RenderUtils {

    fun calculateCelestialAngle(worldtime: Long, ticks: Float, daylenght: Float): Float {
        val j = (worldtime.toFloat() % daylenght).toInt()
        var f1 = (j.toFloat() + ticks) / daylenght - 0.25f
        if (f1 < 0.0f) {
            ++f1
        }
        if (f1 > 1.0f) {
            --f1
        }
        val f2 = f1
        f1 = 1.0f - ((cos(f1.toDouble() * Math.PI) + 1.0) / 2.0).toFloat()
        f1 = f2 + (f1 - f2) / 3.0f
        return f1
    }

    @SideOnly(Side.CLIENT)
    fun getWorldBrightness(world: WorldClient): Float {
        if (world.provider is WorldProviderMoon) {
            val f1 = world.getCelestialAngle(1.0f)
            var f2 = 1.0f - (MathHelper.cos(f1 * Math.PI.toFloat() * 2.0f) * 2.0f + 0.2f)
            if (f2 < 0.0f) {
                f2 = 0.0f
            }
            if (f2 > 1.0f) {
                f2 = 1.0f
            }
            f2 = 1.0f - f2
            return f2 * 0.8f
        }
        return world.getSunBrightness(1.0f)
    }

    fun getColorRed(world: World?): Float {
        return getWorldColor(world).x.toFloat()
    }

    fun getColorGreen(world: World?): Float {
        return getWorldColor(world).y.toFloat()
    }

    fun getColorBlue(world: World?): Float {
        return getWorldColor(world).z.toFloat()
    }

    fun getFogColorHook(world: World): Vec3? {
        return world.getFogColor(1.0f)
    }

    fun getWorldColor(world: World?): Vector3 {
        return Vector3(1, 1, 1)
    }

    fun getSkyColorHook(world: World): Vec3? {
        return world.getSkyColor(FMLClientHandler.instance().client.renderViewEntity, 1.0f)
    }



}