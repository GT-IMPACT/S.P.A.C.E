package space.impact.space.addons.solar_system.jupiter.moons.europa.world

import net.minecraft.block.Block
import net.minecraft.init.Blocks
import net.minecraft.util.MathHelper
import net.minecraft.world.World
import net.minecraft.world.biome.BiomeGenBase
import space.impact.space.addons.solar_system.SolarSystem
import space.impact.space.api.world.gen.world.MapGenMetaBase
import java.util.*

class MapGenRavineEuropa : MapGenMetaBase() {

    companion object {
        val TARGET_BLOCK: Block = SolarSystem.EUROPA_BLOCKS
    }

    private val rs = FloatArray(1024)

    protected fun addTunnel(
        seed: Long, x: Int, z: Int, block: Array<Block?>,
        offsetX: Double, offsetY: Double, offsetZ: Double,
        f1: Float, angle: Float, f2: Float,
        i1: Int, i2: Int, multiply: Double
    ) {
        var offsetXX = offsetX
        var offsetYY = offsetY
        var offsetZZ = offsetZ
        var angle1 = angle
        var ff2 = f2
        var ii1 = i1
        var ii2 = i2

        val rand = Random(seed)
        val d4 = (x * 16 + 8).toDouble()
        val d5 = (z * 16 + 8).toDouble()
        var f3 = 0.0f
        var f4 = 0.0f
        if (ii2 <= 0) {
            val j1 = range * 460 - 16
            ii2 = j1 - rand.nextInt(j1 / 4)
        }
        var flag1 = false
        if (ii1 == -1) {
            ii1 = ii2 / 2
            flag1 = true
        }
        var f5 = 1.0f
        for (k1 in 0..255) {
            if (k1 == 0 || rand.nextInt(3) == 0) {
                f5 = 1.0f + rand.nextFloat() * rand.nextFloat() * 1.0f
            }
            rs[k1] = f5 * f5
        }
        while (ii1 < ii2) {
            var d12 = 1.5 + (MathHelper.sin(ii1.toFloat() * 3.1415927f / ii2.toFloat()) * f1 * 1.0f).toDouble()
            var d6 = d12 * multiply
            d12 *= rand.nextFloat().toDouble() * 0.25 + 0.75
            d6 *= rand.nextFloat().toDouble() * 0.25 + 0.75
            val f6 = MathHelper.cos(ff2)
            val f7 = MathHelper.sin(ff2)
            offsetXX += (MathHelper.cos(angle1) * f6).toDouble()
            offsetYY += f7.toDouble()
            offsetZZ += (MathHelper.sin(angle1) * f6).toDouble()
            ff2 *= 0.7f
            ff2 += f4 * 0.05f
            angle1 += f3 * 0.05f
            f4 *= 0.8f
            f3 *= 0.5f
            f4 += (rand.nextFloat() - rand.nextFloat()) * rand.nextFloat() * 2.0f
            f3 += (rand.nextFloat() - rand.nextFloat()) * rand.nextFloat() * 4.0f
            if (flag1 || rand.nextInt(4) != 0) {
                val d7 = offsetXX - d4
                val d8 = offsetZZ - d5
                val d9 = (ii2 - ii1).toDouble()
                val d10 = (f1 + 2.0f + 16.0f).toDouble()
                if (d7 * d7 + d8 * d8 - d9 * d9 > d10 * d10) {
                    return
                }
                if (offsetXX >= d4 - 16.0 - d12 * 2.0 && offsetZZ >= d5 - 16.0 - d12 * 2.0 && offsetXX <= d4 + 16.0 + d12 * 2.0 && offsetZZ <= d5 + 16.0 + d12 * 2.0) {
                    var i4 = MathHelper.floor_double(offsetXX - d12) - x * 16 - 1
                    var l1 = MathHelper.floor_double(offsetXX + d12) - x * 16 + 1
                    var j4 = MathHelper.floor_double(offsetYY - d6) - 1
                    var i2 = MathHelper.floor_double(offsetYY + d6) + 1
                    var k4 = MathHelper.floor_double(offsetZZ - d12) - z * 16 - 1
                    var j2 = MathHelper.floor_double(offsetZZ + d12) - z * 16 + 1
                    if (i4 < 0) {
                        i4 = 0
                    }
                    if (l1 > 16) {
                        l1 = 16
                    }
                    if (j4 < 1) {
                        j4 = 1
                    }
                    if (i2 > 248) {
                        i2 = 248
                    }
                    if (k4 < 0) {
                        k4 = 0
                    }
                    if (j2 > 16) {
                        j2 = 16
                    }
                    var flag2 = false
                    var k2: Int
                    var j3: Int
                    k2 = i4
                    while (!flag2 && k2 < l1) {
                        var l2 = k4
                        while (!flag2 && l2 < j2) {
                            var i3 = i2 + 1
                            while (!flag2 && i3 >= j4 - 1) {
                                j3 = (k2 * 16 + l2) * 256 + i3
                                if (i3 in 0 until 255) {
                                    if (isOceanBlock(block, j3, k2, i3, l2, x, z)) {
                                        flag2 = true
                                    }
                                    if (i3 != j4 - 1 && k2 != i4 && k2 != l1 - 1 && l2 != k4 && l2 != j2 - 1) {
                                        i3 = j4
                                    }
                                }
                                --i3
                            }
                            ++l2
                        }
                        ++k2
                    }
                    if (!flag2) {
                        k2 = i4
                        while (k2 < l1) {
                            val d13 = ((k2 + x * 16).toDouble() + 0.5 - offsetXX) / d12
                            j3 = k4
                            while (j3 < j2) {
                                val d14 = ((j3 + z * 16).toDouble() + 0.5 - offsetZZ) / d12
                                var k3 = (k2 * 16 + j3) * 256 + i2
                                var flag = false
                                if (d13 * d13 + d14 * d14 < 1.0) {
                                    for (l3 in i2 - 1 downTo j4) {
                                        val d11 = (l3.toDouble() + 0.5 - offsetYY) / d6
                                        if ((d13 * d13 + d14 * d14) * rs[l3].toDouble() + d11 * d11 / 6.0 < 1.0) {
                                            if (isTopBlock(block, k3, k2, l3, j3, x, z)) {
                                                flag = true
                                            }
                                            digBlock(block, k3, k2, l3, j3, x, z, flag)
                                        }
                                        --k3
                                    }
                                }
                                ++j3
                            }
                            ++k2
                        }
                        if (flag1) {
                            break
                        }
                    }
                }
            }
            ++ii1
        }
    }

