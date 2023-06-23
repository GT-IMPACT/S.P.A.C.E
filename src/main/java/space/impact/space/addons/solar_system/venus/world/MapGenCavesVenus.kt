package space.impact.space.addons.solar_system.venus.world

import net.minecraft.block.Block
import net.minecraft.init.Blocks
import net.minecraft.util.MathHelper
import net.minecraft.world.World
import space.impact.space.addons.solar_system.SolarSystem
import space.impact.space.api.world.gen.world.MapGenMetaBase
import java.util.*

@Suppress("NAME_SHADOWING")
class MapGenCavesVenus : MapGenMetaBase() {
    protected fun generateLargeCaveNode(par1: Long, par3: Int, par4: Int, blockIdArray: Array<Block?>, metaArray: ByteArray, par6: Double, par8: Double, par10: Double) {
        generateCaveNode(par1, par3, par4, blockIdArray, metaArray, par6, par8, par10, 1.0f + rand.nextFloat() * 6.0f, 0.0f, 0.0f, -1, -1, 0.5)
    }

    protected fun generateCaveNode(
        par1: Long, par3: Int, par4: Int, blockIdArray: Array<Block?>, metaArray: ByteArray,
        par6: Double, par8: Double, par10: Double, par12: Float,
        par13: Float, par14: Float, par15: Int, par16: Int, par17: Double
    ) {
        var par6 = par6
        var par8 = par8
        var par10 = par10
        var par13 = par13
        var par14 = par14
        var par15 = par15
        var par16 = par16
        val d4 = (par3 * 16 + 8).toDouble()
        val d5 = (par4 * 16 + 8).toDouble()
        var f3 = 0.0f
        var f4 = 0.0f
        val random = Random(par1)
        if (par16 <= 0) {
            val j1 = range * 16 - 16
            par16 = j1 - random.nextInt(j1 / 4)
        }
        var flag = false
        if (par15 == -1) {
            par15 = par16 / 2
            flag = true
        }
        val k1 = random.nextInt(par16 / 2) + par16 / 4
        val flag1 = random.nextInt(6) == 0
        while (par15 < par16) {
            val d6 = 1.5 + (MathHelper.sin(par15.toFloat() * Math.PI.toFloat() / par16.toFloat()) * par12 * 1.0f).toDouble()
            val d7 = d6 * par17
            val f5 = MathHelper.cos(par14)
            val f6 = MathHelper.sin(par14)
            par6 += (MathHelper.cos(par13) * f5).toDouble()
            par8 += f6.toDouble()
            par10 += (MathHelper.sin(par13) * f5).toDouble()
            par14 *= if (flag1) {
                0.92f
            } else {
                0.7f
            }
            par14 += f4 * 0.1f
            par13 += f3 * 0.1f
            f4 *= 0.9f
            f3 *= 0.75f
            f4 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0f
            f3 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0f
            if (!flag && par15 == k1 && par12 > 1.0f && par16 > 0) {
                generateCaveNode(random.nextLong(), par3, par4, blockIdArray, metaArray, par6, par8, par10, random.nextFloat() * 0.5f + 0.5f, par13 - 1.5707964f, par14 / 3.0f, par15, par16, 1.0)
                generateCaveNode(random.nextLong(), par3, par4, blockIdArray, metaArray, par6, par8, par10, random.nextFloat() * 0.5f + 0.5f, par13 + 1.5707964f, par14 / 3.0f, par15, par16, 1.0)
                return
            }
            if (flag || random.nextInt(4) != 0) {
                val d8 = par6 - d4
                val d9 = par10 - d5
                val d10 = (par16 - par15).toDouble()
                val d11 = (par12 + 2.0f + 16.0f).toDouble()
                if (d8 * d8 + d9 * d9 - d10 * d10 > d11 * d11) {
                    return
                }
                if (par6 >= d4 - 16.0 - d6 * 2.0 && par10 >= d5 - 16.0 - d6 * 2.0 && par6 <= d4 + 16.0 + d6 * 2.0 && par10 <= d5 + 16.0 + d6 * 2.0) {
                    var l1 = MathHelper.floor_double(par6 - d6) - par3 * 16 - 1
                    var i2 = MathHelper.floor_double(par6 + d6) - par3 * 16 + 1
                    var j2 = MathHelper.floor_double(par8 - d7) - 1
                    var k2 = MathHelper.floor_double(par8 + d7) + 1
                    var l2 = MathHelper.floor_double(par10 - d6) - par4 * 16 - 1
                    var i3 = MathHelper.floor_double(par10 + d6) - par4 * 16 + 1
                    if (l1 < 0) {
                        l1 = 0
                    }
                    if (i2 > 16) {
                        i2 = 16
                    }
                    if (j2 < 1) {
                        j2 = 1
                    }
                    if (k2 > 120) {
                        k2 = 120
                    }
                    if (l2 < 0) {
                        l2 = 0
                    }
                    if (i3 > 16) {
                        i3 = 16
                    }
                    var localY: Int
                    for (j3 in l1 until i2) {
                        localY = l2
                        while (localY < i3) {
                            var i4 = k2 + 1
                            while (i4 >= j2 - 1) {
                                if (i4 in 0..127 && i4 != j2 - 1 && j3 != l1 && j3 != i2 - 1 && localY != l2 && localY != i3 - 1) {
                                    i4 = j2
                                }
                                --i4
                            }
                            ++localY
                        }
                    }
                    localY = j2
                    while (localY < k2) {
                        val yfactor = (localY.toDouble() + 0.5 - par8) / d7
                        val yfactorSq = yfactor * yfactor
                        for (localX in l1 until i2) {
                            val zfactor = ((localX + par3 * 16).toDouble() + 0.5 - par6) / d6
                            val zfactorSq = zfactor * zfactor
                            for (localZ in l2 until i3) {
                                val xfactor = ((localZ + par4 * 16).toDouble() + 0.5 - par10) / d6
                                val xfactorSq = xfactor * xfactor
                                if (xfactorSq + zfactorSq < 1.0) {
                                    val coords = (localX * 16 + localZ) * 256 + localY
                                    if (yfactor > -0.7 && xfactorSq + yfactorSq + zfactorSq < 1.0) {
                                        if (blockIdArray[coords] == SolarSystem.VENUS_BLOCKS && metaArray[coords].toInt() == 0 && random.nextInt(50) == 0) {
                                            blockIdArray[coords] = Blocks.air
                                        } else if (blockIdArray[coords] == SolarSystem.VENUS_BLOCKS && metaArray[coords].toInt() == 1) {
                                            blockIdArray[coords] = Blocks.air
                                        }
                                    }
                                }
                            }
                        }
                        ++localY
                    }
                    if (flag) {
                        break
                    }
                }
            }
            ++par15
        }
    }

