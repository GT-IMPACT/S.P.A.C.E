package space.impact.space.addons.solar_system.jupiter.moons.europa.world

import cpw.mods.fml.client.FMLClientHandler
import net.minecraft.client.renderer.Tessellator
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.GL_TEXTURE_2D
import space.impact.space.ASSETS
import space.impact.space.api.world.gen.world.SpaceWorldProviderBase
import space.impact.space.api.world.render.SkyProviderBase
import space.impact.space.api.world.world_math.Vector3
import space.impact.space.config.Config
import kotlin.math.sin

class SkyProviderEuropa : SkyProviderBase() {

    companion object {
        private val jupiterTexture: ResourceLocation = ResourceLocation(ASSETS, "textures/gui/sol/jupiter.png")
        private val ioTexture: ResourceLocation = ResourceLocation(ASSETS, "textures/gui/sol/moons/io.png")
        private val ganymedeTexture: ResourceLocation = ResourceLocation(ASSETS, "textures/gui/sol/moons/ganymede.png")
        private val callistoTexture: ResourceLocation = ResourceLocation(ASSETS, "textures/gui/sol/moons/callisto.png")
    }

    var test = false
    var wait = 5

    override fun rendererSky(tessellator: Tessellator, f10: Float, partialTicks: Float) {
        var f10 = f10
        val daylength: Long = (mc.theWorld.provider as SpaceWorldProviderBase).getDayLength()
        GL11.glPopMatrix()
        GL11.glPushMatrix()
        f10 = 5.5f
        GL11.glScalef(0.6f, 0.6f, 0.6f)
        GL11.glRotatef(0.0f, 0.0f, 0.0f, 1.0f)
        GL11.glRotatef(getCelestialAngle(daylength / 2L), 0.0f, 1.0f, 0.0f)
        GL11.glRotatef(100.0f, 1.0f, 0.0f, 0.0f)
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)

        if (Config.isEnabledSupportCircleTexturePlanet) {
            GL11.glPushAttrib(GL11.GL_ENABLE_BIT)
            GL11.glEnable(GL11.GL_BLEND)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        }

        FMLClientHandler.instance().client.renderEngine.bindTexture(ganymedeTexture)
        tessellator.startDrawingQuads()
        tessellator.addVertexWithUV((-f10).toDouble(), -100.0, f10.toDouble(), 0.0, 1.0)
        tessellator.addVertexWithUV(f10.toDouble(), -100.0, f10.toDouble(), 1.0, 1.0)
        tessellator.addVertexWithUV(f10.toDouble(), -100.0, (-f10).toDouble(), 1.0, 0.0)
        tessellator.addVertexWithUV((-f10).toDouble(), -100.0, (-f10).toDouble(), 0.0, 0.0)
        tessellator.draw()

        if (Config.isEnabledSupportCircleTexturePlanet) {
            GL11.glPopAttrib()
        }

