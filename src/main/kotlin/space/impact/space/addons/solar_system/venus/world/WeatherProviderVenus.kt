package space.impact.space.addons.solar_system.venus.world

import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.util.MathHelper
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.IRenderHandler
import org.lwjgl.opengl.GL11
import java.util.*

class WeatherProviderVenus : IRenderHandler() {
    private var rainXCoords: FloatArray = floatArrayOf()
    private var rainYCoords: FloatArray = floatArrayOf()
    private var rendererUpdateCount = 0
    private val random = Random()

    override fun render(partialTicks: Float, world: WorldClient, mc: Minecraft) {
        ++rendererUpdateCount
        renderNormalWeather(partialTicks, mc)
    }

    private fun renderNormalWeather(partialTicks: Float, mc: Minecraft) {
        val rainStrength = mc.theWorld.getRainStrength(partialTicks)
        if (rainStrength > 0.0f) {
            mc.entityRenderer.enableLightmap(partialTicks.toDouble())
            initializeRainCoords()
            val entitylivingbase = mc.renderViewEntity
            val worldclient = mc.theWorld
            val k2 = MathHelper.floor_double(entitylivingbase.posX)
            val l2 = MathHelper.floor_double(entitylivingbase.posY)
            val i3 = MathHelper.floor_double(entitylivingbase.posZ)
            val tessellator = Tessellator.instance
            GL11.glDisable(2884)
            GL11.glNormal3f(0.0f, 1.0f, 0.0f)
            GL11.glEnable(3042)
            OpenGlHelper.glBlendFunc(770, 771, 1, 0)
            GL11.glAlphaFunc(516, 0.1f)
            val d0 = entitylivingbase.lastTickPosX + (entitylivingbase.posX - entitylivingbase.lastTickPosX) * partialTicks.toDouble()
            val d1 = entitylivingbase.lastTickPosY + (entitylivingbase.posY - entitylivingbase.lastTickPosY) * partialTicks.toDouble()
            val d2 = entitylivingbase.lastTickPosZ + (entitylivingbase.posZ - entitylivingbase.lastTickPosZ) * partialTicks.toDouble()
            val k = MathHelper.floor_double(d1)
            var range: Byte = 4
            if (mc.gameSettings.fancyGraphics) {
                range = 8
            }
            var b1: Byte = -1
            val f5 = rendererUpdateCount.toFloat() + partialTicks
            if (mc.gameSettings.fancyGraphics) {
                range = 10
            }
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)

            for (l in i3 - range..i3 + range) {
                for (i1 in k2 - range..k2 + range) {
                    val j1 = (l - i3 + 16) * 32 + i1 - k2 + 16
                    val f6 = rainXCoords[j1] * 0.5f
                    val f7 = rainYCoords[j1] * 0.5f
                    val biomegenbase = worldclient.getBiomeGenForCoords(i1, l)
                    if (biomegenbase.canSpawnLightningBolt() || biomegenbase.enableSnow) {
                        val yHeight = worldclient.getPrecipitationHeight(i1, l) + 10 - (4.8f * rainStrength).toInt()
                        var y = l2 - range
                        var yMax = l2 + range
                        if (y < yHeight) {
                            y = yHeight
                        }
                        if (yMax < yHeight) {
                            yMax = yHeight
                        }
                        val f8 = 1.0f
                        var yBase = yHeight
                        if (yHeight < k) {
                            yBase = k
                        }
                        if (y != yMax) {
                            random.setSeed((i1 * i1 * 3121 + i1 * 45238971 xor l * l * 418711 + l * 13761).toLong())
                            biomegenbase.getFloatTemperature(i1, y, l)
                            if (b1.toInt() != 0) {
                                if (b1 >= 0) {
                                    tessellator.draw()
                                }
                                b1 = 0
                                mc.textureManager.bindTexture(locationRainPng)
                                tessellator.startDrawingQuads()
                            }
                            val speed = rendererUpdateCount / 4
                            val downwardsMotion = ((speed + i1 * i1 * 3121 + i1 * 45238971 + l * l * 418711 + l * 13761 and 31).toFloat() + partialTicks) / 32.0f * (1.0f + random.nextFloat())
                            val yo = random.nextDouble() / 1.8
                            val d3 = (i1.toFloat() + 0.5f).toDouble() - entitylivingbase.posX
                            val xDist = (l.toFloat() + 0.5f).toDouble() - entitylivingbase.posZ
                            val f12 = MathHelper.sqrt_double(d3 * d3 + xDist * xDist) / range.toFloat()
                            tessellator.setBrightness(worldclient.getLightBrightnessForSkyBlocks(i1, yBase, l, 0))
                            tessellator.setColorRGBA_F(1.0f, 0.5f, 0.0f, ((1.0f - f12 * f12) * 0.5f + 0.5f) * rainStrength)
                            tessellator.setTranslation(-d0 * 1.0, -d1 * 1.0, -d2 * 1.0)
                            tessellator.addVertexWithUV(
                                (i1.toFloat() - f6).toDouble() + 0.5,
                                y.toDouble() - yo,
                                (l.toFloat() - f7).toDouble() + 0.5,
                                (0.0f * f8).toDouble(),
                                (y.toFloat() * f8 / 4.0f + downwardsMotion * f8).toDouble()
                            )
                            tessellator.addVertexWithUV(
                                (i1.toFloat() + f6).toDouble() + 0.5,
                                y.toDouble() - yo,
                                (l.toFloat() + f7).toDouble() + 0.5,
                                (1.0f * f8).toDouble(),
                                (y.toFloat() * f8 / 4.0f + downwardsMotion * f8).toDouble()
                            )
                            tessellator.addVertexWithUV(
                                (i1.toFloat() + f6).toDouble() + 0.5,
                                yMax.toDouble() - yo,
                                (l.toFloat() + f7).toDouble() + 0.5,
                                (1.0f * f8).toDouble(),
                                (yMax.toFloat() * f8 / 4.0f + downwardsMotion * f8).toDouble()
                            )
                            tessellator.addVertexWithUV(
                                (i1.toFloat() - f6).toDouble() + 0.5,
                                yMax.toDouble() - yo,
                                (l.toFloat() - f7).toDouble() + 0.5,
                                (0.0f * f8).toDouble(),
                                (yMax.toFloat() * f8 / 4.0f + downwardsMotion * f8).toDouble()
                            )
                        }
                    }
                }
            }
            if (b1 >= 0) {
                tessellator.draw()
            }
            tessellator.setTranslation(0.0, 0.0, 0.0)
            GL11.glEnable(2884)
            GL11.glDisable(3042)
            GL11.glAlphaFunc(516, 0.1f)
            mc.entityRenderer.disableLightmap(partialTicks.toDouble())
        }
    }

    private fun initializeRainCoords() {
        if (rainXCoords.isEmpty()) {
            rainXCoords = FloatArray(1024)
            rainYCoords = FloatArray(1024)
            for (i in 0..31) {
                val f1 = (i - 16).toFloat()
                for (j in 0..31) {
                    val f = (j - 16).toFloat()
                    val f2 = MathHelper.sqrt_float(f * f + f1 * f1)
                    rainXCoords[i shl 5 or j] = -f1 / f2
                    rainYCoords[i shl 5 or j] = f / f2
                }
            }
        }
    }

    companion object {
        private val locationRainPng = ResourceLocation("textures/environment/rain.png")
    }
}