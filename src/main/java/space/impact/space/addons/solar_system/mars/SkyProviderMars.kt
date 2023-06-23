package space.impact.space.addons.solar_system.mars

import cpw.mods.fml.client.FMLClientHandler
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.client.renderer.GLAllocation
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.util.MathHelper
import net.minecraft.util.ResourceLocation
import net.minecraft.util.Vec3
import net.minecraftforge.client.IRenderHandler
import org.lwjgl.opengl.GL11
import space.impact.space.MODID
import space.impact.space.api.world.render.SkyProviderBase.Companion.andromedaTexture
import java.util.*
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class SkyProviderMars(marsProvider : WorldProviderMars) : IRenderHandler() {

    private var starList: Int
    private var glSkyList: Int
    private var glSkyList2: Int
    private val sunSize: Float

    init {
        sunSize = 17.5f * marsProvider.getSolarSize()
        val displayLists = GLAllocation.generateDisplayLists(3)
        starList = displayLists
        glSkyList = displayLists + 1
        glSkyList2 = displayLists + 2
        GL11.glPushMatrix()
        GL11.glNewList(starList, 4864)
        renderStars()
        GL11.glEndList()
        GL11.glPopMatrix()
        val tessellator = Tessellator.instance
        GL11.glNewList(glSkyList, 4864)
        var f = 16.0f
        var i1: Int
        var k: Int = -384
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

    override fun render(partialTicks: Float, world: WorldClient, mc: Minecraft) {
        GL11.glDisable(3553)
        GL11.glDisable(32826)
        RenderHelper.enableStandardItemLighting()
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
        var f18 = world.getStarBrightness(partialTicks)
        if (f18 > 0.0f) {
            GL11.glPushMatrix()
            GL11.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f)
            GL11.glRotatef(world.getCelestialAngle(partialTicks) * 360.0f, 1.0f, 0.0f, 0.0f)
            GL11.glRotatef(-19.0f, 0.0f, 1.0f, 0.0f)
            GL11.glColor4f(f18, f18, f18, f18)
            GL11.glCallList(starList)
            GL11.glPopMatrix()
        }
        val time = world.worldTime % 24000L
        val afloat = FloatArray(4)
        GL11.glDisable(3553)
        GL11.glShadeModel(7425)
        GL11.glPushMatrix()
        GL11.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f)
        GL11.glRotatef(world.getCelestialAngle(partialTicks) * 360.0f, 1.0f, 0.0f, 0.0f)
        if (time < 100L) {
            afloat[0] = 0.73333335f
            afloat[1] = 0.5058824f
            afloat[2] = 0.83137256f
        } else if ((time <= 100L || time >= 500L) && (time <= 11000L || time >= 11500L)) {
            if ((time <= 500L || time >= 1000L) && (time <= 10500L || time >= 11000L)) {
                afloat[0] = 1.0f
                afloat[1] = 0.7607843f
                afloat[2] = 0.7058824f
            } else {
                afloat[0] = 0.8901961f
                afloat[1] = 0.8f
                afloat[2] = 0.92941177f
            }
        } else {
            afloat[0] = 0.8039216f
            afloat[1] = 0.6431373f
            afloat[2] = 0.87058824f
        }
        afloat[3] = 0.3f
        f6 = afloat[0]
        f7 = afloat[1]
        var f8 = afloat[2]
        var f9: Float
        var f10: Float
        if (mc.gameSettings.anaglyph) {
            f9 = (f6 * 30.0f + f7 * 59.0f + f8 * 11.0f) / 100.0f
            f10 = (f6 * 30.0f + f7 * 70.0f) / 100.0f
            val f11 = (f6 * 30.0f + f8 * 70.0f) / 100.0f
            f6 = f9
            f7 = f10
            f8 = f11
        }
        f18 = 1.0f - f18
        tessellator1.startDrawing(6)
        tessellator1.setColorRGBA_F(f6 * f18, f7 * f18, f8 * f18, afloat[3] * 2.0f / f18)
        tessellator1.addVertex(0.0, 100.0, 0.0)
        tessellator1.setColorRGBA_F(afloat[0] * f18, afloat[1] * f18, afloat[2] * f18, 0.0f)
        f10 = 20.0f
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
        tessellator1.startDrawing(6)
        tessellator1.setColorRGBA_F(f6 * f18, f7 * f18, f8 * f18, afloat[3] * f18)
        tessellator1.addVertex(0.0, 100.0, 0.0)
        tessellator1.setColorRGBA_F(afloat[0] * f18, afloat[1] * f18, afloat[2] * f18, 0.0f)
        f10 = 40.0f
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
        GL11.glPopMatrix()
        GL11.glShadeModel(7424)
        GL11.glEnable(3553)
        OpenGlHelper.glBlendFunc(770, 1, 1, 0)
        GL11.glPushMatrix()
        f7 = 0.0f
        f8 = 0.0f
        f9 = 0.0f
        GL11.glTranslatef(f7, f8, f9)
        GL11.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f)
        GL11.glRotatef(world.getCelestialAngle(partialTicks) * 360.0f, 1.0f, 0.0f, 0.0f)
        GL11.glDisable(3553)
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f)
        f10 = sunSize / 3.5f
        tessellator1.startDrawingQuads()
        tessellator1.addVertex((-f10).toDouble(), 99.9, (-f10).toDouble())
        tessellator1.addVertex(f10.toDouble(), 99.9, (-f10).toDouble())
        tessellator1.addVertex(f10.toDouble(), 99.9, f10.toDouble())
        tessellator1.addVertex((-f10).toDouble(), 99.9, f10.toDouble())
        tessellator1.draw()
        GL11.glEnable(3553)
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        f10 = sunSize
        mc.renderEngine.bindTexture(sunTexture)
        tessellator1.startDrawingQuads()
        tessellator1.addVertexWithUV((-f10).toDouble(), 100.0, (-f10).toDouble(), 0.0, 0.0)
        tessellator1.addVertexWithUV(f10.toDouble(), 100.0, (-f10).toDouble(), 1.0, 0.0)
        tessellator1.addVertexWithUV(f10.toDouble(), 100.0, f10.toDouble(), 1.0, 1.0)
        tessellator1.addVertexWithUV((-f10).toDouble(), 100.0, f10.toDouble(), 0.0, 1.0)
        tessellator1.draw()
        f10 = 0.5f
        GL11.glScalef(0.6f, 0.6f, 0.6f)
        GL11.glRotatef(40.0f, 0.0f, 0.0f, 1.0f)
        GL11.glRotatef(200.0f, 1.0f, 0.0f, 0.0f)
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        FMLClientHandler.instance().client.renderEngine.bindTexture(overworldTexture)
        tessellator1.startDrawingQuads()
        tessellator1.addVertexWithUV((-f10).toDouble(), -100.0, f10.toDouble(), 0.0, 1.0)
        tessellator1.addVertexWithUV(f10.toDouble(), -100.0, f10.toDouble(), 1.0, 1.0)
        tessellator1.addVertexWithUV(f10.toDouble(), -100.0, (-f10).toDouble(), 1.0, 0.0)
        tessellator1.addVertexWithUV((-f10).toDouble(), -100.0, (-f10).toDouble(), 0.0, 0.0)
        tessellator1.draw()
        f10 = 3.5f
        GL11.glScalef(0.6f, 0.6f, 0.6f)
        GL11.glRotatef(60.0f, 0.0f, 0.0f, 1.0f)
        GL11.glRotatef(360.0f, 1.0f, 0.0f, 0.0f)
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        FMLClientHandler.instance().client.renderEngine.bindTexture(phobosTexture)
        tessellator1.startDrawingQuads()
        tessellator1.addVertexWithUV((-f10).toDouble(), -100.0, f10.toDouble(), 0.0, 1.0)
        tessellator1.addVertexWithUV(f10.toDouble(), -100.0, f10.toDouble(), 1.0, 1.0)
        tessellator1.addVertexWithUV(f10.toDouble(), -100.0, (-f10).toDouble(), 1.0, 0.0)
        tessellator1.addVertexWithUV((-f10).toDouble(), -100.0, (-f10).toDouble(), 0.0, 0.0)
        tessellator1.draw()
        f10 = 1.5f
        GL11.glScalef(0.6f, 0.6f, 0.6f)
        GL11.glRotatef(80.0f, 0.0f, 0.0f, 1.0f)
        GL11.glRotatef(20.0f, 1.0f, 0.0f, 0.0f)
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        FMLClientHandler.instance().client.renderEngine.bindTexture(deimosTexture)
        tessellator1.startDrawingQuads()
        tessellator1.addVertexWithUV((-f10).toDouble(), -100.0, f10.toDouble(), 0.0, 1.0)
        tessellator1.addVertexWithUV(f10.toDouble(), -100.0, f10.toDouble(), 1.0, 1.0)
        tessellator1.addVertexWithUV(f10.toDouble(), -100.0, (-f10).toDouble(), 1.0, 0.0)
        tessellator1.addVertexWithUV((-f10).toDouble(), -100.0, (-f10).toDouble(), 0.0, 0.0)
        tessellator1.draw()
        GL11.glPopMatrix()
        GL11.glPushMatrix()
        f10 = 3.0f
        GL11.glScalef(0.6f, 0.6f, 0.6f)
        GL11.glRotatef(145.0f, 0.0f, 1.0f, 0.0f)
        GL11.glRotatef(-135.0f, 1.0f, 0.0f, 0.0f)
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        FMLClientHandler.instance().client.renderEngine.bindTexture(andromedaTexture)
        tessellator1.startDrawingQuads()
        tessellator1.addVertexWithUV((-f10).toDouble(), -100.0, f10.toDouble(), 0.0, 1.0)
        tessellator1.addVertexWithUV(f10.toDouble(), -100.0, f10.toDouble(), 1.0, 1.0)
        tessellator1.addVertexWithUV(f10.toDouble(), -100.0, (-f10).toDouble(), 1.0, 0.0)
        tessellator1.addVertexWithUV((-f10).toDouble(), -100.0, (-f10).toDouble(), 0.0, 0.0)
        tessellator1.draw()
        GL11.glPopMatrix()
        GL11.glPushMatrix()
        GL11.glDisable(3553)
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        GL11.glDisable(3042)
        GL11.glEnable(3008)
        GL11.glEnable(2912)
        GL11.glPopMatrix()
        GL11.glDisable(3553)
        GL11.glColor3f(0.0f, 0.0f, 0.0f)
        val d0 = mc.thePlayer.getPosition(partialTicks).yCoord - world.horizon
        if (d0 < 0.0) {
            GL11.glPushMatrix()
            GL11.glTranslatef(0.0f, 12.0f, 0.0f)
            GL11.glCallList(glSkyList2)
            GL11.glPopMatrix()
            f8 = 1.0f
            f9 = -(d0 + 65.0).toFloat()
            f10 = -f8
            tessellator1.startDrawingQuads()
            tessellator1.setColorRGBA_I(0, 255)
            tessellator1.addVertex((-f8).toDouble(), f9.toDouble(), f8.toDouble())
            tessellator1.addVertex(f8.toDouble(), f9.toDouble(), f8.toDouble())
            tessellator1.addVertex(f8.toDouble(), f10.toDouble(), f8.toDouble())
            tessellator1.addVertex((-f8).toDouble(), f10.toDouble(), f8.toDouble())
            tessellator1.addVertex((-f8).toDouble(), f10.toDouble(), (-f8).toDouble())
            tessellator1.addVertex(f8.toDouble(), f10.toDouble(), (-f8).toDouble())
            tessellator1.addVertex(f8.toDouble(), f9.toDouble(), (-f8).toDouble())
            tessellator1.addVertex((-f8).toDouble(), f9.toDouble(), (-f8).toDouble())
            tessellator1.addVertex(f8.toDouble(), f10.toDouble(), (-f8).toDouble())
            tessellator1.addVertex(f8.toDouble(), f10.toDouble(), f8.toDouble())
            tessellator1.addVertex(f8.toDouble(), f9.toDouble(), f8.toDouble())
            tessellator1.addVertex(f8.toDouble(), f9.toDouble(), (-f8).toDouble())
            tessellator1.addVertex((-f8).toDouble(), f9.toDouble(), (-f8).toDouble())
            tessellator1.addVertex((-f8).toDouble(), f9.toDouble(), f8.toDouble())
            tessellator1.addVertex((-f8).toDouble(), f10.toDouble(), f8.toDouble())
            tessellator1.addVertex((-f8).toDouble(), f10.toDouble(), (-f8).toDouble())
            tessellator1.addVertex((-f8).toDouble(), f10.toDouble(), (-f8).toDouble())
            tessellator1.addVertex((-f8).toDouble(), f10.toDouble(), f8.toDouble())
            tessellator1.addVertex(f8.toDouble(), f10.toDouble(), f8.toDouble())
            tessellator1.addVertex(f8.toDouble(), f10.toDouble(), (-f8).toDouble())
            tessellator1.draw()
        }
        if (world.provider.isSkyColored) {
            GL11.glColor3f(f1 * 0.2f + 0.04f, f2 * 0.2f + 0.04f, f3 * 0.6f + 0.1f)
        } else {
            GL11.glColor3f(f1, f2, f3)
        }
        GL11.glPushMatrix()
        GL11.glTranslatef(0.0f, -(d0 - 16.0).toFloat(), 0.0f)
        GL11.glCallList(glSkyList2)
        GL11.glPopMatrix()
        GL11.glEnable(3553)
        GL11.glDepthMask(true)
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
                val var14 = var4 * 100.0
                val var16 = var6 * 100.0
                val var18 = var8 * 100.0
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
                    val var39 = 0.0
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
        var var3 = 1.0f - (MathHelper.sin(var2 * 3.1415927f * 2.0f) * 2.0f + 0.25f)
        if (var3 < 0.0f) {
            var3 = 0.0f
        }
        if (var3 > 1.0f) {
            var3 = 1.0f
        }
        return var3 * var3 * 1.0f
    }

    companion object {
        private val overworldTexture: ResourceLocation = ResourceLocation(MODID, "textures/gui/sol/earth.png")
        private val phobosTexture: ResourceLocation = ResourceLocation(MODID, "textures/gui/sol/moons/phobos.png")
        private val deimosTexture: ResourceLocation = ResourceLocation(MODID, "textures/gui/sol/moons/deimos.png")
        private val sunTexture = ResourceLocation("textures/environment/sun.png")
    }
}