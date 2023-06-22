package space.impact.space.api.events

import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.ActiveRenderInfo
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.Potion
import net.minecraft.util.MathHelper
import net.minecraft.util.Vec3
import net.minecraft.world.World
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors
import net.minecraftforge.client.event.EntityViewRenderEvent.RenderFogEvent
import net.minecraftforge.common.ForgeModContainer
import org.lwjgl.opengl.GL11
import space.impact.space.api.world.atmosphere.PlanetFog

class EventFogHandler {

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    fun onGetFogColour(event: FogColors) {
        if (event.entity is EntityPlayer) {
            val player = event.entity as EntityPlayer
            val world = player.worldObj
            val x = MathHelper.floor_double(player.posX)
            val y = MathHelper.floor_double(player.posY)
            val z = MathHelper.floor_double(player.posZ)
            val blockAtEyes = ActiveRenderInfo.getBlockAtEntityViewpoint(world, event.entity, event.renderPartialTicks.toFloat())
            if (blockAtEyes.material == Material.lava) {
                return
            }
            val mixedColor = if (blockAtEyes.material == Material.water) {
                getFogBlendColorWater(world, player, x, y, z, event.renderPartialTicks)
            } else {
                getFogBlendColour(world, player, x, y, z, event.red, event.green, event.blue, event.renderPartialTicks)
            }
            event.red = mixedColor.xCoord.toFloat()
            event.green = mixedColor.yCoord.toFloat()
            event.blue = mixedColor.zCoord.toFloat()
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    fun onRenderFog(event: RenderFogEvent) {
        val entity: Entity = event.entity
        val world = entity.worldObj
        val playerX = MathHelper.floor_double(entity.posX)
        val playerY = MathHelper.floor_double(entity.posY)
        val playerZ = MathHelper.floor_double(entity.posZ)
        if (playerX.toDouble() == fogX && playerZ.toDouble() == fogZ && fogInit) {
            renderFog(event.fogMode, fogFarPlaneDistance, 0.75f)
        } else {
            fogInit = true
            val distance = 20
            var fpDistanceBiomeFog = 0.0f
            var weightBiomeFog = 0.0f
            var distancePart: Float
            var weightPart: Float
            for (x in -distance..distance) {
                for (z in -distance..distance) {
                    val provider = world.provider
                    if (provider is PlanetFog) {
                        distancePart = (provider as PlanetFog).getFogDensity(playerX + x, playerY, playerZ + z)
                        weightPart = 1.0f
                        var zDiff: Double
                        if (x == -distance) {
                            zDiff = 1.0 - (entity.posX - playerX.toDouble())
                            distancePart = (distancePart.toDouble() * zDiff).toFloat()
                            weightPart = (weightPart.toDouble() * zDiff).toFloat()
                        } else if (x == distance) {
                            zDiff = entity.posX - playerX.toDouble()
                            distancePart = (distancePart.toDouble() * zDiff).toFloat()
                            weightPart = (weightPart.toDouble() * zDiff).toFloat()
                        }
                        if (z == -distance) {
                            zDiff = 1.0 - (entity.posZ - playerZ.toDouble())
                            distancePart = (distancePart.toDouble() * zDiff).toFloat()
                            weightPart = (weightPart.toDouble() * zDiff).toFloat()
                        } else if (z == distance) {
                            zDiff = entity.posZ - playerZ.toDouble()
                            distancePart = (distancePart.toDouble() * zDiff).toFloat()
                            weightPart = (weightPart.toDouble() * zDiff).toFloat()
                        }
                        fpDistanceBiomeFog += distancePart
                        weightBiomeFog += weightPart
                    }
                }
            }
            val weightMixed = (distance * 2 * distance * 2).toFloat()
            val weightDefault = weightMixed - weightBiomeFog
            val fpDistanceBiomeFogAvg = if (weightBiomeFog == 0.0f) 0.0f else fpDistanceBiomeFog / weightBiomeFog
            distancePart = (fpDistanceBiomeFog * 240.0f + event.farPlaneDistance * weightDefault) / weightMixed
            weightPart = 0.1f * (1.0f - fpDistanceBiomeFogAvg) + 0.75f * fpDistanceBiomeFogAvg
            val farPlaneDistanceScale = (weightPart * weightBiomeFog + 0.75f * weightDefault) / weightMixed
            fogX = entity.posX
            fogZ = entity.posZ
            fogFarPlaneDistance = Math.min(distancePart, event.farPlaneDistance)
            renderFog(event.fogMode, fogFarPlaneDistance, farPlaneDistanceScale)
        }
    }

    companion object {
        private var fogX = 0.0
        private var fogZ = 0.0
        private var fogInit = false
        private var fogFarPlaneDistance = 0f

        private fun renderFog(fogMode: Int, farPlaneDistance: Float, farPlaneDistanceScale: Float) {
            if (fogMode < 0) {
                GL11.glFogf(2915, 0.0f)
                GL11.glFogf(2916, farPlaneDistance)
            } else {
                GL11.glFogf(2915, farPlaneDistance * farPlaneDistanceScale)
                GL11.glFogf(2916, farPlaneDistance)
            }
        }

        private fun postProcessColor(world: World, player: EntityLivingBase, r: Float, g: Float, b: Float, renderPartialTicks: Double): Vec3 {
            var r = r
            var g = g
            var b = b
            var darkScale = (player.lastTickPosY + (player.posY - player.lastTickPosY) * renderPartialTicks) * world.provider.voidFogYFactor
            var duration: Int
            if (player.isPotionActive(Potion.blindness)) {
                duration = player.getActivePotionEffect(Potion.blindness).duration
                darkScale *= if (duration < 20) (1.0f - duration.toFloat() / 20.0f).toDouble() else 0.0
            }
            if (darkScale < 1.0) {
                darkScale = if (darkScale < 0.0) 0.0 else darkScale * darkScale
                r = (r.toDouble() * darkScale).toFloat()
                g = (g.toDouble() * darkScale).toFloat()
                b = (b.toDouble() * darkScale).toFloat()
            }
            var aG: Float
            var aB: Float
            if (player.isPotionActive(Potion.nightVision)) {
                duration = player.getActivePotionEffect(Potion.nightVision).duration
                aG = if (duration > 200) 1.0f else 0.7f + MathHelper.sin(((duration.toDouble() - renderPartialTicks) * Math.PI * 0.20000000298023224).toFloat()) * 0.3f
                aB = 1.0f / r
                aB = Math.min(aB, 1.0f / g)
                aB = Math.min(aB, 1.0f / b)
                r = r * (1.0f - aG) + r * aB * aG
                g = g * (1.0f - aG) + g * aB * aG
                b = b * (1.0f - aG) + b * aB * aG
            }
            if (Minecraft.getMinecraft().gameSettings.anaglyph) {
                val aR = (r * 30.0f + g * 59.0f + b * 11.0f) / 100.0f
                aG = (r * 30.0f + g * 70.0f) / 100.0f
                aB = (r * 30.0f + b * 70.0f) / 100.0f
                r = aR
                g = aG
                b = aB
            }
            return Vec3.createVectorHelper(r.toDouble(), g.toDouble(), b.toDouble())
        }

        private fun getFogBlendColorWater(world: World, playerEntity: EntityLivingBase, playerX: Int, playerY: Int, playerZ: Int, renderPartialTicks: Double): Vec3 {
            val distance = 2
            var rBiomeFog = 0.0f
            var gBiomeFog = 0.0f
            var bBiomeFog = 0.0f
            var rPart: Float
            for (x in -distance..distance) {
                for (z in -distance..distance) {
                    val biome = world.getBiomeGenForCoords(playerX + x, playerZ + z)
                    val waterColorMult = biome.waterColorMultiplier
                    rPart = (waterColorMult and 16711680 shr 16).toFloat()
                    var gPart = (waterColorMult and '\uff00'.code shr 8).toFloat()
                    var bPart = (waterColorMult and 255).toFloat()
                    var zDiff: Double
                    if (x == -distance) {
                        zDiff = 1.0 - (playerEntity.posX - playerX.toDouble())
                        rPart = (rPart.toDouble() * zDiff).toFloat()
                        gPart = (gPart.toDouble() * zDiff).toFloat()
                        bPart = (bPart.toDouble() * zDiff).toFloat()
                    } else if (x == distance) {
                        zDiff = playerEntity.posX - playerX.toDouble()
                        rPart = (rPart.toDouble() * zDiff).toFloat()
                        gPart = (gPart.toDouble() * zDiff).toFloat()
                        bPart = (bPart.toDouble() * zDiff).toFloat()
                    }
                    if (z == -distance) {
                        zDiff = 1.0 - (playerEntity.posZ - playerZ.toDouble())
                        rPart = (rPart.toDouble() * zDiff).toFloat()
                        gPart = (gPart.toDouble() * zDiff).toFloat()
                        bPart = (bPart.toDouble() * zDiff).toFloat()
                    } else if (z == distance) {
                        zDiff = playerEntity.posZ - playerZ.toDouble()
                        rPart = (rPart.toDouble() * zDiff).toFloat()
                        gPart = (gPart.toDouble() * zDiff).toFloat()
                        bPart = (bPart.toDouble() * zDiff).toFloat()
                    }
                    rBiomeFog += rPart
                    gBiomeFog += gPart
                    bBiomeFog += bPart
                }
            }
            rBiomeFog /= 255.0f
            gBiomeFog /= 255.0f
            bBiomeFog /= 255.0f
            val weight = (distance * 2 * distance * 2).toFloat()
            val respirationLevel = EnchantmentHelper.getRespiration(playerEntity).toFloat() * 0.2f
            val rMixed = (rBiomeFog * 0.02f + respirationLevel) / weight
            val gMixed = (gBiomeFog * 0.02f + respirationLevel) / weight
            rPart = (bBiomeFog * 0.2f + respirationLevel) / weight
            return postProcessColor(world, playerEntity, rMixed, gMixed, rPart, renderPartialTicks)
        }

        private fun getFogBlendColour(world: World, playerEntity: EntityLivingBase, playerX: Int, playerY: Int, playerZ: Int, defR: Float, defG: Float, defB: Float, renderPartialTicks: Double): Vec3 {
            val settings = Minecraft.getMinecraft().gameSettings
            val ranges = ForgeModContainer.blendRanges
            var distance = 6
            if (settings.fancyGraphics && settings.renderDistanceChunks >= 0 && settings.renderDistanceChunks < ranges.size) {
                distance = ranges[settings.renderDistanceChunks]
            }
            var rBiomeFog = 0.0f
            var gBiomeFog = 0.0f
            var bBiomeFog = 0.0f
            var weightBiomeFog = 0.0f
            var rPart: Float
            var gPart: Float
            var weightPart: Float
            for (x in -distance..distance) {
                for (z in -distance..distance) {
                    val provider = world.provider
                    if (provider is PlanetFog) {
                        val biomeFog = provider as PlanetFog
                        val fogColour = biomeFog.getFogColor(playerX + x, playerY, playerZ + z)
                        rPart = (fogColour and 16711680 shr 16).toFloat()
                        gPart = (fogColour and '\uff00'.code shr 8).toFloat()
                        var bPart = (fogColour and 255).toFloat()
                        weightPart = 1.0f
                        var zDiff: Double
                        if (x == -distance) {
                            zDiff = 1.0 - (playerEntity.posX - playerX.toDouble())
                            rPart = (rPart.toDouble() * zDiff).toFloat()
                            gPart = (gPart.toDouble() * zDiff).toFloat()
                            bPart = (bPart.toDouble() * zDiff).toFloat()
                            weightPart = (weightPart.toDouble() * zDiff).toFloat()
                        } else if (x == distance) {
                            zDiff = playerEntity.posX - playerX.toDouble()
                            rPart = (rPart.toDouble() * zDiff).toFloat()
                            gPart = (gPart.toDouble() * zDiff).toFloat()
                            bPart = (bPart.toDouble() * zDiff).toFloat()
                            weightPart = (weightPart.toDouble() * zDiff).toFloat()
                        }
                        if (z == -distance) {
                            zDiff = 1.0 - (playerEntity.posZ - playerZ.toDouble())
                            rPart = (rPart.toDouble() * zDiff).toFloat()
                            gPart = (gPart.toDouble() * zDiff).toFloat()
                            bPart = (bPart.toDouble() * zDiff).toFloat()
                            weightPart = (weightPart.toDouble() * zDiff).toFloat()
                        } else if (z == distance) {
                            zDiff = playerEntity.posZ - playerZ.toDouble()
                            rPart = (rPart.toDouble() * zDiff).toFloat()
                            gPart = (gPart.toDouble() * zDiff).toFloat()
                            bPart = (bPart.toDouble() * zDiff).toFloat()
                            weightPart = (weightPart.toDouble() * zDiff).toFloat()
                        }
                        rBiomeFog += rPart
                        gBiomeFog += gPart
                        bBiomeFog += bPart
                        weightBiomeFog += weightPart
                    }
                }
            }
            return if (weightBiomeFog != 0.0f && distance != 0) {
                rBiomeFog /= 255.0f
                gBiomeFog /= 255.0f
                bBiomeFog /= 255.0f
                val celestialAngle = world.getCelestialAngle(renderPartialTicks.toFloat())
                val baseScale = MathHelper.clamp_float(MathHelper.cos(celestialAngle * Math.PI.toFloat() * 2.0f) * 2.0f + 0.5f, 0.0f, 1.0f)
                var rScale = baseScale * 0.94f + 0.06f
                var gScale = baseScale * 0.94f + 0.06f
                var bScale = baseScale * 0.91f + 0.09f
                rPart = 1.0f
                if (rPart > 0.0f) {
                    rScale *= 1.0f - rPart * 0.5f
                    gScale *= 1.0f - rPart * 0.5f
                    bScale *= 1.0f - rPart * 0.4f
                }
                gPart = world.getWeightedThunderStrength(renderPartialTicks.toFloat())
                if (gPart > 0.0f) {
                    rScale *= 1.0f - gPart * 0.5f
                    gScale *= 1.0f - gPart * 0.5f
                    bScale *= 1.0f - gPart * 0.5f
                }
                rBiomeFog *= rScale / weightBiomeFog
                gBiomeFog *= gScale / weightBiomeFog
                bBiomeFog *= bScale / weightBiomeFog
                val processedColor = postProcessColor(world, playerEntity, rBiomeFog, gBiomeFog, bBiomeFog, renderPartialTicks)
                rBiomeFog = processedColor.xCoord.toFloat()
                gBiomeFog = processedColor.yCoord.toFloat()
                bBiomeFog = processedColor.zCoord.toFloat()
                weightPart = (distance * 2 * distance * 2).toFloat()
                val weightDefault = weightPart - weightBiomeFog
                processedColor.xCoord = ((rBiomeFog * weightBiomeFog + defR * weightDefault) / weightPart).toDouble()
                processedColor.yCoord = ((gBiomeFog * weightBiomeFog + defG * weightDefault) / weightPart).toDouble()
                processedColor.zCoord = ((bBiomeFog * weightBiomeFog + defB * weightDefault) / weightPart).toDouble()
                processedColor
            } else {
                Vec3.createVectorHelper(defR.toDouble(), defG.toDouble(), defB.toDouble())
            }
        }
    }
}