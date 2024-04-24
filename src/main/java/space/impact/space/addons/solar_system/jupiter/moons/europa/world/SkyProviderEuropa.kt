package space.impact.space.addons.solar_system.jupiter.moons.europa.world

import cpw.mods.fml.client.FMLClientHandler
import net.minecraft.client.renderer.Tessellator
import org.lwjgl.opengl.GL11
import space.impact.space.api.world.gen.world.SpaceWorldProviderBaseProvider
import space.impact.space.api.world.render.SkyProviderBase
import space.impact.space.api.world.render.SkyRenderManager.callistoTexture
import space.impact.space.api.world.render.SkyRenderManager.ganymedeTexture
import space.impact.space.api.world.render.SkyRenderManager.ioTexture
import space.impact.space.api.world.render.SkyRenderManager.jupiterTexture
import space.impact.space.api.world.render.SkyRenderManager.renderPlanet
import kotlin.math.sin

class SkyProviderEuropa : SkyProviderBase() {

    private var isIoFirst = false
    private val dayLength: Long = (mc.theWorld.provider as? SpaceWorldProviderBaseProvider)?.getDayLength() ?: 1

    override fun doRenderCustom(tess: Tessellator, partialTicks: Float) {
        GL11.glPushMatrix()
        renderGanymede(tess)
        renderCallisto(tess)
        if (!isIoFirst) renderJupiter(tess)
        renderIo(tess, partialTicks)
        if (isIoFirst) renderJupiter(tess)
        GL11.glPopMatrix()
    }

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
}
