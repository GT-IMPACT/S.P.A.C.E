package space.impact.space.api.world.gen.layer.standart.help

import net.minecraft.block.Block
import net.minecraft.block.BlockSapling
import net.minecraft.block.material.Material
import net.minecraft.init.Blocks
import net.minecraft.util.Direction
import net.minecraft.world.World
import net.minecraft.world.gen.feature.WorldGenerator
import net.minecraftforge.common.util.ForgeDirection
import space.impact.space.config.Config.OVERFLOW_CYCLE_MAX
import java.util.*
import kotlin.math.abs

class TreeGen(doBlockNotify: Boolean) : WorldGenerator(doBlockNotify) {

    var bWood: Block = Blocks.log
    var bLeaves: Block = Blocks.leaves
    var bSapling: Block = Blocks.sapling
    var bVine: Block = Blocks.vine
    var bCocoa: Block = Blocks.cocoa
    var minTreeHeight = 4
    var metaWood = 0
    var metaLeaves = 0
    var vinesGrow = false
    val ab = ArrayList<Block>()
    val am = ArrayList<Material>()

    init {
        ab.add(Blocks.grass)
        ab.add(Blocks.dirt)
        ab.add(Blocks.log)
        ab.add(Blocks.log2)
        ab.add(Blocks.sapling)
        ab.add(Blocks.vine)
        am.add(Material.air)
        am.add(Material.leaves)
    }

    private fun cb(b: Block): Boolean {
        var i = 0
        while (i < ab.size) {
            if (b == ab[i]) {
                return true
            }
            ++i
        }
        i = 0
        while (i < am.size) {
            if (b.material == am[i]) {
                return true
            }
            ++i
        }
        return false
    }

    private fun isReplaceable(world: World, x: Int, y: Int, z: Int): Boolean {
        val b = world.getBlock(x, y, z)
        return b.isAir(world, x, y, z) || b.isLeaves(world, x, y, z) || b.isWood(world, x, y, z) || cb(b)
    }

    private fun growVines(world: World, x: Int, y: Int, z: Int, meta: Int) {
        var yy = y
        setBlockAndNotifyAdequately(world, x, yy, z, bVine, meta)
        var i1 = 4

        var counterOverflow = 0
        while (true) {
            --yy
            if (!world.getBlock(x, yy, z).isAir(world, x, yy, z) || i1 <= 0) {
                return
            }
            setBlockAndNotifyAdequately(world, x, yy, z, bVine, meta)
            --i1
            counterOverflow++
            if (counterOverflow > OVERFLOW_CYCLE_MAX) break
        }
    }

