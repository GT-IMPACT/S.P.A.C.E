package space.impact.space.addons.solar_system.earth.orbit

import cpw.mods.fml.client.FMLClientHandler
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.client.renderer.GLAllocation
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.util.MathHelper
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.IRenderHandler
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import space.impact.space.MODID
import java.util.*
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class SkyProviderOrbit(private val planetToRender: ResourceLocation, private val renderMoon: Boolean, private val renderSun: Boolean) : IRenderHandler() {
    private var starGLCallList = GLAllocation.generateDisplayLists(3)
    private var glSkyList: Int
    private var glSkyList2: Int
    private var spinAngle = 0f
    private var spinDeltaPerTick = 0f
    private var prevPartialTicks = 0f
    private var prevTick: Long = 0
    private val minecraft = FMLClientHandler.instance().client

    init {
        GL11.glPushMatrix()
        GL11.glNewList(starGLCallList, GL11.GL_COMPILE)
        renderStars()
        GL11.glEndList()
        GL11.glPopMatrix()
        val tes = Tessellator.instance
        glSkyList = starGLCallList + 1
        GL11.glNewList(glSkyList, GL11.GL_COMPILE)
        val byte2: Byte = 64
        val i = 256 / byte2 + 2
        var f = 16f
        var j = -byte2 * i
        while (j <= byte2 * i) {
            var l = -byte2 * i
            while (l <= byte2 * i) {
                tes.startDrawingQuads()
                tes.addVertex((j + 0).toDouble(), f.toDouble(), (l + 0).toDouble())
                tes.addVertex((j + byte2).toDouble(), f.toDouble(), (l + 0).toDouble())
                tes.addVertex((j + byte2).toDouble(), f.toDouble(), (l + byte2).toDouble())
                tes.addVertex((j + 0).toDouble(), f.toDouble(), (l + byte2).toDouble())
                tes.draw()
                l += byte2.toInt()
            }
            j += byte2.toInt()
        }
        GL11.glEndList()
        glSkyList2 = starGLCallList + 2
        GL11.glNewList(glSkyList2, GL11.GL_COMPILE)
        f = -16f
        tes.startDrawingQuads()
        var k = -byte2 * i
        while (k <= byte2 * i) {
            var i1 = -byte2 * i
            while (i1 <= byte2 * i) {
                tes.addVertex((k + byte2).toDouble(), f.toDouble(), (i1 + 0).toDouble())
                tes.addVertex((k + 0).toDouble(), f.toDouble(), (i1 + 0).toDouble())
                tes.addVertex((k + 0).toDouble(), f.toDouble(), (i1 + byte2).toDouble())
                tes.addVertex((k + byte2).toDouble(), f.toDouble(), (i1 + byte2).toDouble())
                i1 += byte2.toInt()
            }
            k += byte2.toInt()
        }
        tes.draw()
        GL11.glEndList()
    }

    override fun render(partialTicks: Float, world: WorldClient, mc: Minecraft) {
        val var20 = 400.0f + minecraft.thePlayer.posY.toFloat() / 2f
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        GL11.glDisable(GL12.GL_RESCALE_NORMAL)
        val var2 = minecraft.theWorld.getSkyColor(minecraft.renderViewEntity, partialTicks)
        var var3 = var2.xCoord.toFloat()
        var var4 = var2.yCoord.toFloat()
        var var5 = var2.zCoord.toFloat()
        var var8: Float
        if (minecraft.gameSettings.anaglyph) {
            val var6 = (var3 * 30.0f + var4 * 59.0f + var5 * 11.0f) / 100.0f
            val var7 = (var3 * 30.0f + var4 * 70.0f) / 100.0f
            var8 = (var3 * 30.0f + var5 * 70.0f) / 100.0f
            var3 = var6
            var4 = var7
            var5 = var8
        }
        GL11.glColor3f(var3, var4, var5)
        val var23 = Tessellator.instance
        GL11.glDepthMask(false)
        GL11.glEnable(GL11.GL_FOG)
        GL11.glColor3f(var3, var4, var5)
        GL11.glCallList(glSkyList)
        GL11.glDisable(GL11.GL_FOG)
        GL11.glDisable(GL11.GL_ALPHA_TEST)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        RenderHelper.disableStandardItemLighting()
        val var24 = minecraft.theWorld.provider
            .calcSunriseSunsetColors(minecraft.theWorld.getCelestialAngle(partialTicks), partialTicks)
        var var9: Float
        var var10: Float
        var var11: Float
        var var12: Float
        if (var24 != null) {
            GL11.glDisable(GL11.GL_TEXTURE_2D)
            GL11.glShadeModel(GL11.GL_SMOOTH)
            GL11.glPushMatrix()
            GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f)
            GL11.glRotatef(
                if (MathHelper.sin(minecraft.theWorld.getCelestialAngleRadians(partialTicks)) < 0.0f) 180.0f else 0.0f,
                0.0f,
                0.0f,
                1.0f
            )
            GL11.glRotatef(90.0f, 0.0f, 0.0f, 1.0f)
            var8 = var24[0]
            var9 = var24[1]
            var10 = var24[2]
            var var13: Float
            if (minecraft.gameSettings.anaglyph) {
                var11 = (var8 * 30.0f + var9 * 59.0f + var10 * 11.0f) / 100.0f
                var12 = (var8 * 30.0f + var9 * 70.0f) / 100.0f
                var13 = (var8 * 30.0f + var10 * 70.0f) / 100.0f
                var8 = var11
                var9 = var12
                var10 = var13
            }
            var23.startDrawing(6)
            var23.setColorRGBA_F(var8, var9, var10, var24[3])
            var23.addVertex(0.0, 100.0, 0.0)
            val var26: Byte = 16
            var23.setColorRGBA_F(var24[0], var24[1], var24[2], 0.0f)
            for (var27 in 0..var26) {
                var13 = var27 * Math.PI.toFloat() * 2.0f / var26
                val var14 = MathHelper.sin(var13)
                val var15 = MathHelper.cos(var13)
                var23.addVertex((var14 * 120.0f).toDouble(), (var15 * 120.0f).toDouble(), (-var15 * 40.0f * var24[3]).toDouble())
            }
            var23.draw()
            GL11.glPopMatrix()
            GL11.glShadeModel(GL11.GL_FLAT)
        }
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE)
        GL11.glPushMatrix()
        var8 = 1.0f - minecraft.theWorld.getRainStrength(partialTicks)
        var9 = 0.0f
        var10 = 0.0f
        var11 = 0.0f
        GL11.glColor4f(1.0f, 1.0f, 1.0f, var8)
        GL11.glTranslatef(var9, var10, var11)
        GL11.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f)

        var deltaTick = partialTicks - prevPartialTicks
        prevPartialTicks = partialTicks
        val curTick = minecraft.theWorld.totalWorldTime
        val tickDiff = (curTick - prevTick).toInt()
        prevTick = curTick
        if (tickDiff in 1..19) {
            deltaTick += tickDiff.toFloat()
        }
        spinAngle -= spinDeltaPerTick * deltaTick
        while (spinAngle < -180f) {
            spinAngle += 360f
        }
        GL11.glRotatef(spinAngle, 0.0f, 1.0f, 0.0f)
        GL11.glColor4f(0.8f, 0.8f, 0.8f, 0.8f)
        GL11.glCallList(starGLCallList)
        GL11.glEnable(GL11.GL_TEXTURE_2D)
        GL11.glPushMatrix()
        val celestialAngle = minecraft.theWorld.getCelestialAngle(partialTicks)
        GL11.glRotatef(celestialAngle * 360.0f, 1.0f, 0.0f, 0.0f)
        if (renderSun) {
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            GL11.glDisable(GL11.GL_TEXTURE_2D)
            GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f)
            var12 = 8.0f
            var23.startDrawingQuads()
            var23.addVertex(-var12.toDouble(), 99.9, -var12.toDouble())
            var23.addVertex(var12.toDouble(), 99.9, -var12.toDouble())
            var23.addVertex(var12.toDouble(), 99.9, var12.toDouble())
            var23.addVertex(-var12.toDouble(), 99.9, var12.toDouble())
            var23.draw()
            GL11.glEnable(GL11.GL_TEXTURE_2D)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE)
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
            var12 = 28.0f
            minecraft.renderEngine.bindTexture(sunTexture)
            var23.startDrawingQuads()
            var23.addVertexWithUV(-var12.toDouble(), 100.0, -var12.toDouble(), 0.0, 0.0)
            var23.addVertexWithUV(var12.toDouble(), 100.0, -var12.toDouble(), 1.0, 0.0)
            var23.addVertexWithUV(var12.toDouble(), 100.0, var12.toDouble(), 1.0, 1.0)
            var23.addVertexWithUV(-var12.toDouble(), 100.0, var12.toDouble(), 0.0, 1.0)
            var23.draw()
        }
        if (renderMoon) {
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            GL11.glDisable(GL11.GL_TEXTURE_2D)
            GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f)
            var12 = 11.3f
            var23.startDrawingQuads()
            var23.addVertex(-var12.toDouble(), -99.9, var12.toDouble())
            var23.addVertex(var12.toDouble(), -99.9, var12.toDouble())
            var23.addVertex(var12.toDouble(), -99.9, -var12.toDouble())
            var23.addVertex(-var12.toDouble(), -99.9, -var12.toDouble())
            var23.draw()
            GL11.glEnable(GL11.GL_TEXTURE_2D)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE)
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
            var12 = 40.0f
            minecraft.renderEngine.bindTexture(moonTexture)
            val var28 = minecraft.theWorld.moonPhase.toFloat()
            val var30 = (var28 % 4).toInt()
            val var29 = (var28 / 4 % 2).toInt()
            val var16 = (var30 + 0) / 4.0f
            val var17 = (var29 + 0) / 2.0f
            val var18 = (var30 + 1) / 4.0f
            val var19 = (var29 + 1) / 2.0f
            var23.startDrawingQuads()
            var23.addVertexWithUV(-var12.toDouble(), -100.0, var12.toDouble(), var18.toDouble(), var19.toDouble())
            var23.addVertexWithUV(var12.toDouble(), -100.0, var12.toDouble(), var16.toDouble(), var19.toDouble())
            var23.addVertexWithUV(var12.toDouble(), -100.0, -var12.toDouble(), var16.toDouble(), var17.toDouble())
            var23.addVertexWithUV(-var12.toDouble(), -100.0, -var12.toDouble(), var18.toDouble(), var17.toDouble())
            var23.draw()
        }
        GL11.glPopMatrix()
        GL11.glDisable(GL11.GL_BLEND)
        GL11.glPushMatrix()
        GL11.glTranslatef(0.0f, -var20 / 10, 0.0f)
        var scale = 100 * (0.3f - var20 / 10000.0f)
        scale = scale.coerceAtLeast(0.2f)
        GL11.glScalef(scale, 0.0f, scale)
        GL11.glTranslatef(0.0f, -var20, 0.0f)
        GL11.glRotatef(90f, 0.0f, 1.0f, 0.0f)
        minecraft.renderEngine.bindTexture(planetToRender)
        var10 = 1.0f
        val alpha = 0.5f
        GL11.glColor4f(
            alpha.coerceAtMost(1.0f),
            alpha.coerceAtMost(1.0f),
            alpha.coerceAtMost(1.0f),
            alpha.coerceAtMost(1.0f)
        )
        var23.startDrawingQuads()
        var23.addVertexWithUV(-var10.toDouble(), 0.0, var10.toDouble(), 0.0, 1.0)
        var23.addVertexWithUV(var10.toDouble(), 0.0, var10.toDouble(), 1.0, 1.0)
        var23.addVertexWithUV(var10.toDouble(), 0.0, -var10.toDouble(), 1.0, 0.0)
        var23.addVertexWithUV(-var10.toDouble(), 0.0, -var10.toDouble(), 0.0, 0.0)
        var23.draw()
        GL11.glPopMatrix()
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        GL11.glPopMatrix()
        GL11.glEnable(GL11.GL_ALPHA_TEST)
        GL11.glColor3f(0.0f, 0.0f, 0.0f)
        GL11.glEnable(GL11.GL_TEXTURE_2D)
        GL11.glEnable(GL11.GL_COLOR_MATERIAL)
        GL11.glDepthMask(true)
    }

    private fun renderStars() {
        val var1 = Random(10842L)
        val var2 = Tessellator.instance
        var2.startDrawingQuads()
        for (var3 in 0..19999) {
            var var4 = (var1.nextFloat() * 2.0f - 1.0f).toDouble()
            var var6 = (var1.nextFloat() * 2.0f - 1.0f).toDouble()
            var var8 = (var1.nextFloat() * 2.0f - 1.0f).toDouble()
            val var10 = (0.07f + var1.nextFloat() * 0.06f).toDouble()
            var var12 = var4 * var4 + var6 * var6 + var8 * var8
            if (var12 < 1.0 && var12 > 0.01) {
                var12 = 1.0 / sqrt(var12)
                var4 *= var12
                var6 *= var12
                var8 *= var12
                val var14 = var4 * var1.nextDouble() * 50.0 + 75.0
                val var16 = var6 * var1.nextDouble() * 50.0 + 75.0
                val var18 = var8 * var1.nextDouble() * 50.0 + 75.0
                val var20 = atan2(var4, var8)
                val var22 = sin(var20)
                val var24 = cos(var20)
                val var26 = atan2(sqrt(var4 * var4 + var8 * var8), var6)
                val var28 = sin(var26)
                val var30 = cos(var26)
                val var32 = var1.nextDouble() * Math.PI * 2.0
                val var34 = sin(var32)
                val var36 = cos(var32)
                for (var38 in 0..3) {
                    val var39 = 0.0
                    val var41 = ((var38 and 2) - 1) * var10
                    val var43 = ((var38 + 1 and 2) - 1) * var10
                    val var47 = var41 * var36 - var43 * var34
                    val var49 = var43 * var36 + var41 * var34
                    val var53 = var47 * var28 + var39 * var30
                    val var55 = var39 * var28 - var47 * var30
                    val var57 = var55 * var22 - var49 * var24
                    val var61 = var49 * var22 + var55 * var24
                    var2.addVertex(var14 + var57, var16 + var53, var18 + var61)
                }
            }
        }
        var2.draw()
    }

    companion object {
        private val moonTexture = ResourceLocation("textures/environment/moon_phases.png")
        private val sunTexture = ResourceLocation(MODID, "textures/gui/sol/orbit/orbitalsun.png")
    }
}
