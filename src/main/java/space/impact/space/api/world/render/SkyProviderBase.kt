package space.impact.space.api.world.render

import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.client.renderer.GLAllocation
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.MathHelper
import net.minecraftforge.client.IRenderHandler
import org.lwjgl.opengl.GL11
import space.impact.space.api.world.render.SkyRenderManager.getRandomStarColor
import space.impact.space.utils.world.RenderUtils
import java.util.*
import kotlin.math.max

abstract class SkyProviderBase : IRenderHandler() {

    companion object {
        const val RENDER_CHUNK_SIZE = 384
        const val RENDER_CHUNK_STEP = 64
        const val START_Y_POS = 16.0
    }

    private val displayStarsId = GLAllocation.generateDisplayLists(2)
    private val displaySkyLayer1Id = displayStarsId + 1
    private val displaySkyLayer2Id = displayStarsId + 2

    protected var mc: Minecraft = Minecraft.getMinecraft()
    private var currentTick = 0f
    protected open val params: SkyRenderParameters = SkyRenderParameters()

    init {
        val tess = Tessellator.instance
        preRenderStars()
        preRenderSkyAndFog(tess)
    }

    override fun render(partialTicks: Float, world: WorldClient, mc: Minecraft) {
        val tess = Tessellator.instance
        currentTick = partialTicks
        doRenderStarsAndFog(world, partialTicks)
        doRenderCustom(tess, partialTicks)
    }

    protected abstract fun doRenderCustom(tess: Tessellator, partialTicks: Float)

    private fun inWater(player: EntityPlayer): Boolean {
        val vec = player.getPosition(1.0f)
        return mc.theWorld.getBlock(vec.xCoord.toInt(), vec.yCoord.toInt(), vec.zCoord.toInt()).material == Material.water
    }