    override fun recursiveGenerate(world: World, xChunkCoord: Int, zChunkCoord: Int, origXChunkCoord: Int, origZChunkCoord: Int, blocks: Array<Block?>, metadata: ByteArray) {
        var var7 = rand.nextInt(rand.nextInt(rand.nextInt(40) + 1) + 1)
        if (rand.nextInt(15) != 0) {
            var7 = 0
        }
        for (var8 in 0 until var7) {
            val var9 = (xChunkCoord * 16 + rand.nextInt(16)).toDouble()
            val var11 = rand.nextInt(rand.nextInt(150) + 8).toDouble()
            val var13 = (zChunkCoord * 16 + rand.nextInt(16)).toDouble()
            var var15 = 1
            if (rand.nextInt(4) == 0) {
                generateLargeCaveNode(rand.nextLong(), origXChunkCoord, origZChunkCoord, blocks, metadata, var9, var11, var13)
                var15 += rand.nextInt(4)
            }
            for (var16 in 0 until var15) {
                val var17 = rand.nextFloat() * Math.PI.toFloat()* 2.0f
                val var18 = (rand.nextFloat() - 0.5f) * 2.0f / 8.0f
                var var19 = rand.nextFloat() * 2.0f + rand.nextFloat()
                if (rand.nextInt(10) == 0) {
                    var19 *= rand.nextFloat() * rand.nextFloat() * 3.0f + 1.0f
                }
                generateCaveNode(rand.nextLong(), origXChunkCoord, origZChunkCoord, blocks, metadata, var9, var11, var13, var19, var17, var18, 0, 0, 1.0)
            }
        }
    }
}