    override fun recursiveGenerate(world: World, xChunkCoord: Int, zChunkCoord: Int, origXChunkCoord: Int, origZChunkCoord: Int, blocks: Array<Block?>, metadata: ByteArray) {
        if (rand.nextInt(30) == 0) {
            val offsetX = (xChunkCoord * 16 + rand.nextInt(16)).toDouble()
            val offsetY = (rand.nextInt(10) + 90).toDouble()
            val offsetZ = (zChunkCoord * 16 + rand.nextInt(16)).toDouble()
            val b0: Byte = 1
            for (i1 in 0 until b0) {
                val angle = rand.nextFloat() * Math.PI.toFloat() * 2.0f
                val f1 = (rand.nextFloat() - 0.5f) * 2.0f / 8.0f
                val f2 = (rand.nextFloat() * 2.0f + rand.nextFloat()) * 2.0f
                this.addTunnel(rand.nextLong(), origXChunkCoord, origZChunkCoord, blocks, offsetX, offsetY, offsetZ, f2, angle, f1, 0, 0, 6.0)
            }
        }
    }

    protected fun isOceanBlock(data: Array<Block?>, index: Int, x: Int, y: Int, z: Int, chunkX: Int, chunkZ: Int): Boolean {
        return data[index] == Blocks.water || data[index] == Blocks.flowing_water
    }

    private fun isTopBlock(data: Array<Block?>, index: Int, x: Int, y: Int, z: Int, chunkX: Int, chunkZ: Int): Boolean {
        val biome: BiomeGenBase = this.world?.getBiomeGenForCoords(x + chunkX * 16, z + chunkZ * 16) ?: return false
        return data[index] == biome.topBlock
    }

    protected fun digBlock(data: Array<Block?>, index: Int, x: Int, y: Int, z: Int, chunkX: Int, chunkZ: Int, foundTop: Boolean) {
        val biome: BiomeGenBase = this.world?.getBiomeGenForCoords(x + chunkX * 16, z + chunkZ * 16) ?: return
        val top = biome.topBlock
        val filler = biome.fillerBlock
        val block = data[index]
        if (block == Blocks.packed_ice || block == TARGET_BLOCK || block == filler || block == top) {
            data[index] = null
            if (foundTop && data[index - 1] == filler) {
                data[index - 1] = top
            }
        }
    }
}