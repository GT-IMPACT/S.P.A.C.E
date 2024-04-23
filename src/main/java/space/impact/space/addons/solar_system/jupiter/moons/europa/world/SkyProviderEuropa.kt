package space.impact.space.addons.solar_system.jupiter.moons.europa.world

import cpw.mods.fml.client.FMLClientHandler
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.util.MathHelper
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.GL_TEXTURE_2D
import space.impact.space.MODID
import space.impact.space.api.world.gen.world.SpaceWorldProviderBaseProvider
import space.impact.space.api.world.render.SkyProviderBase
import space.impact.space.api.world.world_math.Vector3
import space.impact.space.config.Config
import java.util.*
import kotlin.math.sin


class SkyProviderEuropa : SkyProviderBase() {

    companion object {
        private val jupiterTexture: ResourceLocation = ResourceLocation(MODID, "textures/gui/sol/jupiter${if (Config.isEnabledSupportHDTexturePlanet) "_hd" else ""}.png")
        private val ioTexture: ResourceLocation = ResourceLocation(MODID, "textures/gui/sol/moons/io${if (Config.isEnabledSupportHDTexturePlanet) "_hd" else ""}.png")
        private val ganymedeTexture: ResourceLocation = ResourceLocation(MODID, "textures/gui/sol/moons/ganymede${if (Config.isEnabledSupportHDTexturePlanet) "_hd" else ""}.png")
        private val callistoTexture: ResourceLocation = ResourceLocation(MODID, "textures/gui/sol/moons/callisto${if (Config.isEnabledSupportHDTexturePlanet) "_hd" else ""}.png")
    }

    var isIoFirst = false

    private val dayLength: Long = (mc.theWorld.provider as? SpaceWorldProviderBaseProvider)?.getDayLength() ?: 1

    private fun renderJupiter(tess: Tessellator) = renderPlanet {
        val sizePlanet = 135.0f
        GL11.glScalef(.6f, .6f, .6f)
        GL11.glRotatef(0.0f, 0.0f, 1.0f, 0.0f)
        GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f)
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        GL11.glEnable(GL11.GL_ALPHA_TEST)

        FMLClientHandler.instance().client.renderEngine.bindTexture(jupiterTexture)

        tess.startDrawingQuads()
        tess.addVertexWithUV(-sizePlanet.toDouble(), -100.0, sizePlanet.toDouble(), 0.0, 1.0)
        tess.addVertexWithUV(sizePlanet.toDouble(), -100.0, sizePlanet.toDouble(), 1.0, 1.0)
        tess.addVertexWithUV(sizePlanet.toDouble(), -100.0, -sizePlanet.toDouble(), 1.0, 0.0)
        tess.addVertexWithUV(-sizePlanet.toDouble(), -100.0, -sizePlanet.toDouble(), 0.0, 0.0)
        tess.draw()

        GL11.glDisable(GL11.GL_ALPHA_TEST)