        GL11.glPopMatrix()
        GL11.glPushMatrix()
        f10 = 3.5f
        GL11.glScalef(0.6f, 0.6f, 0.6f)
        GL11.glRotatef(0.0f, 0.0f, 0.0f, 1.0f)
        GL11.glRotatef(getCelestialAngle(daylength * 2L), 0.0f, 1.0f, 0.0f)
        GL11.glRotatef(100.0f, 1.0f, 0.0f, 0.0f)
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)

        if (Config.isEnabledSupportCircleTexturePlanet) {
            GL11.glPushAttrib(GL11.GL_ENABLE_BIT)
            GL11.glEnable(GL11.GL_BLEND)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        }

        FMLClientHandler.instance().client.renderEngine.bindTexture(callistoTexture)
        tessellator.startDrawingQuads()
        tessellator.addVertexWithUV((-f10).toDouble(), -100.0, f10.toDouble(), 0.0, 1.0)
        tessellator.addVertexWithUV(f10.toDouble(), -100.0, f10.toDouble(), 1.0, 1.0)
        tessellator.addVertexWithUV(f10.toDouble(), -100.0, (-f10).toDouble(), 1.0, 0.0)
        tessellator.addVertexWithUV((-f10).toDouble(), -100.0, (-f10).toDouble(), 0.0, 0.0)
        tessellator.draw()

        if (Config.isEnabledSupportCircleTexturePlanet) {
            GL11.glPopAttrib()
        }

        GL11.glPopMatrix()
        val x = (40.0 * sin((mc.theWorld.getCelestialAngle(ticks) * 360.0f / 10.0f).toDouble())).toFloat()
        if (this.wait == 0 && (x >= 39.99f || x <= -39.99f)) {
            this.wait = 150
            this.test = !this.test
        }
        if (this.wait > 0) {
            --this.wait
        }
        var f: Float
        if (!this.test) {
            GL11.glPushMatrix()
            f10 = 100.0f
            GL11.glScalef(.6f, .6f, .6f)
            GL11.glRotatef(0.0f, 0.0f, 1.0f, 0.0f)
            GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f)
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)

            if (Config.isEnabledSupportCircleTexturePlanet) {
                GL11.glPushAttrib(GL11.GL_ENABLE_BIT)
                GL11.glEnable(GL11.GL_BLEND)
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            }

            FMLClientHandler.instance().client.renderEngine.bindTexture(jupiterTexture)
            tessellator.startDrawingQuads()
            tessellator.addVertexWithUV((-f10).toDouble(), -100.0, f10.toDouble(), 0.0, 1.0)
            tessellator.addVertexWithUV(f10.toDouble(), -100.0, f10.toDouble(), 1.0, 1.0)
            tessellator.addVertexWithUV(f10.toDouble(), -100.0, (-f10).toDouble(), 1.0, 0.0)
            tessellator.addVertexWithUV((-f10).toDouble(), -100.0, (-f10).toDouble(), 0.0, 0.0)
            tessellator.draw()

            if (Config.isEnabledSupportCircleTexturePlanet) {
                GL11.glPopAttrib()
            }

            f = 0.9f
            renderAtmo(tessellator, 0.0f, 0.0f, f10 - 8.0f, Vector3((0.47058824f * f).toDouble(), (0.43137255f * f).toDouble(), (0.47058824f * f).toDouble()))

            GL11.glEnable(GL_TEXTURE_2D)

            GL11.glPopMatrix()
        }
        GL11.glPushMatrix()
        f10 = 8.0f
        GL11.glRotatef(x, 0.0f, 1.0f, 0.0f)
        GL11.glRotatef(100.0f, 1.0f, 0.0f, 0.0f)
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)

        if (Config.isEnabledSupportCircleTexturePlanet) {
            GL11.glPushAttrib(GL11.GL_ENABLE_BIT)
            GL11.glEnable(GL11.GL_BLEND)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        }

        FMLClientHandler.instance().client.renderEngine.bindTexture(ioTexture)
        tessellator.startDrawingQuads()
        tessellator.addVertexWithUV((-f10).toDouble(), -100.0, f10.toDouble(), 0.0, 1.0)
        tessellator.addVertexWithUV(f10.toDouble(), -100.0, f10.toDouble(), 1.0, 1.0)
        tessellator.addVertexWithUV(f10.toDouble(), -100.0, (-f10).toDouble(), 1.0, 0.0)
        tessellator.addVertexWithUV((-f10).toDouble(), -100.0, (-f10).toDouble(), 0.0, 0.0)
        tessellator.draw()

        if (Config.isEnabledSupportCircleTexturePlanet) {
            GL11.glPopAttrib()
        }

        GL11.glPopMatrix()

        if (this.test) {
            GL11.glPushMatrix()
            f10 = 100.0f
            GL11.glScalef(0.6f, 0.6f, 0.6f)
            GL11.glRotatef(0.0f, 0.0f, 1.0f, 0.0f)
            GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f)
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)

            if (Config.isEnabledSupportCircleTexturePlanet) {
                GL11.glPushAttrib(GL11.GL_ENABLE_BIT)
                GL11.glEnable(GL11.GL_BLEND)
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            }

            FMLClientHandler.instance().client.renderEngine.bindTexture(jupiterTexture)
            tessellator.startDrawingQuads()
            tessellator.addVertexWithUV((-f10).toDouble(), -100.0, f10.toDouble(), 0.0, 1.0)
            tessellator.addVertexWithUV(f10.toDouble(), -100.0, f10.toDouble(), 1.0, 1.0)
            tessellator.addVertexWithUV(f10.toDouble(), -100.0, (-f10).toDouble(), 1.0, 0.0)
            tessellator.addVertexWithUV((-f10).toDouble(), -100.0, (-f10).toDouble(), 0.0, 0.0)
            tessellator.draw()

            if (Config.isEnabledSupportCircleTexturePlanet) {
                GL11.glPopAttrib()
            }

            f = 0.9f
            renderAtmo(tessellator, 0.0f, 0.0f, f10 - 8.0f, Vector3((0.47058824f * f).toDouble(), (0.43137255f * f).toDouble(), (0.47058824f * f).toDouble()))


            GL11.glEnable(GL_TEXTURE_2D)
            GL11.glPopMatrix()
        }
        GL11.glPushMatrix()
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
        return ResourceLocation(ASSETS, "textures/gui/sun_blank.png")
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