    override fun generate(world: World, random: Random, xx: Int, yy: Int, zz: Int): Boolean {
        val l = random.nextInt(3) + minTreeHeight
        var flag = true

        if (yy >= 1 && yy + l + 1 <= 256) {
            var b0: Byte
            var k1: Int

            for (i1 in yy..yy + 1 + l) {
                b0 = 1
                if (i1 == yy) {
                    b0 = 0
                }
                if (i1 >= yy + 1 + l - 2) {
                    b0 = 2
                }
                var j1 = xx - b0
                while (j1 <= xx + b0 && flag) {
                    k1 = zz - b0
                    while (k1 <= zz + b0 && flag) {
                        if (i1 in 0..255) {
                            world.getBlock(j1, i1, k1)
                            if (!isReplaceable(world, j1, i1, k1)) {
                                flag = false
                            }
                        } else {
                            flag = false
                        }
                        ++k1
                    }
                    ++j1
                }
            }

            if (!flag) {
                return false
            } else {
                val block2 = world.getBlock(xx, yy - 1, zz)
                val isSoil = block2.canSustainPlant(world, xx, yy - 1, zz, ForgeDirection.UP, bSapling as BlockSapling)

                if (isSoil && yy < 256 - l - 1) {
                    block2.onPlantGrow(world, xx, yy - 1, zz, xx, yy, zz)
                    b0 = 3
                    val b1: Byte = 0

                    var l1: Int
                    var i2: Int
                    var j2: Int
                    var i3: Int

                    k1 = yy - b0 + l
                    while (k1 <= yy + l) {
                        i3 = k1 - yy - l
                        l1 = b1 + 1 - i3 / 2
                        i2 = xx - l1
                        while (i2 <= xx + l1) {
                            j2 = i2 - xx
                            for (k2 in zz - l1..zz + l1) {
                                val l2 = k2 - zz
                                if (abs(j2) != l1 || abs(l2) != l1 || random.nextInt(2) != 0 && i3 != 0) {
                                    val block1 = world.getBlock(i2, k1, k2)
                                    if (block1.isAir(world, i2, k1, k2) || block1.isLeaves(world, i2, k1, k2)) {
                                        setBlockAndNotifyAdequately(world, i2, k1, k2, bLeaves, metaLeaves)
                                    }
                                }
                            }
                            ++i2
                        }
                        ++k1
                    }

                    k1 = 0
                    while (k1 < l) {
                        val block = world.getBlock(xx, yy + k1, zz)
                        if (block.isAir(world, xx, yy + k1, zz) || block.isLeaves(world, xx, yy + k1, zz)) {
                            setBlockAndNotifyAdequately(world, xx, yy + k1, zz, bWood, metaWood)
                            if (vinesGrow && k1 > 0) {
                                if (random.nextInt(3) > 0 && world.isAirBlock(xx - 1, yy + k1, zz)) {
                                    setBlockAndNotifyAdequately(world, xx - 1, yy + k1, zz, bVine, 8)
                                }
                                if (random.nextInt(3) > 0 && world.isAirBlock(xx + 1, yy + k1, zz)) {
                                    setBlockAndNotifyAdequately(world, xx + 1, yy + k1, zz, bVine, 2)
                                }
                                if (random.nextInt(3) > 0 && world.isAirBlock(xx, yy + k1, zz - 1)) {
                                    setBlockAndNotifyAdequately(world, xx, yy + k1, zz - 1, bVine, 1)
                                }
                                if (random.nextInt(3) > 0 && world.isAirBlock(xx, yy + k1, zz + 1)) {
                                    setBlockAndNotifyAdequately(world, xx, yy + k1, zz + 1, bVine, 4)
                                }
                            }
                        }
                        ++k1
                    }

                    if (vinesGrow) {
                        k1 = yy - 3 + l
                        while (k1 <= yy + l) {
                            i3 = k1 - yy - l
                            l1 = 2 - i3 / 2
                            i2 = xx - l1
                            while (i2 <= xx + l1) {
                                j2 = zz - l1
                                while (j2 <= zz + l1) {
                                    if (world.getBlock(i2, k1, j2).isLeaves(world, i2, k1, j2)) {
                                        if (random.nextInt(4) == 0 && world.getBlock(i2 - 1, k1, j2).isAir(world, i2 - 1, k1, j2)) {
                                            growVines(world, i2 - 1, k1, j2, 8)
                                        }
                                        if (random.nextInt(4) == 0 && world.getBlock(i2 + 1, k1, j2).isAir(world, i2 + 1, k1, j2)) {
                                            growVines(world, i2 + 1, k1, j2, 2)
                                        }
                                        if (random.nextInt(4) == 0 && world.getBlock(i2, k1, j2 - 1).isAir(world, i2, k1, j2 - 1)) {
                                            growVines(world, i2, k1, j2 - 1, 1)
                                        }
                                        if (random.nextInt(4) == 0 && world.getBlock(i2, k1, j2 + 1).isAir(world, i2, k1, j2 + 1)) {
                                            growVines(world, i2, k1, j2 + 1, 4)
                                        }
                                    }
                                    ++j2
                                }
                                ++i2
                            }
                            ++k1
                        }
                        if (random.nextInt(5) == 0 && l > 5) {
                            k1 = 0
                            while (k1 < 2) {
                                i3 = 0
                                while (i3 < 4) {
                                    if (random.nextInt(4 - k1) == 0) {
                                        l1 = random.nextInt(3)
                                        setBlockAndNotifyAdequately(
                                            world,
                                            xx + Direction.offsetX[Direction.rotateOpposite[i3]],
                                            yy + l - 5 + k1,
                                            zz + Direction.offsetZ[Direction.rotateOpposite[i3]],
                                            bCocoa,
                                            l1 shl 2 or i3
                                        )
                                    }
                                    ++i3
                                }
                                ++k1
                            }
                        }
                    }
                    return true
                } else {
                    return false
                }
            }
        } else {
            return false
        }
    }
}