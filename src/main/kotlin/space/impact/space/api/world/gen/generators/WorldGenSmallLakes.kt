package space.impact.space.api.world.gen.generators

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.init.Blocks
import net.minecraft.world.EnumSkyBlock
import net.minecraft.world.World
import net.minecraft.world.gen.feature.WorldGenerator
import java.util.*

class WorldGenSmallLakes(private val block: Block, private val spreadBlock: Block, private val spreadBlockMeta: Int) : WorldGenerator() {
    override fun generate(world: World, rand: Random, x: Int, y: Int, z: Int): Boolean {
        var x = x
        var y = y
        var z = z
        x -= 8
        z -= 8
        while (y > 5 && world.isAirBlock(x, y, z)) {
            --y
        }
        return if (y <= 4) {
            false
        } else {
            y -= 4
            val aboolean = BooleanArray(2048)
            val l = rand.nextInt(4) + 4
            var i1: Int
            i1 = 0
            while (i1 < l) {
                val d0 = rand.nextDouble() * 6.0 + 3.0
                val d1 = rand.nextDouble() * 4.0 + 2.0
                val d2 = rand.nextDouble() * 6.0 + 3.0
                val d3 = rand.nextDouble() * (16.0 - d0 - 2.0) + 1.0 + d0 / 2.0
                val d4 = rand.nextDouble() * (8.0 - d1 - 4.0) + 2.0 + d1 / 2.0
                val d5 = rand.nextDouble() * (16.0 - d2 - 2.0) + 1.0 + d2 / 2.0
                for (k1 in 1..14) {
                    for (l1 in 1..14) {
                        for (i2 in 1..6) {
                            val d6 = (k1.toDouble() - d3) / (d0 / 2.0)
                            val d7 = (i2.toDouble() - d4) / (d1 / 2.0)
                            val d8 = (l1.toDouble() - d5) / (d2 / 2.0)
                            val d9 = d6 * d6 + d7 * d7 + d8 * d8
                            if (d9 < 1.0) {
                                aboolean[(k1 * 16 + l1) * 8 + i2] = true
                            }
                        }
                    }
                }
                ++i1
            }
            var j2: Int
            var j1: Int
            var flag: Boolean
            i1 = 0
            while (i1 < 16) {
                j2 = 0
                while (j2 < 16) {
                    j1 = 0
                    while (j1 < 8) {
                        flag =
                            !aboolean[(i1 * 16 + j2) * 8 + j1] && (i1 < 15 && aboolean[((i1 + 1) * 16 + j2) * 8 + j1] || i1 > 0 && aboolean[((i1 - 1) * 16 + j2) * 8 + j1] || j2 < 15 && aboolean[(i1 * 16 + j2 + 1) * 8 + j1] || j2 > 0 && aboolean[(i1 * 16 + (j2 - 1)) * 8 + j1] || j1 < 7 && aboolean[(i1 * 16 + j2) * 8 + j1 + 1] || j1 > 0 && aboolean[(i1 * 16 + j2) * 8 + (j1 - 1)])
                        if (flag) {
                            val material = world.getBlock(x + i1, y + j1, z + j2).material
                            if (j1 >= 4 && material.isLiquid) {
                                return false
                            }
                            if (j1 < 4 && !material.isSolid && world.getBlock(x + i1, y + j1, z + j2) != block) {
                                return false
                            }
                        }
                        ++j1
                    }
                    ++j2
                }
                ++i1
            }
            i1 = 0
            while (i1 < 16) {
                j2 = 0
                while (j2 < 16) {
                    j1 = 0
                    while (j1 < 8) {
                        if (aboolean[(i1 * 16 + j2) * 8 + j1]) {
                            world.setBlock(x + i1, y + j1, z + j2, if (j1 >= 4) Blocks.air else block, 0, 2)
                        }
                        ++j1
                    }
                    ++j2
                }
                ++i1
            }
            i1 = 0
            while (i1 < 16) {
                j2 = 0
                while (j2 < 16) {
                    j1 = 4
                    while (j1 < 8) {
                        if (aboolean[(i1 * 16 + j2) * 8 + j1] && world.getBlock(x + i1, y + j1 - 1, z + j2) == Blocks.dirt && world.getSavedLightValue(EnumSkyBlock.Sky, x + i1, y + j1, z + j2) > 0) {
                            val biomegenbase = world.getBiomeGenForCoords(x + i1, z + j2)
                            if (biomegenbase.topBlock == Blocks.mycelium) {
                                world.setBlock(x + i1, y + j1 - 1, z + j2, Blocks.mycelium, 0, 2)
                            } else {
                                world.setBlock(x + i1, y + j1 - 1, z + j2, Blocks.grass, 0, 2)
                            }
                        }
                        ++j1
                    }
                    ++j2
                }
                ++i1
            }
            if (block.material == Material.lava) {
                i1 = 0
                while (i1 < 16) {
                    j2 = 0
                    while (j2 < 16) {
                        j1 = 0
                        while (j1 < 8) {
                            flag =
                                !aboolean[(i1 * 16 + j2) * 8 + j1] && (i1 < 15 && aboolean[((i1 + 1) * 16 + j2) * 8 + j1] || i1 > 0 && aboolean[((i1 - 1) * 16 + j2) * 8 + j1] || j2 < 15 && aboolean[(i1 * 16 + j2 + 1) * 8 + j1] || j2 > 0 && aboolean[(i1 * 16 + (j2 - 1)) * 8 + j1] || j1 < 7 && aboolean[(i1 * 16 + j2) * 8 + j1 + 1] || j1 > 0 && aboolean[(i1 * 16 + j2) * 8 + (j1 - 1)])
                            if (flag && (j1 < 4 || rand.nextInt(2) != 0) && world.getBlock(x + i1, y + j1, z + j2).material.isSolid) {
                                world.setBlock(x + i1, y + j1, z + j2, spreadBlock, spreadBlockMeta, 2)
                            }
                            ++j1
                        }
                        ++j2
                    }
                    ++i1
                }
            }
            if (block.material == Material.water) {
                i1 = 0
                while (i1 < 16) {
                    j2 = 0
                    while (j2 < 16) {
                        val b0: Byte = 4
                        if (world.isBlockFreezable(x + i1, y + b0, z + j2)) {
                            world.setBlock(x + i1, y + b0, z + j2, Blocks.ice, 0, 2)
                        }
                        ++j2
                    }
                    ++i1
                }
            }
            true
        }
    }
}