        val atmosphereScale = 0.9f
        renderAtmo(
            tess, 0.0f, 0.0f, sizePlanet * 5f,
            Vector3((0.47058824f * atmosphereScale).toDouble(), (0.43137255f * atmosphereScale).toDouble(), (0.47058824f * atmosphereScale).toDouble())
        )
    }

    private fun renderGanymede(tess: Tessellator) = renderPlanet {
        val sizePlanet = 5.5f
        GL11.glScalef(0.6f, 0.6f, 0.6f)
        GL11.glRotatef(0.0f, 0.0f, 0.0f, 1.0f)
        GL11.glRotatef(getCelestialAngle(dayLength / 2L), 0.0f, 1.0f, 0.0f)
        GL11.glRotatef(100.0f, 1.0f, 0.0f, 0.0f)
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        GL11.glEnable(GL11.GL_ALPHA_TEST)

        FMLClientHandler.instance().client.renderEngine.bindTexture(ganymedeTexture)
        tess.startDrawingQuads()
        tess.addVertexWithUV((-sizePlanet).toDouble(), -100.0, sizePlanet.toDouble(), 0.0, 1.0)
        tess.addVertexWithUV(sizePlanet.toDouble(), -100.0, sizePlanet.toDouble(), 1.0, 1.0)
        tess.addVertexWithUV(sizePlanet.toDouble(), -100.0, (-sizePlanet).toDouble(), 1.0, 0.0)
        tess.addVertexWithUV((-sizePlanet).toDouble(), -100.0, (-sizePlanet).toDouble(), 0.0, 0.0)
        tess.draw()

        GL11.glDisable(GL11.GL_ALPHA_TEST)

    }

    private fun renderCallisto(tess: Tessellator) = renderPlanet {
        val sizePlanet = 3.5f
        GL11.glScalef(0.6f, 0.6f, 0.6f)
        GL11.glRotatef(0.0f, 0.0f, 0.0f, 1.0f)
        GL11.glRotatef(getCelestialAngle(dayLength * 2L), 0.0f, 1.0f, 0.0f)
        GL11.glRotatef(100.0f, 1.0f, 0.0f, 0.0f)
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        GL11.glEnable(GL11.GL_ALPHA_TEST)

        FMLClientHandler.instance().client.renderEngine.bindTexture(callistoTexture)
        tess.startDrawingQuads()
        tess.addVertexWithUV((-sizePlanet).toDouble(), -100.0, sizePlanet.toDouble(), 0.0, 1.0)
        tess.addVertexWithUV(sizePlanet.toDouble(), -100.0, sizePlanet.toDouble(), 1.0, 1.0)
        tess.addVertexWithUV(sizePlanet.toDouble(), -100.0, (-sizePlanet).toDouble(), 1.0, 0.0)
        tess.addVertexWithUV((-sizePlanet).toDouble(), -100.0, (-sizePlanet).toDouble(), 0.0, 0.0)
        tess.draw()

        GL11.glDisable(GL11.GL_ALPHA_TEST)
    }

    private fun renderIo(tess: Tessellator, partialTicks: Float) = renderPlanet {
        val ioPosition = (50.0 * sin((mc.theWorld.getCelestialAngle(partialTicks) * 360.0f / 10.0f).toDouble())).toFloat()
        when {
            ioPosition >= 49.99f -> isIoFirst = false
            ioPosition <= -49.99f -> isIoFirst = true
        }
        val sizePlanet = 8.0f
        GL11.glRotatef(ioPosition, 0.0f, 1.0f, 0.0f)
        GL11.glRotatef(100.0f, 1.0f, 0.0f, 0.0f)
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        GL11.glEnable(GL11.GL_ALPHA_TEST)

        FMLClientHandler.instance().client.renderEngine.bindTexture(ioTexture)
        tess.startDrawingQuads()
        tess.addVertexWithUV((-sizePlanet).toDouble(), -100.0, sizePlanet.toDouble(), 0.0, 1.0)
        tess.addVertexWithUV(sizePlanet.toDouble(), -100.0, sizePlanet.toDouble(), 1.0, 1.0)
        tess.addVertexWithUV(sizePlanet.toDouble(), -100.0, (-sizePlanet).toDouble(), 1.0, 0.0)
        tess.addVertexWithUV((-sizePlanet).toDouble(), -100.0, (-sizePlanet).toDouble(), 0.0, 0.0)
        tess.draw()

        GL11.glDisable(GL11.GL_ALPHA_TEST)
    }


    override fun rendererSky(tess: Tessellator, f10: Float, partialTicks: Float) {
        GL11.glPushMatrix()
        renderGanymede(tess)
        renderCallisto(tess)
        if (!this.isIoFirst) renderJupiter(tess)
        renderIo(tess, partialTicks)
        if (this.isIoFirst) renderJupiter(tess)
        GL11.glPopMatrix()
    }

    override fun enableBaseImages(): Boolean {
        return true
    }

    override fun sunSize(): Float {
        return 5.0f
    }

    override fun enableStar(): Boolean {
        return true
    }

    override fun sunImage(): ResourceLocation {
        return ResourceLocation(MODID, "textures/gui/sun_blank.png")
    }

    override fun modeLight(): Int {
        return 0
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
}