    private fun doRenderStarsAndFog(
        world: WorldClient,
        partialTicks: Float,
    ) {
        val skyColor = world.getSkyColor(mc.renderViewEntity, partialTicks)
        val skyColorRed = skyColor.xCoord.toFloat()
        val skyColorGreen = skyColor.yCoord.toFloat()
        val skyColorBlue = skyColor.zCoord.toFloat()

        GL11.glDisable(GL11.GL_TEXTURE_2D)
        GL11.glDepthMask(false)
        GL11.glEnable(GL11.GL_FOG)
        GL11.glColor3f(skyColorRed, skyColorGreen, skyColorBlue)
        GL11.glCallList(displaySkyLayer1Id)
        GL11.glDisable(GL11.GL_FOG)
        GL11.glDisable(GL11.GL_ALPHA_TEST)
        GL11.glEnable(GL11.GL_BLEND)

        OpenGlHelper.glBlendFunc(770, 771, 1, 0)
        RenderHelper.disableStandardItemLighting()

        if (params.isEnabledStars) {
            val rainStrength = 1.0f - world.getRainStrength(partialTicks)
            val starBrightness = world.getStarBrightness(partialTicks) * rainStrength

            if (starBrightness > 0.0f && !inWater(mc.thePlayer)) {
                GL11.glPushMatrix()
                GL11.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f)
                GL11.glRotatef(world.getCelestialAngle(partialTicks) * 360.0f, 1.0f, 0.0f, 0.0f)
                GL11.glRotatef(-19.0f, 0.0f, 1.0f, 0.0f)
                GL11.glColor4f(starBrightness, starBrightness, starBrightness, starBrightness)
                GL11.glCallList(displayStarsId)
                GL11.glPopMatrix()
            }
        }
        GL11.glDepthMask(true)
        GL11.glEnable(GL11.GL_TEXTURE_2D)
    }

    private fun preRenderSkyAndFog(tess: Tessellator) {
        GL11.glNewList(displaySkyLayer1Id, GL11.GL_COMPILE)
        var startZ: Int
        var startX = -RENDER_CHUNK_SIZE
        while (startX <= RENDER_CHUNK_SIZE) {
            startZ = -RENDER_CHUNK_SIZE
            while (startZ <= RENDER_CHUNK_SIZE) {
                tess.startDrawingQuads()
                tess.addVertex(startX.toDouble(), START_Y_POS, startZ.toDouble())
                tess.addVertex((startX + RENDER_CHUNK_STEP).toDouble(), START_Y_POS, startZ.toDouble())
                tess.addVertex((startX + RENDER_CHUNK_STEP).toDouble(), START_Y_POS, (startZ + RENDER_CHUNK_STEP).toDouble())
                tess.addVertex(startX.toDouble(), START_Y_POS, (startZ + RENDER_CHUNK_STEP).toDouble())
                tess.draw()
                startZ += RENDER_CHUNK_STEP
            }
            startX += RENDER_CHUNK_STEP
        }
        GL11.glEndList()

        GL11.glNewList(displaySkyLayer2Id, GL11.GL_COMPILE)
        startX = -RENDER_CHUNK_SIZE
        while (startX <= RENDER_CHUNK_SIZE) {
            startZ = -RENDER_CHUNK_SIZE
            while (startZ <= RENDER_CHUNK_SIZE) {
                tess.startDrawingQuads()
                tess.addVertex((startX + RENDER_CHUNK_STEP).toDouble(), -START_Y_POS, startZ.toDouble())
                tess.addVertex(startX.toDouble(), -START_Y_POS, startZ.toDouble())
                tess.addVertex(startX.toDouble(), -START_Y_POS, (startZ + RENDER_CHUNK_STEP).toDouble())
                tess.addVertex((startX + RENDER_CHUNK_STEP).toDouble(), -START_Y_POS, (startZ + RENDER_CHUNK_STEP).toDouble())
                tess.draw()
                startZ += RENDER_CHUNK_STEP
            }
            startX += RENDER_CHUNK_STEP
        }
        GL11.glEndList()
    }

    private fun preRenderStars() {
        if (params.isEnabledStars) {
            GL11.glPushMatrix()
            GL11.glNewList(displayStarsId, GL11.GL_COMPILE)

            val screenWidth = Minecraft.getMinecraft().displayWidth
            val screenHeight = Minecraft.getMinecraft().displayHeight
            val rand = Random(0x123456789)
            val mc = Minecraft.getMinecraft()
            val world = mc.theWorld
            val celestialAngle = world.getCelestialAngle(0f)

            GL11.glDisable(GL11.GL_TEXTURE_2D)
            GL11.glDisable(GL11.GL_DEPTH_TEST)
            GL11.glDepthMask(false)
            GL11.glEnable(GL11.GL_BLEND)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)

            GL11.glPushMatrix()
            GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f)
            GL11.glRotatef(MathHelper.sin(celestialAngle * Math.PI.toFloat()) * 180.0f, 0.0f, 0.0f, 1.0f)
            val tessellator = Tessellator.instance
            tessellator.startDrawing(GL11.GL_POINTS)
            for (i in 0..4999) { // Можете изменить количество звезд по вашему вкусу
                val brightness = rand.nextFloat() * 0.2f + 0.1f // Случайная яркость
                val size = rand.nextFloat() * 2.0f + 1.0f // Случайный размер
                val color = getRandomStarColor(rand) // Случайный цвет

                val scaleFactor = (screenWidth * screenHeight).toFloat() / (1920 * 1080).toFloat() // Коэффициент масштабирования
                val starSize: Float = 1f + (1.5f - 1f) * scaleFactor // Рассчитываем размер звезды
                GL11.glPointSize(minOf(max(starSize, 2f), 2f))
                tessellator.setColorRGBA_I(color, (brightness * 255.0f).toInt()) // Устанавливаем цвет и яркость
                val x = rand.nextGaussian() * 50 * size // Умножаем координаты на размер звезды
                val y = rand.nextGaussian() * 50 * size
                val z = rand.nextGaussian() * 50 * size
                tessellator.addVertex(x, y, z) // Рисуем звезду
            }
            tessellator.draw()
            GL11.glPopMatrix()

            GL11.glDepthMask(true)
            GL11.glEnable(GL11.GL_DEPTH_TEST)
            GL11.glEnable(GL11.GL_TEXTURE_2D)
            GL11.glDisable(GL11.GL_BLEND)

            GL11.glEndList()
            GL11.glPopMatrix()
        }
    }

    protected fun getCelestialAngle(daylength: Long): Float {
        return RenderUtils.calculateCelestialAngle(mc.theWorld.worldTime, currentTick, daylength.toInt().toFloat()) * 360.0f
    }
}
