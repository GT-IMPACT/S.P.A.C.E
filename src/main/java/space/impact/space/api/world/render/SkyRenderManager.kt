package space.impact.space.api.world.render

import cpw.mods.fml.client.FMLClientHandler
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import space.impact.space.MODID
import space.impact.space.api.world.world_math.Vector3
import space.impact.space.config.Config
import java.util.*

object SkyRenderManager {

    val jupiterTexture: ResourceLocation = ResourceLocation(MODID, "textures/gui/sol/jupiter${if (Config.isEnabledSupportHDTexturePlanet) "_hd" else ""}.png")
    val ioTexture: ResourceLocation = ResourceLocation(MODID, "textures/gui/sol/moons/io${if (Config.isEnabledSupportHDTexturePlanet) "_hd" else ""}.png")
    val ganymedeTexture: ResourceLocation = ResourceLocation(MODID, "textures/gui/sol/moons/ganymede${if (Config.isEnabledSupportHDTexturePlanet) "_hd" else ""}.png")
    val callistoTexture: ResourceLocation = ResourceLocation(MODID, "textures/gui/sol/moons/callisto${if (Config.isEnabledSupportHDTexturePlanet) "_hd" else ""}.png")
    val sunTexture = ResourceLocation("textures/environment/sun.png")
    val lmcTexture: ResourceLocation = ResourceLocation(MODID, "textures/environment/background/LMC.png")
    val smcTexture: ResourceLocation = ResourceLocation(MODID, "textures/environment/background/SMC.png")
    val andromedaTexture: ResourceLocation = ResourceLocation(MODID, "textures/environment/background/Andromeda.png")
    val moonTexture: ResourceLocation = ResourceLocation("textures/environment/moon_phases.png")
    val barnardaLoopTexture: ResourceLocation = ResourceLocation(MODID, "textures/environment/background/BarnardaLoop.png")
    val pumpkinSunTexture: ResourceLocation = ResourceLocation(MODID, "textures/environment/pumkinsun.png")
    val earthTexture: ResourceLocation = ResourceLocation(MODID, "textures/gui/sol/earth${if (Config.isEnabledSupportHDTexturePlanet) "_hd" else ""}.png")

    inline fun renderPlanet(onRender: () -> Unit) {
        GL11.glDisable(GL11.GL_DEPTH_TEST)
        GL11.glDepthMask(false)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glPushMatrix()

        onRender()

        GL11.glPopMatrix()

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL11.glDisable(GL11.GL_BLEND)
    }

    fun doRenderCustom(
        image: ResourceLocation?,
        x: Float, y: Float, z: Float,
        size: Float,
        alpha: Float = FMLClientHandler.instance().client.theWorld.getStarBrightness(1.0f),
    ) {
        GL11.glPushMatrix()
        GL11.glEnable(GL11.GL_BLEND)

        val tess = Tessellator.instance
        GL11.glRotatef(x, 0.0f, 1.0f, 0.0f)
        GL11.glRotatef(y, 1.0f, 0.0f, 0.0f)
        GL11.glRotatef(z, 0.0f, 0.0f, 1.0f)
        GL11.glColor4f(1.0f, 1.0f, 1.0f, alpha)

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT)

//        GL11.glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

        FMLClientHandler.instance().client.renderEngine.bindTexture(image)
        tess.startDrawingQuads()
        tess.addVertexWithUV((-size).toDouble(), -100.0, size.toDouble(), 0.0, 1.0)
        tess.addVertexWithUV(size.toDouble(), -100.0, size.toDouble(), 1.0, 1.0)
        tess.addVertexWithUV(size.toDouble(), -100.0, (-size).toDouble(), 1.0, 0.0)
        tess.addVertexWithUV((-size).toDouble(), -100.0, (-size).toDouble(), 0.0, 0.0)
        tess.draw()

        GL11.glDisable(GL11.GL_BLEND)
        GL11.glPopAttrib()
        GL11.glPopMatrix()
    }

    fun getRandomStarColor(random: Random): Int {
        val colors = intArrayOf(0xFFFFFF, 0xFFFFCC, 0xFFFF99, 0xFFFF66, 0xFFFF33) // Можете изменить список цветов
        return colors[random.nextInt(colors.size)]
    }

    fun renderTestWithUV(tess: Tessellator, yMax: Double, xMin: Double, zMin: Double, xMax: Double, zMax: Double, uMin: Double, uMax: Double, vMin: Double, vMax: Double) {
        tess.setNormal(0.0f, 1.0f, 0.0f)
        tess.addVertexWithUV(xMin, yMax, zMin, uMin, vMin)
        tess.addVertexWithUV(xMin, yMax, zMax, uMin, vMax)
        tess.addVertexWithUV(xMax, yMax, zMax, uMax, vMax)
        tess.addVertexWithUV(xMax, yMax, zMin, uMax, vMin)
    }

    fun renderAtmosphere(tess: Tessellator, x: Float, y: Float, size: Float, vec: Vector3?) {
        if (vec != null) {
            val thePlayer = Minecraft.getMinecraft().thePlayer
            GL11.glEnable(GL11.GL_BLEND)
            GL11.glDisable(GL11.GL_TEXTURE_2D)
            GL11.glBlendFunc(770, 771)
            GL11.glRotatef(y, 0.0f, 0.0f, 1.0f)
            GL11.glRotatef(x, 1.0f, 0.0f, 0.0f)
            val planetOrbitalDistance = 5.0
            val dist = -64.0 - 4.0 * planetOrbitalDistance / 2.0 - (thePlayer.posY.toFloat() / thePlayer.posY.toFloat()).toDouble()
            val scalingMult = 1.0 - 0.9 * planetOrbitalDistance
            val xOffset = (System.currentTimeMillis().toDouble() / 1000000.0 % 1.0).toFloat()
            val f14 = 1.0f + xOffset
            val f15 = 0.0f + xOffset
            val color = floatArrayOf(vec.x.toFloat(), vec.y.toFloat(), vec.z.toFloat())
            tess.startDrawingQuads()
            tess.setColorRGBA_F(color[0], color[1], color[2], 0.09f)
            for (i in 0..4) {
                renderTestWithUV(tess, dist + i.toDouble() * scalingMult, (-size).toDouble(), (-size).toDouble(), 0.0, 0.0, f14.toDouble(), f15.toDouble(), f15.toDouble(), f14.toDouble())
                renderTestWithUV(tess, dist + i.toDouble() * scalingMult, 0.0, 0.0, size.toDouble(), size.toDouble(), f14.toDouble(), f15.toDouble(), f15.toDouble(), f14.toDouble())
                renderTestWithUV(tess, dist + i.toDouble() * scalingMult, (-size).toDouble(), 0.0, 0.0, size.toDouble(), f14.toDouble(), f15.toDouble(), f15.toDouble(), f14.toDouble())
                renderTestWithUV(tess, dist + i.toDouble() * scalingMult, 0.0, (-size).toDouble(), size.toDouble(), 0.0, f14.toDouble(), f15.toDouble(), f15.toDouble(), f14.toDouble())
            }
            tess.draw()
            GL11.glDisable(GL11.GL_BLEND)
            GL11.glEnable(GL11.GL_TEXTURE_2D)
        }
    }
}
