package space.impact.space.addons.solar_system.earth.moons.moon.world

import cpw.mods.fml.client.FMLClientHandler
import net.minecraft.client.renderer.Tessellator
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import space.impact.space.MODID
import space.impact.space.api.world.render.SkyProviderBase
import space.impact.space.api.world.world_math.Vector3
import space.impact.space.config.Config

class SkyProviderMoon : SkyProviderBase() {
    override fun rendererSky(tess: Tessellator, f10: Float, partialTicks: Float) {
        GL11.glPopMatrix()
        GL11.glPushMatrix()

        val earthRotation = (mc.theWorld.spawnPoint.posZ.toDouble() - mc.thePlayer.posZ - 20000.0).toFloat() * 0.01f
        GL11.glScalef(0.6f, 0.6f, 0.6f)
        GL11.glRotatef(earthRotation, 1.0f, 0.0f, 0.0f)
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)

        if (Config.isEnabledSupportCircleTexturePlanet) {
            GL11.glPushAttrib(GL11.GL_ENABLE_BIT)
            GL11.glEnable(GL11.GL_BLEND)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        }

        FMLClientHandler.instance().client.renderEngine.bindTexture(earthTexture)

        val sizePlanet = 40.0f
        tess.startDrawingQuads()
        tess.addVertexWithUV((-sizePlanet).toDouble(), -100.0, sizePlanet.toDouble(), 0.0, 0.75)
        tess.addVertexWithUV(sizePlanet.toDouble(), -100.0, sizePlanet.toDouble(), 0.75, 0.75)
        tess.addVertexWithUV(sizePlanet.toDouble(), -100.0, (-sizePlanet).toDouble(), 0.75, 0.0)
        tess.addVertexWithUV((-sizePlanet).toDouble(), -100.0, (-sizePlanet).toDouble(), 0.0, 0.0)
        tess.draw()

        if (Config.isEnabledSupportCircleTexturePlanet) {
            GL11.glPopAttrib()
        } else {
            val f = 0.59f
            renderAtmo(tess, 0.0f, 0.0f, sizePlanet - 0.5f, Vector3((0.34509805f * f).toDouble(), (0.47843137f * f).toDouble(), (0.7058824f * f).toDouble()))
        }
    }

    override fun modeLight(): Int {
        return 1
    }

    override fun enableBaseImages(): Boolean {
        return true
    }

    override fun sunSize(): Float {
        return 6.0f
    }

    override fun sunImage(): ResourceLocation {
        return ResourceLocation(MODID, "textures/gui/sun_blank.png")
    }

    override fun enableStar(): Boolean {
        return true
    }

    override fun colorSunAura(): Vector3 {
        return Vector3(150.0, 150.0, 150.0)
    }

    override fun getAtmosphereColor(): Vector3? {
        return null
    }

    override fun enableSmoothRender(): Boolean {
        return true
    }

    companion object {
        private val earthTexture: ResourceLocation = ResourceLocation(MODID, "textures/gui/sol/earth.png")
    }
}