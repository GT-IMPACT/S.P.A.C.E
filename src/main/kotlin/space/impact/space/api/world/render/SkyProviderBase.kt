package space.impact.space.api.world.render

import cpw.mods.fml.client.FMLClientHandler
import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.client.renderer.GLAllocation
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.MathHelper
import net.minecraft.util.ResourceLocation
import net.minecraft.util.Vec3
import net.minecraft.world.WorldProviderSurface
import net.minecraftforge.client.IRenderHandler
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.GL_BLEND
import org.lwjgl.opengl.GL11.GL_TEXTURE_2D
import space.impact.space.ASSETS
import space.impact.space.api.world.gen.world.SpaceProvider
import space.impact.space.api.world.world_math.Vector3
import space.impact.space.config.Config
import space.impact.space.utils.world.RenderUtils
import java.util.*
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

abstract class SkyProviderBase : IRenderHandler() {
    private val planetToRender: ResourceLocation
    var starList: Int
    var glSkyList: Int
    var glSkyList2: Int
    private val sunSize = 0f
    protected var ticks = 0f
    var afloat: FloatArray
    protected var mc: Minecraft

    override fun render(partialTicks: Float, world: WorldClient, mc: Minecraft) {
        ticks = partialTicks
        GL11.glDisable(GL_TEXTURE_2D)
        val vec3 = world.getSkyColor(mc.renderViewEntity, partialTicks)
        var f1 = vec3.xCoord.toFloat()
        var f2 = vec3.yCoord.toFloat()
        var f3 = vec3.zCoord.toFloat()
        var f6: Float
        var f7: Float
        if (mc.gameSettings.anaglyph) {
            val f4 = (f1 * 30.0f + f2 * 59.0f + f3 * 11.0f) / 100.0f
            f7 = (f1 * 30.0f + f2 * 70.0f) / 100.0f
            f6 = (f1 * 30.0f + f3 * 70.0f) / 100.0f
            f1 = f4
            f2 = f7
            f3 = f6
        }
        GL11.glColor3f(f1, f2, f3)
        val tessellator1 = Tessellator.instance
        GL11.glDepthMask(false)
        GL11.glEnable(2912)
        GL11.glColor3f(f1, f2, f3)
        GL11.glCallList(glSkyList)
        GL11.glDisable(2912)
        GL11.glDisable(3008)
        GL11.glEnable(3042)
        OpenGlHelper.glBlendFunc(770, 771, 1, 0)
        RenderHelper.disableStandardItemLighting()
        var f9: Float
        var f10: Float
        val rain = 1.0f - world.getRainStrength(partialTicks)
        var f18 = world.getStarBrightness(partialTicks) * rain
        if (f18 > 0.0f && !inWater(this.mc.thePlayer)) {
            GL11.glPushMatrix()
            GL11.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f)
            GL11.glRotatef(world.getCelestialAngle(partialTicks) * 360.0f, 1.0f, 0.0f, 0.0f)
            GL11.glRotatef(-19.0f, 0.0f, 1.0f, 0.0f)
            GL11.glColor4f(f18, f18, f18, f18)
            GL11.glCallList(starList)
            GL11.glPopMatrix()
        }
        GL11.glPushMatrix()
        GL11.glDisable(GL_TEXTURE_2D)
        GL11.glShadeModel(7425)
        afloat[0] = 1.0f
        afloat[1] = 0.7607843f
        afloat[2] = 0.7058824f
        afloat[3] = 0.3f
        val color = colorSunAura()
        if (color != null) {
            afloat[0] = color.x.toInt().toFloat() / 255.0f
            afloat[1] = color.y.toInt().toFloat() / 255.0f
            afloat[2] = color.z.toInt().toFloat() / 255.0f
        }
        f6 = afloat[0]
        f7 = afloat[1]
        var f8 = afloat[2]
        if (mc.gameSettings.anaglyph) {
            f9 = (f6 * 30.0f + f7 * 59.0f + f8 * 11.0f) / 100.0f
            f10 = (f6 * 30.0f + f7 * 70.0f) / 100.0f
            val f11 = (f6 * 30.0f + f8 * 70.0f) / 100.0f
        }
        f18 = 1.0f - f18
        GL11.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f)
        GL11.glRotatef(180.0f, 1.0f, 0.0f, 0.0f)
        GL11.glRotatef(this.mc.theWorld.getCelestialAngle(ticks) * 360.0f, 1.0f, 0.0f, 0.0f)
        f10 = sunSize() + 5.5f
        if (!inWater(this.mc.thePlayer)) {
            renderSunAura(tessellator1, f10 + addSizeAura().toFloat(), f18)
        }
        GL11.glPopMatrix()
        GL11.glShadeModel(7424)
        GL11.glEnable(3042)
        GL11.glEnable(GL_TEXTURE_2D)
        OpenGlHelper.glBlendFunc(770, 1, 1, 0)
        GL11.glPushMatrix()
        f7 = 0.0f
        f8 = 0.0f
        f9 = 0.0f
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f - Minecraft.getMinecraft().theWorld.getRainStrength(partialTicks))
        GL11.glTranslatef(f7, f8, f9)
        GL11.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f)
        GL11.glRotatef(world.getCelestialAngle(partialTicks) * 360.0f, 1.0f, 0.0f, 0.0f)
        if (modeLight() != 2 && !inWater(this.mc.thePlayer)) {
            f10 = sunSize()
            val calendar = Calendar.getInstance()
            if ((calendar[2] + 1 != 10 || calendar[5] < 30 || calendar[5] > 31) && (calendar[2] + 1 != 11 || calendar[5] > 1)) {
                if (sunImage() != null) {
                    FMLClientHandler.instance().client.renderEngine.bindTexture(sunImage())
                } else {
                    FMLClientHandler.instance().client.renderEngine.bindTexture(sunTexture)
                }
            } else {
                FMLClientHandler.instance().client.renderEngine.bindTexture(pumpkinsunTexture)
            }
            if (enableSmoothRender()) {
                GL11.glDisable(3042)
            }
            tessellator1.startDrawingQuads()
            tessellator1.addVertexWithUV((-f10).toDouble(), 100.0, (-f10).toDouble(), 0.0, 0.0)
            tessellator1.addVertexWithUV(f10.toDouble(), 100.0, (-f10).toDouble(), 1.0, 0.0)
            tessellator1.addVertexWithUV(f10.toDouble(), 100.0, f10.toDouble(), 1.0, 1.0)
            tessellator1.addVertexWithUV((-f10).toDouble(), 100.0, f10.toDouble(), 0.0, 1.0)
            tessellator1.draw()
            if (enableSmoothRender()) {
                GL11.glEnable(3042)
            }
        }
        var light: Float
        if (enableBaseImages() && !inWater(this.mc.thePlayer)) {
            if (Minecraft.getMinecraft().theWorld.provider is WorldProviderSurface) {
                f10 = 20.0f
                FMLClientHandler.instance().client.renderEngine.bindTexture(moonTexture)
                val k = Minecraft.getMinecraft().theWorld.moonPhase
                val l = k % 4
                val i1 = k / 4 % 2
                val f14 = (l + 0).toFloat() / 4.0f
                val f15 = (i1 + 0).toFloat() / 2.0f
                val f16 = (l + 1).toFloat() / 4.0f
                val f17 = (i1 + 1).toFloat() / 2.0f
                tessellator1.startDrawingQuads()
                tessellator1.addVertexWithUV((-f10).toDouble(), -100.0, f10.toDouble(), f16.toDouble(), f17.toDouble())
                tessellator1.addVertexWithUV(f10.toDouble(), -100.0, f10.toDouble(), f14.toDouble(), f17.toDouble())
                tessellator1.addVertexWithUV(f10.toDouble(), -100.0, (-f10).toDouble(), f14.toDouble(), f15.toDouble())
                tessellator1.addVertexWithUV((-f10).toDouble(), -100.0, (-f10).toDouble(), f16.toDouble(), f15.toDouble())
                tessellator1.draw()
            }
            light = 0.0f
            if (modeLight() == 0) {
                light = FMLClientHandler.instance().client.theWorld.getStarBrightness(1.0f) - Minecraft.getMinecraft().theWorld.getRainStrength(partialTicks)
            }
            if (modeLight() == 1) {
                light = 1.0f
            }

            renderImage(lmcTexture, -90.0f, 90.0f, 0.0f, 45.0f, true, light)
            renderImage(smcTexture, 0.0f, -40.0f, 0.0f, 30.0f, true, light)
            renderImage(andromedaTexture, 100.0f, -150.0f, 0.0f, 30.0f, true, light)
            renderImage(barnardaloopTexture, 200.0f, -70.0f, 0.0f, 40.0f, true, light)
            GL11.glDisable(3042)
            rendererSky(tessellator1, f10, partialTicks)


        }
        GL11.glPopMatrix()
        GL11.glPushMatrix()
        if (mc.thePlayer.posY.toFloat() > 200.0f && enableRenderPlanet()) {
            GL11.glEnable(GL_TEXTURE_2D)
            light = Math.round(mc.thePlayer.posY - 200.0).toFloat() / 10.0f
            f10 = 116.0f - Math.min(light, 115.0f)
            GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f)
            GL11.glRotatef(-360.0f, 1.0f, 0.0f, 0.0f)

            if (Config.isEnabledSupportCircleTexturePlanet) {
                GL11.glPushAttrib(GL11.GL_ENABLE_BIT)
                GL11.glEnable(GL11.GL_BLEND)
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            }

            if (mc.theWorld.provider !is WorldProviderSurface) {
                mc.renderEngine.bindTexture((mc.theWorld.provider as SpaceProvider).getCelestialBody().getBodyIcon())
            } else {
                mc.renderEngine.bindTexture(planetToRender)
            }
            GL11.glColor4f(light / 10.0f, light / 10.0f, light / 10.0f, 1.0f)
            tessellator1.startDrawingQuads()
            tessellator1.addVertexWithUV((-f10).toDouble(), -100.0, f10.toDouble(), 0.0, 1.0)
            tessellator1.addVertexWithUV(f10.toDouble(), -100.0, f10.toDouble(), 1.0, 1.0)
            tessellator1.addVertexWithUV(f10.toDouble(), -100.0, (-f10).toDouble(), 1.0, 0.0)
            tessellator1.addVertexWithUV((-f10).toDouble(), -100.0, (-f10).toDouble(), 0.0, 0.0)
            tessellator1.draw()

            if (Config.isEnabledSupportCircleTexturePlanet) {
                GL11.glPopAttrib()
            } else {
                renderAtmo(tessellator1, -360.0f, 0.0f, f10 - 5.0f, getAtmosphereColor())
            }
        }
        GL11.glDisable(GL_TEXTURE_2D)
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        GL11.glDisable(3042)
        GL11.glEnable(3008)
        GL11.glEnable(2912)
        GL11.glPopMatrix()
        GL11.glDisable(GL_TEXTURE_2D)
        GL11.glColor3f(0.0f, 0.0f, 0.0f)
        val d0 = mc.thePlayer.getPosition(partialTicks).yCoord - world.horizon
        if (world.provider.isSkyColored) {
            GL11.glColor3f(f1 * 0.2f + 0.04f, f2 * 0.2f + 0.04f, f3 * 0.6f + 0.1f)
        } else {
            GL11.glColor3f(f1, f2, f3)
        }
        GL11.glEnable(GL_TEXTURE_2D)
        GL11.glDepthMask(true)
    }

    private fun inWater(player: EntityPlayer): Boolean {
        val vec = player.getPosition(1.0f)
        return mc.theWorld.getBlock(vec.xCoord.toInt(), vec.yCoord.toInt(), vec.zCoord.toInt()).material == Material.water
    }

    protected fun renderImage(
        image: ResourceLocation?, x: Float, y: Float, z: Float, f10: Float, withsun: Boolean, alpha: Float = FMLClientHandler.instance().client.theWorld.getStarBrightness(1.0f)
    ) {
        GL11.glEnable(GL_BLEND)
        if (!withsun) {
            GL11.glPopMatrix()
            GL11.glPushMatrix()
        }
        val tessellator1 = Tessellator.instance
        GL11.glRotatef(x, 0.0f, 1.0f, 0.0f)
        GL11.glRotatef(y, 1.0f, 0.0f, 0.0f)
        GL11.glRotatef(z, 0.0f, 0.0f, 1.0f)
        GL11.glColor4f(1.0f, 1.0f, 1.0f, alpha)

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

        FMLClientHandler.instance().client.renderEngine.bindTexture(image)
        tessellator1.startDrawingQuads()
        tessellator1.addVertexWithUV((-f10).toDouble(), -100.0, f10.toDouble(), 0.0, 1.0)
        tessellator1.addVertexWithUV(f10.toDouble(), -100.0, f10.toDouble(), 1.0, 1.0)
        tessellator1.addVertexWithUV(f10.toDouble(), -100.0, (-f10).toDouble(), 1.0, 0.0)
        tessellator1.addVertexWithUV((-f10).toDouble(), -100.0, (-f10).toDouble(), 0.0, 0.0)
        tessellator1.draw()

        GL11.glPopAttrib()
    }

    protected fun renderAtmo(tessellator1: Tessellator, x: Float, y: Float, f10: Float, vec: Vector3?) {
        if (vec != null && !Config.isEnabledSupportCircleTexturePlanet) {
            GL11.glEnable(GL_BLEND)
            GL11.glDisable(GL_TEXTURE_2D)
            GL11.glBlendFunc(770, 771)
            GL11.glRotatef(y, 0.0f, 0.0f, 1.0f)
            GL11.glRotatef(x, 1.0f, 0.0f, 0.0f)
            val planetOrbitalDistance = 5.0
            val dist = -64.0 - 4.0 * planetOrbitalDistance / 2.0 - (mc.thePlayer.posY.toFloat() / mc.thePlayer.posY.toFloat()).toDouble()
            val scalingMult = 1.0 - 0.9 * planetOrbitalDistance
            val Xoffset = (System.currentTimeMillis().toDouble() / 1000000.0 % 1.0).toFloat()
            val f14 = 1.0f + Xoffset
            val f15 = 0.0f + Xoffset
            val color = floatArrayOf(vec.x.toFloat(), vec.y.toFloat(), vec.z.toFloat())
            tessellator1.startDrawingQuads()
            tessellator1.setColorRGBA_F(color[0], color[1], color[2], 0.09f)
            for (i in 0..4) {
                renderTestWithUV(
                    tessellator1, dist + i.toDouble() * scalingMult, (-f10).toDouble(), (-f10).toDouble(), 0.0, 0.0, f14.toDouble(), f15.toDouble(), f15.toDouble(), f14.toDouble()
                )
                renderTestWithUV(
                    tessellator1, dist + i.toDouble() * scalingMult, 0.0, 0.0, f10.toDouble(), f10.toDouble(), f14.toDouble(), f15.toDouble(), f15.toDouble(), f14.toDouble()
                )
                renderTestWithUV(
                    tessellator1, dist + i.toDouble() * scalingMult, (-f10).toDouble(), 0.0, 0.0, f10.toDouble(), f14.toDouble(), f15.toDouble(), f15.toDouble(), f14.toDouble()
                )
                renderTestWithUV(
                    tessellator1, dist + i.toDouble() * scalingMult, 0.0, (-f10).toDouble(), f10.toDouble(), 0.0, f14.toDouble(), f15.toDouble(), f15.toDouble(), f14.toDouble()
                )
            }
            tessellator1.draw()
        }
    }

    protected fun renderSunAura(tessellator1: Tessellator, f10: Float, f18: Float) {
        var f10 = f10
        GL11.glRotatef(180.0f, 1.0f, 0.0f, 0.0f)
        val vec3 = mc.theWorld.getSkyColor(mc.renderViewEntity, ticks)
        val f1 = vec3.xCoord.toFloat()
        val f2 = vec3.yCoord.toFloat()
        val f3 = vec3.zCoord.toFloat()
        var f9 = 0.0f
        var f6: Float
        if (mc.gameSettings.anaglyph) {
            val f4 = (f1 * 30.0f + f2 * 59.0f + f3 * 11.0f) / 100.0f
            val f5 = (f1 * 30.0f + f2 * 70.0f) / 100.0f
            f6 = (f1 * 30.0f + f3 * 70.0f) / 100.0f
        }
        afloat[0] = 1.0f
        afloat[1] = 0.7607843f
        afloat[2] = 0.7058824f
        afloat[3] = 0.3f
        val color = colorSunAura()
        if (color != null) {
            afloat[0] = color.x.toInt().toFloat() / 255.0f
            afloat[1] = color.y.toInt().toFloat() / 255.0f
            afloat[2] = color.z.toInt().toFloat() / 255.0f
        }
        f6 = afloat[0]
        var f7 = afloat[1]
        var f8 = afloat[2]
        if (mc.gameSettings.anaglyph) {
            f9 = (f6 * 30.0f + f7 * 59.0f + f8 * 11.0f) / 100.0f
            f10 = (f6 * 30.0f + f7 * 70.0f) / 100.0f
            val f11 = (f6 * 30.0f + f8 * 70.0f) / 100.0f
            f6 = f9
            f7 = f10
            f8 = f11
        }
        GL11.glDisable(GL_TEXTURE_2D)
        GL11.glShadeModel(7425)
        GL11.glEnable(3042)
        if (modeLight() != 2) {
            tessellator1.startDrawing(6)
            tessellator1.setColorRGBA_F(f6 * f18, f7 * f18, f8 * f18, afloat[3] * 2.0f / f18 - Minecraft.getMinecraft().theWorld.getRainStrength(ticks))
            tessellator1.addVertex(0.0, 100.0, 0.0)
            tessellator1.setColorRGBA_F(afloat[0] * f18, afloat[1] * f18, afloat[2] * f18, 0.0f)
            tessellator1.addVertex((-f10).toDouble(), 100.0, (-f10).toDouble())
            tessellator1.addVertex(0.0, 100.0, (-f10).toDouble() * 1.5)
            tessellator1.addVertex(f10.toDouble(), 100.0, (-f10).toDouble())
            tessellator1.addVertex(f10.toDouble() * 1.5, 100.0, 0.0)
            tessellator1.addVertex(f10.toDouble(), 100.0, f10.toDouble())
            tessellator1.addVertex(0.0, 100.0, f10.toDouble() * 1.5)
            tessellator1.addVertex((-f10).toDouble(), 100.0, f10.toDouble())
            tessellator1.addVertex((-f10).toDouble() * 1.5, 100.0, 0.0)
            tessellator1.addVertex((-f10).toDouble(), 100.0, (-f10).toDouble())
            tessellator1.draw()
        }
        if (enableSmoothRender()) {
            GL11.glLineWidth(2.0f)
            GL11.glPolygonMode(1032, 6913)
        }
        if (enableLargeSunAura()) {
            tessellator1.startDrawing(6)
            if (enableSmoothRender()) {
                tessellator1.setColorRGBA_F(f6 * f18, f7 * f18, f8 * f18, afloat[3] * 2.0f / f18 - Minecraft.getMinecraft().theWorld.getRainStrength(ticks))
            } else {
                tessellator1.setColorRGBA_F(f6 * f18, f7 * f18, f8 * f18, afloat[3] * f18)
            }
            tessellator1.addVertex(0.0, 100.0, 0.0)
            tessellator1.setColorRGBA_F(afloat[0] * f18, afloat[1] * f18, afloat[2] * f18, 0.0f)
            f10 += 10.0f
            val i = if (enableSmoothRender()) 8 else 0
            f10 += i.toFloat()
            tessellator1.addVertex((-f10).toDouble(), 100.0, (-f10).toDouble())
            tessellator1.addVertex(0.0, 100.0, (-f10).toDouble() * 1.5)
            f10 -= (i + i).toFloat()
            tessellator1.addVertex(f10.toDouble(), 100.0, (-f10).toDouble())
            tessellator1.addVertex(f10.toDouble() * 1.5, 100.0, 0.0)
            f10 += i.toFloat()
            tessellator1.addVertex(f10.toDouble(), 100.0, f10.toDouble())
            tessellator1.addVertex(0.0, 100.0, f10.toDouble() * 1.5)
            f10 -= i.toFloat()
            tessellator1.addVertex((-f10).toDouble(), 100.0, f10.toDouble())
            tessellator1.addVertex((-f10).toDouble() * 1.5, 100.0, 0.0)
            tessellator1.addVertex((-f10).toDouble(), 100.0, (-f10).toDouble())
            tessellator1.draw()
        }
        GL11.glPolygonMode(1032, 6914)
        GL11.glEnable(GL_TEXTURE_2D)
        GL11.glDisable(3042)
    }

    private fun renderStars() {
        val rand = Random(10842L)
        val var2 = Tessellator.instance
        var2.startDrawingQuads()
        var starIndex = 0
        while (starIndex < 6000) {
            var var4 = (rand.nextFloat() * 2.0f - 1.0f).toDouble()
            var var6 = (rand.nextFloat() * 2.0f - 1.0f).toDouble()
            var var8 = (rand.nextFloat() * 2.0f - 1.0f).toDouble()
            val var10 = (0.15f + rand.nextFloat() * 0.1f).toDouble()
            var var12 = var4 * var4 + var6 * var6 + var8 * var8
            if (var12 < 1.0 && var12 > 0.01) {
                var12 = 1.0 / sqrt(var12)
                var4 *= var12
                var6 *= var12
                var8 *= var12
                val var14 = var4 * rand.nextDouble() * 150.0 + 130.0
                val var16 = var6 * rand.nextDouble() * 150.0 + 130.0
                val var18 = var8 * rand.nextDouble() * 150.0 + 130.0
                val var20 = atan2(var4, var8)
                val var22 = sin(var20)
                val var24 = cos(var20)
                val var26 = atan2(sqrt(var4 * var4 + var8 * var8), var6)
                val var28 = sin(var26)
                val var30 = cos(var26)
                val var32 = rand.nextDouble() * Math.PI * 2.0
                val var34 = sin(var32)
                val var36 = cos(var32)
                for (var38 in 0..3) {
                    val var41 = ((var38 and 2) - 1).toDouble() * var10
                    val var43 = ((var38 + 1 and 2) - 1).toDouble() * var10
                    val var47 = var41 * var36 - var43 * var34
                    val var49 = var43 * var36 + var41 * var34
                    val var53 = var47 * var28 + 0.0 * var30
                    val var55 = 0.0 * var28 - var47 * var30
                    val var57 = var55 * var22 - var49 * var24
                    val var61 = var49 * var22 + var55 * var24
                    var2.addVertex(var14 + var57, var16 + var53, var18 + var61)
                }
            }
            ++starIndex
        }
        var2.draw()
    }

    private fun getCustomSkyColor(): Vec3 {
        return Vec3.createVectorHelper(0.26796875, 0.1796875, 0.0)
    }

    fun getSkyBrightness(par1: Float): Float {
        val var2 = FMLClientHandler.instance().client.theWorld.getCelestialAngle(par1)
        var var3 = 1.0f - (MathHelper.sin(var2 * Math.PI.toFloat() * 2.0f) * 2.0f + 0.25f)
        if (var3 < 0.0f) {
            var3 = 0.0f
        }
        if (var3 > 1.0f) {
            var3 = 1.0f
        }
        return var3 * var3 * 1.0f
    }

    protected abstract fun rendererSky(tess: Tessellator, f10: Float, partialTicks: Float)
    protected abstract fun modeLight(): Int
    protected abstract fun enableBaseImages(): Boolean
    protected abstract fun sunSize(): Float
    protected abstract fun sunImage(): ResourceLocation?
    protected abstract fun enableStar(): Boolean
    protected abstract fun colorSunAura(): Vector3?
    protected abstract fun getAtmosphereColor(): Vector3?

    fun enableLargeSunAura(): Boolean {
        return true
    }

    open fun enableSmoothRender(): Boolean {
        return false
    }

    fun enableRenderPlanet(): Boolean {
        return true
    }

    fun addSizeAura(): Int {
        return 0
    }

    protected fun getCelestialAngle(daylength: Long): Float {
        return RenderUtils.calculateCelestialAngle(mc.theWorld.worldTime, ticks, daylength.toInt().toFloat()) * 360.0f
    }

    init {
        planetToRender = ResourceLocation(ASSETS, "textures/gui/celestialbodies/earth.png")
        afloat = FloatArray(4)
        mc = Minecraft.getMinecraft()
        val displayLists = GLAllocation.generateDisplayLists(3)
        starList = displayLists
        glSkyList = displayLists + 1
        glSkyList2 = displayLists + 2
        GL11.glPushMatrix()
        GL11.glNewList(starList, 4864)
        if (enableStar()) {
            renderStars()
        }
        GL11.glEndList()
        GL11.glPopMatrix()
        val tessellator = Tessellator.instance
        GL11.glNewList(glSkyList, 4864)
        var f = 16.0f
        var k: Int
        var i1: Int
        k = -384
        while (k <= 384) {
            i1 = -384
            while (i1 <= 384) {
                tessellator.startDrawingQuads()
                tessellator.addVertex((k + 0).toDouble(), f.toDouble(), (i1 + 0).toDouble())
                tessellator.addVertex((k + 64).toDouble(), f.toDouble(), (i1 + 0).toDouble())
                tessellator.addVertex((k + 64).toDouble(), f.toDouble(), (i1 + 64).toDouble())
                tessellator.addVertex((k + 0).toDouble(), f.toDouble(), (i1 + 64).toDouble())
                tessellator.draw()
                i1 += 64
            }
            k += 64
        }
        GL11.glEndList()
        GL11.glNewList(glSkyList2, 4864)
        f = -16.0f
        tessellator.startDrawingQuads()
        k = -384
        while (k <= 384) {
            i1 = -384
            while (i1 <= 384) {
                tessellator.addVertex((k + 64).toDouble(), f.toDouble(), (i1 + 0).toDouble())
                tessellator.addVertex((k + 0).toDouble(), f.toDouble(), (i1 + 0).toDouble())
                tessellator.addVertex((k + 0).toDouble(), f.toDouble(), (i1 + 64).toDouble())
                tessellator.addVertex((k + 64).toDouble(), f.toDouble(), (i1 + 64).toDouble())
                i1 += 64
            }
            k += 64
        }
        tessellator.draw()
        GL11.glEndList()
    }

    companion object {
        private val sunTexture = ResourceLocation("textures/environment/sun.png")
        private val lmcTexture: ResourceLocation = ResourceLocation(ASSETS, "textures/environment/background/LMC.png")
        private val smcTexture: ResourceLocation = ResourceLocation(ASSETS, "textures/environment/background/SMC.png")
        private val andromedaTexture: ResourceLocation = ResourceLocation(ASSETS, "textures/environment/background/Andromeda.png")
        private val moonTexture: ResourceLocation = ResourceLocation("textures/environment/moon_phases.png")
        private val barnardaloopTexture: ResourceLocation = ResourceLocation(ASSETS, "textures/environment/background/BarnardaLoop.png")
        val pumpkinsunTexture: ResourceLocation = ResourceLocation(ASSETS, "textures/environment/pumkinsun.png")

        fun renderTestWithUV(tess: Tessellator, yMax: Double, xMin: Double, zMin: Double, xMax: Double, zMax: Double, uMin: Double, uMax: Double, vMin: Double, vMax: Double) {
            tess.setNormal(0.0f, 1.0f, 0.0f)
            tess.addVertexWithUV(xMin, yMax, zMin, uMin, vMin)
            tess.addVertexWithUV(xMin, yMax, zMax, uMin, vMax)
            tess.addVertexWithUV(xMax, yMax, zMax, uMax, vMax)
            tess.addVertexWithUV(xMax, yMax, zMin, uMax, vMin)
        }
    }
}