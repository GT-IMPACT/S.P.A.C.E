package space.impact.space.addons.solar_system.venus.world

import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.client.renderer.EntityRenderer
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.util.MathHelper
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.IRenderHandler
import org.lwjgl.opengl.GL11

class CloudProviderVenus : IRenderHandler() {

    override fun render(partialTicks: Float, world: WorldClient, mc: Minecraft) {
        GL11.glDisable(2884)
        val f1 = (mc.renderViewEntity.lastTickPosY + (mc.renderViewEntity.posY - mc.renderViewEntity.lastTickPosY) * partialTicks.toDouble()).toFloat()
        val tess = Tessellator.instance
        val f2 = 12.0f
        val f3 = 4.0f
        mc.renderEngine.bindTexture(locationCloudsPng)
        GL11.glEnable(3042)
        OpenGlHelper.glBlendFunc(770, 771, 1, 0)
        mc.theWorld.getCloudColour(partialTicks)
        val b0: Byte = 10
        val b1: Byte = 2
        val f13 = 9.765625E-4f
        for (count in 0..3) {
            GL11.glPushMatrix()
            GL11.glScalef(f2 + (count * 2).toFloat(), 1.0f, f2 + (count * 2).toFloat())
            for (k in 0..1) {
                if (k == 0) {
                    GL11.glColorMask(false, false, false, false)
                } else if (mc.gameSettings.anaglyph) {
                    if (EntityRenderer.anaglyphField == 0) {
                        GL11.glColorMask(false, true, true, true)
                    } else {
                        GL11.glColorMask(true, false, false, true)
                    }
                } else {
                    GL11.glColorMask(true, true, true, true)
                }
                val d0 = (cloudTickCounter.toFloat() * (1.0f - count.toFloat() / 5.0f) + count.toFloat() / 5.0f * partialTicks + (count * 250000).toFloat()).toDouble()
                var d1 = (mc.renderViewEntity.prevPosX + (mc.renderViewEntity.posX - mc.renderViewEntity.prevPosX) * partialTicks.toDouble() + d0 * 0.029999999329447746) / f2.toDouble()
                var d2 = (mc.renderViewEntity.prevPosZ + (mc.renderViewEntity.posZ - mc.renderViewEntity.prevPosZ) * partialTicks.toDouble()) / f2.toDouble() + 0.33000001311302185
                val f4 = mc.theWorld.provider.cloudHeight - f1 + 0.33f + count.toFloat() * 20.0f
                val i = MathHelper.floor_double(d1 / 2048.0)
                val j = MathHelper.floor_double(d2 / 2048.0)
                d1 -= (i * 2048).toDouble()
                d2 -= (j * 2048).toDouble()
                var f5 = 234.0f / (1200.0f + count.toFloat() * 300.0f)
                var f6 = 147.0f / (1200.0f + count.toFloat() * 300.0f)
                var f7 = 9.0f / (1200.0f + count.toFloat() * 300.0f)
                var f8: Float
                var f9: Float
                var f10: Float
                if (mc.gameSettings.anaglyph) {
                    f8 = (f5 * 30.0f + f6 * 59.0f + f7 * 11.0f) / 100.0f
                    f9 = (f5 * 30.0f + f6 * 70.0f) / 100.0f
                    f10 = (f5 * 30.0f + f7 * 70.0f) / 100.0f
                    f5 = f8
                    f6 = f9
                    f7 = f10
                }
                f10 = 0.00390625f
                f8 = MathHelper.floor_double(d1).toFloat() * f10
                f9 = MathHelper.floor_double(d2).toFloat() * f10
                val f11 = (d1 - MathHelper.floor_double(d1).toDouble()).toFloat()
                val f12 = (d2 - MathHelper.floor_double(d2).toDouble()).toFloat()
                for (l in -b1 + 1..b1) {
                    for (i1 in -b1 + 1..b1) {
                        tess.startDrawingQuads()
                        val f14 = (l * b0).toFloat()
                        val f15 = (i1 * b0).toFloat()
                        val f16 = f14 - f11
                        val f17 = f15 - f12
                        if (f4 > -f3 - 1.0f) {
                            tess.setColorRGBA_F(f5 * 0.7f, f6 * 0.7f, f7 * 0.7f, 0.9f)
                            tess.setNormal(0.0f, -1.0f, 0.0f)
                            tess.addVertexWithUV(
                                (f16 + 0.0f).toDouble(), (f4 + 0.0f).toDouble(), (f17 + b0.toFloat()).toDouble(), ((f14 + 0.0f) * f10 + f8).toDouble(), ((f15 + b0.toFloat()) * f10 + f9).toDouble()
                            )
                            tess.addVertexWithUV(
                                (f16 + b0.toFloat()).toDouble(),
                                (f4 + 0.0f).toDouble(),
                                (f17 + b0.toFloat()).toDouble(),
                                ((f14 + b0.toFloat()) * f10 + f8).toDouble(),
                                ((f15 + b0.toFloat()) * f10 + f9).toDouble()
                            )
                            tess.addVertexWithUV(
                                (f16 + b0.toFloat()).toDouble(), (f4 + 0.0f).toDouble(), (f17 + 0.0f).toDouble(), ((f14 + b0.toFloat()) * f10 + f8).toDouble(), ((f15 + 0.0f) * f10 + f9).toDouble()
                            )
                            tess.addVertexWithUV(
                                (f16 + 0.0f).toDouble(), (f4 + 0.0f).toDouble(), (f17 + 0.0f).toDouble(), ((f14 + 0.0f) * f10 + f8).toDouble(), ((f15 + 0.0f) * f10 + f9).toDouble()
                            )
                        }
                        if (f4 <= f3 + 1.0f) {
                            tess.setColorRGBA_F(f5, f6, f7, 0.9f)
                            tess.setNormal(0.0f, 1.0f, 0.0f)
                            tess.addVertexWithUV(
                                (f16 + 0.0f).toDouble(), (f4 + f3 - f13).toDouble(), (f17 + b0.toFloat()).toDouble(), ((f14 + 0.0f) * f10 + f8).toDouble(), ((f15 + b0.toFloat()) * f10 + f9).toDouble()
                            )
                            tess.addVertexWithUV(
                                (f16 + b0.toFloat()).toDouble(),
                                (f4 + f3 - f13).toDouble(),
                                (f17 + b0.toFloat()).toDouble(),
                                ((f14 + b0.toFloat()) * f10 + f8).toDouble(),
                                ((f15 + b0.toFloat()) * f10 + f9).toDouble()
                            )
                            tess.addVertexWithUV(
                                (f16 + b0.toFloat()).toDouble(), (f4 + f3 - f13).toDouble(), (f17 + 0.0f).toDouble(), ((f14 + b0.toFloat()) * f10 + f8).toDouble(), ((f15 + 0.0f) * f10 + f9).toDouble()
                            )
                            tess.addVertexWithUV(
                                (f16 + 0.0f).toDouble(), (f4 + f3 - f13).toDouble(), (f17 + 0.0f).toDouble(), ((f14 + 0.0f) * f10 + f8).toDouble(), ((f15 + 0.0f) * f10 + f9).toDouble()
                            )
                        }
                        tess.setColorRGBA_F(f5 * 0.9f, f6 * 0.9f, f7 * 0.9f, 0.9f)
                        var j1: Int
                        if (l > -1) {
                            tess.setNormal(-1.0f, 0.0f, 0.0f)
                            j1 = 0
                            while (j1 < b0) {
                                tess.addVertexWithUV(
                                    (f16 + j1.toFloat() + 0.0f).toDouble(),
                                    (f4 + 0.0f).toDouble(),
                                    (f17 + b0.toFloat()).toDouble(),
                                    ((f14 + j1.toFloat() + 0.5f) * f10 + f8).toDouble(),
                                    ((f15 + b0.toFloat()) * f10 + f9).toDouble()
                                )
                                tess.addVertexWithUV(
                                    (f16 + j1.toFloat() + 0.0f).toDouble(),
                                    (f4 + f3).toDouble(),
                                    (f17 + b0.toFloat()).toDouble(),
                                    ((f14 + j1.toFloat() + 0.5f) * f10 + f8).toDouble(),
                                    ((f15 + b0.toFloat()) * f10 + f9).toDouble()
                                )
                                tess.addVertexWithUV(
                                    (f16 + j1.toFloat() + 0.0f).toDouble(),
                                    (f4 + f3).toDouble(),
                                    (f17 + 0.0f).toDouble(),
                                    ((f14 + j1.toFloat() + 0.5f) * f10 + f8).toDouble(),
                                    ((f15 + 0.0f) * f10 + f9).toDouble()
                                )
                                tess.addVertexWithUV(
                                    (f16 + j1.toFloat() + 0.0f).toDouble(),
                                    (f4 + 0.0f).toDouble(),
                                    (f17 + 0.0f).toDouble(),
                                    ((f14 + j1.toFloat() + 0.5f) * f10 + f8).toDouble(),
                                    ((f15 + 0.0f) * f10 + f9).toDouble()
                                )
                                ++j1
                            }
                        }
                        if (l <= 1) {
                            tess.setNormal(1.0f, 0.0f, 0.0f)
                            j1 = 0
                            while (j1 < b0) {
                                tess.addVertexWithUV(
                                    (f16 + j1.toFloat() + 1.0f - f13).toDouble(),
                                    (f4 + 0.0f).toDouble(),
                                    (f17 + b0.toFloat()).toDouble(),
                                    ((f14 + j1.toFloat() + 0.5f) * f10 + f8).toDouble(),
                                    ((f15 + b0.toFloat()) * f10 + f9).toDouble()
                                )
                                tess.addVertexWithUV(
                                    (f16 + j1.toFloat() + 1.0f - f13).toDouble(),
                                    (f4 + f3).toDouble(),
                                    (f17 + b0.toFloat()).toDouble(),
                                    ((f14 + j1.toFloat() + 0.5f) * f10 + f8).toDouble(),
                                    ((f15 + b0.toFloat()) * f10 + f9).toDouble()
                                )
                                tess.addVertexWithUV(
                                    (f16 + j1.toFloat() + 1.0f - f13).toDouble(),
                                    (f4 + f3).toDouble(),
                                    (f17 + 0.0f).toDouble(),
                                    ((f14 + j1.toFloat() + 0.5f) * f10 + f8).toDouble(),
                                    ((f15 + 0.0f) * f10 + f9).toDouble()
                                )
                                tess.addVertexWithUV(
                                    (f16 + j1.toFloat() + 1.0f - f13).toDouble(),
                                    (f4 + 0.0f).toDouble(),
                                    (f17 + 0.0f).toDouble(),
                                    ((f14 + j1.toFloat() + 0.5f) * f10 + f8).toDouble(),
                                    ((f15 + 0.0f) * f10 + f9).toDouble()
                                )
                                ++j1
                            }
                        }
                        tess.setColorRGBA_F(f5 * 0.8f, f6 * 0.8f, f7 * 0.8f, 0.9f)
                        if (i1 > -1) {
                            tess.setNormal(0.0f, 0.0f, -1.0f)
                            j1 = 0
                            while (j1 < b0) {
                                tess.addVertexWithUV(
                                    (f16 + 0.0f).toDouble(),
                                    (f4 + f3).toDouble(),
                                    (f17 + j1.toFloat() + 0.0f).toDouble(),
                                    ((f14 + 0.0f) * f10 + f8).toDouble(),
                                    ((f15 + j1.toFloat() + 0.5f) * f10 + f9).toDouble()
                                )
                                tess.addVertexWithUV(
                                    (f16 + b0.toFloat()).toDouble(),
                                    (f4 + f3).toDouble(),
                                    (f17 + j1.toFloat() + 0.0f).toDouble(),
                                    ((f14 + b0.toFloat()) * f10 + f8).toDouble(),
                                    ((f15 + j1.toFloat() + 0.5f) * f10 + f9).toDouble()
                                )
                                tess.addVertexWithUV(
                                    (f16 + b0.toFloat()).toDouble(),
                                    (f4 + 0.0f).toDouble(),
                                    (f17 + j1.toFloat() + 0.0f).toDouble(),
                                    ((f14 + b0.toFloat()) * f10 + f8).toDouble(),
                                    ((f15 + j1.toFloat() + 0.5f) * f10 + f9).toDouble()
                                )
                                tess.addVertexWithUV(
                                    (f16 + 0.0f).toDouble(),
                                    (f4 + 0.0f).toDouble(),
                                    (f17 + j1.toFloat() + 0.0f).toDouble(),
                                    ((f14 + 0.0f) * f10 + f8).toDouble(),
                                    ((f15 + j1.toFloat() + 0.5f) * f10 + f9).toDouble()
                                )
                                ++j1
                            }
                        }
                        if (i1 <= 1) {
                            tess.setNormal(0.0f, 0.0f, 1.0f)
                            j1 = 0
                            while (j1 < b0) {
                                tess.addVertexWithUV(
                                    (f16 + 0.0f).toDouble(),
                                    (f4 + f3).toDouble(),
                                    (f17 + j1.toFloat() + 1.0f - f13).toDouble(),
                                    ((f14 + 0.0f) * f10 + f8).toDouble(),
                                    ((f15 + j1.toFloat() + 0.5f) * f10 + f9).toDouble()
                                )
                                tess.addVertexWithUV(
                                    (f16 + b0.toFloat()).toDouble(),
                                    (f4 + f3).toDouble(),
                                    (f17 + j1.toFloat() + 1.0f - f13).toDouble(),
                                    ((f14 + b0.toFloat()) * f10 + f8).toDouble(),
                                    ((f15 + j1.toFloat() + 0.5f) * f10 + f9).toDouble()
                                )
                                tess.addVertexWithUV(
                                    (f16 + b0.toFloat()).toDouble(),
                                    (f4 + 0.0f).toDouble(),
                                    (f17 + j1.toFloat() + 1.0f - f13).toDouble(),
                                    ((f14 + b0.toFloat()) * f10 + f8).toDouble(),
                                    ((f15 + j1.toFloat() + 0.5f) * f10 + f9).toDouble()
                                )
                                tess.addVertexWithUV(
                                    (f16 + 0.0f).toDouble(),
                                    (f4 + 0.0f).toDouble(),
                                    (f17 + j1.toFloat() + 1.0f - f13).toDouble(),
                                    ((f14 + 0.0f) * f10 + f8).toDouble(),
                                    ((f15 + j1.toFloat() + 0.5f) * f10 + f9).toDouble()
                                )
                                ++j1
                            }
                        }
                        tess.draw()
                    }
                }
            }
            GL11.glPopMatrix()
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        GL11.glDisable(3042)
        GL11.glEnable(2884)
    }

    companion object {
        private val locationCloudsPng = ResourceLocation("textures/environment/clouds.png")
        var cloudTickCounter = 0
    }
}