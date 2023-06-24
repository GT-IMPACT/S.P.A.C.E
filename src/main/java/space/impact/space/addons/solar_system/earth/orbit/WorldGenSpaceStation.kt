package space.impact.space.addons.solar_system.earth.orbit

import net.minecraft.block.Block
import net.minecraft.init.Blocks
import net.minecraft.world.World
import net.minecraft.world.gen.feature.WorldGenerator
import space.impact.space.addons.solar_system.SolarSystem.EARTH_ORBIT_BLOCKS
import java.util.*

class WorldGenSpaceStation : WorldGenerator() {

    companion object {
        private var yellowZoneBlock: Block = EARTH_ORBIT_BLOCKS
        private var yellowZoneMeta = 0
        private var redZoneBlock: Block = EARTH_ORBIT_BLOCKS
        private var redZoneBlockMeta = 1
        private var redZoneGlass: Block = EARTH_ORBIT_BLOCKS
        private var redZoneGlassMeta = 2

        private const val O = 0
    }

    override fun generate(w: World, r: Random, x: Int, y: Int, z: Int): Boolean {
        this[w, x, y, z, yellowZoneBlock] = yellowZoneMeta
        yellowZone(w, x, y, z)
        redZone(w, x, y, z)
        return true
    }

    private operator fun set(w: World, x: Int, y: Int, z: Int, b: Block, m: Int) {
        w.setBlock(x, y, z, b, m, 3)
    }

    private fun yellowZone(w: World, x: Int, y: Int, z: Int) {
        val row = 5
        var zz: Int
        var yy: Int = -row
        while (yy <= row) {
            zz = -row
            while (zz <= row) {
                if ((yy != 0 || zz != 0) && (yy != row && yy != -row || zz != row && zz != -row)) {
                    this[w, x + yy, y, z + zz, yellowZoneBlock] = yellowZoneMeta
                }
                ++zz
            }
            ++yy
        }
        yy = -row
        while (yy <= row) {
            zz = -row
            while (zz <= row) {
                if ((yy < -1 || yy > 1 || zz < -1 || zz > 1) && (yy != row && yy != -row || zz != row && zz != -row)) {
                    this[w, x + yy, y + 6, z + zz, yellowZoneBlock] = yellowZoneMeta
                }
                ++zz
            }
            ++yy
        }
        yy = 1
        while (yy <= 6) {
            this[w, x - 5, y + yy, z + 3, yellowZoneBlock] = yellowZoneMeta
            this[w, x - 5, y + yy, z - 3, yellowZoneBlock] = yellowZoneMeta
            this[w, x + 5, y + yy, z + 3, yellowZoneBlock] = yellowZoneMeta
            this[w, x + 5, y + yy, z - 3, yellowZoneBlock] = yellowZoneMeta
            this[w, x - 3, y + yy, z + 5, yellowZoneBlock] = yellowZoneMeta
            this[w, x - 3, y + yy, z - 5, yellowZoneBlock] = yellowZoneMeta
            this[w, x + 3, y + yy, z + 5, yellowZoneBlock] = yellowZoneMeta
            this[w, x + 3, y + yy, z - 5, yellowZoneBlock] = yellowZoneMeta
            this[w, x + 4, y + yy, z + 4, yellowZoneBlock] = yellowZoneMeta
            this[w, x + 4, y + yy, z - 4, yellowZoneBlock] = yellowZoneMeta
            this[w, x - 4, y + yy, z + 4, yellowZoneBlock] = yellowZoneMeta
            this[w, x - 4, y + yy, z - 4, yellowZoneBlock] = yellowZoneMeta
            this[w, x - 4, y + yy, z + 3, yellowZoneBlock] = yellowZoneMeta
            this[w, x - 4, y + yy, z - 3, yellowZoneBlock] = yellowZoneMeta
            this[w, x + 4, y + yy, z + 3, yellowZoneBlock] = yellowZoneMeta
            this[w, x + 4, y + yy, z - 3, yellowZoneBlock] = yellowZoneMeta
            this[w, x - 3, y + yy, z + 4, yellowZoneBlock] = yellowZoneMeta
            this[w, x - 3, y + yy, z - 4, yellowZoneBlock] = yellowZoneMeta
            this[w, x + 3, y + yy, z + 4, yellowZoneBlock] = yellowZoneMeta
            this[w, x + 3, y + yy, z - 4, yellowZoneBlock] = yellowZoneMeta
            ++yy
        }
        yy = 0
        while (yy <= 6) {
            if (yy == 0 || yy == 6) {
                this[w, x + 4, y + yy, z + 5, Blocks.air] = 4
                this[w, x + 4, y + yy, z - 5, Blocks.air] = 4
                this[w, x - 4, y + yy, z + 5, Blocks.air] = 4
                this[w, x - 4, y + yy, z - 5, Blocks.air] = 4
                this[w, x - 5, y + yy, z + 4, Blocks.air] = 4
                this[w, x - 5, y + yy, z - 4, Blocks.air] = 4
                this[w, x + 5, y + yy, z + 4, Blocks.air] = 4
                this[w, x + 5, y + yy, z - 4, Blocks.air] = 4
                this[w, x + 4, y + yy, z + 5, Blocks.air] = 4
                this[w, x + 4, y + yy, z - 5, Blocks.air] = 4
                this[w, x - 4, y + yy, z + 5, Blocks.air] = 4
                this[w, x - 4, y + yy, z - 5, Blocks.air] = 4
                this[w, x - 5, y + yy, z + 4, Blocks.air] = 4
                this[w, x - 5, y + yy, z - 4, Blocks.air] = 4
                this[w, x + 5, y + yy, z + 4, Blocks.air] = 4
                this[w, x + 5, y + yy, z - 4, Blocks.air] = 4
                this[w, x - 5, y + yy, z + 3, Blocks.air] = 4
                this[w, x - 5, y + yy, z - 3, Blocks.air] = 4
                this[w, x + 5, y + yy, z + 3, Blocks.air] = 4
                this[w, x + 5, y + yy, z - 3, Blocks.air] = 4
                this[w, x - 3, y + yy, z + 5, Blocks.air] = 4
                this[w, x - 3, y + yy, z - 5, Blocks.air] = 4
                this[w, x + 3, y + yy, z + 5, Blocks.air] = 4
                this[w, x + 3, y + yy, z - 5, Blocks.air] = 4
                this[w, x - 4, y + yy, z + 4, Blocks.air] = 4
                this[w, x - 4, y + yy, z - 4, Blocks.air] = 4
                this[w, x + 4, y + yy, z + 4, Blocks.air] = 4
                this[w, x + 4, y + yy, z - 4, Blocks.air] = 4
            }
            ++yy
        }
    }

    private fun redZoneCorridor(w: World, x: Int, y: Int, z: Int, rotate: Boolean, minusCoords: Boolean) {
        var xx: Int
        var zz: Int
        if (rotate) {
            if (minusCoords) {
                xx = 0
                while (xx <= 4) {
                    zz = -3
                    while (zz <= 3) {
                        if (zz == -2 || zz == 2) {
                            this[w, x - xx, y + 1, z + zz, redZoneBlock] = redZoneBlockMeta
                            this[w, x - xx, y + 5, z + zz, redZoneBlock] = redZoneBlockMeta
                        }
                        if (xx != 0 && xx != 4) {
                            if (zz >= -1 && zz <= 1) {
                                this[w, x - xx, y + O, z + zz, redZoneGlass] = redZoneGlassMeta
                                this[w, x - xx, y + 6, z + zz, redZoneGlass] = redZoneGlassMeta
                            }
                            if (zz == -3 || zz == 3) {
                                this[w, x - xx, y + 4, z + zz, redZoneGlass] = redZoneGlassMeta
                                this[w, x - xx, y + 2, z + zz, redZoneGlass] = redZoneGlassMeta
                                this[w, x - xx, y + 3, z + zz, redZoneGlass] = redZoneGlassMeta
                            }
                        } else {
                            if (zz >= -1 && zz <= 1) {
                                this[w, x - xx, y + O, z + zz, redZoneBlock] = redZoneBlockMeta
                                this[w, x - xx, y + 6, z + zz, redZoneBlock] = redZoneBlockMeta
                            }
                            if (zz == -3 || zz == 3) {
                                this[w, x - xx, y + 4, z + zz, redZoneBlock] = redZoneBlockMeta
                                this[w, x - xx, y + 2, z + zz, redZoneBlock] = redZoneBlockMeta
                                this[w, x - xx, y + 3, z + zz, redZoneBlock] = redZoneBlockMeta
                            }
                        }
                        ++zz
                    }
                    ++xx
                }
            } else {
                xx = -4
                while (xx <= 0) {
                    zz = -3
                    while (zz <= 3) {
                        if (zz == -2 || zz == 2) {
                            this[w, x - xx, y + 1, z + zz, redZoneBlock] = redZoneBlockMeta
                            this[w, x - xx, y + 5, z + zz, redZoneBlock] = redZoneBlockMeta
                        }
                        if (xx != 0 && xx != -4) {
                            if (zz >= -1 && zz <= 1) {
                                this[w, x - xx, y + O, z + zz, redZoneGlass] = redZoneGlassMeta
                                this[w, x - xx, y + 6, z + zz, redZoneGlass] = redZoneGlassMeta
                            }
                            if (zz == -3 || zz == 3) {
                                this[w, x - xx, y + 4, z + zz, redZoneGlass] = redZoneGlassMeta
                                this[w, x - xx, y + 2, z + zz, redZoneGlass] = redZoneGlassMeta
                                this[w, x - xx, y + 3, z + zz, redZoneGlass] = redZoneGlassMeta
                            }
                        } else {
                            if (zz >= -1 && zz <= 1) {
                                this[w, x - xx, y + O, z + zz, redZoneBlock] = redZoneBlockMeta
                                this[w, x - xx, y + 6, z + zz, redZoneBlock] = redZoneBlockMeta
                            }
                            if (zz == -3 || zz == 3) {
                                this[w, x - xx, y + 4, z + zz, redZoneBlock] = redZoneBlockMeta
                                this[w, x - xx, y + 2, z + zz, redZoneBlock] = redZoneBlockMeta
                                this[w, x - xx, y + 3, z + zz, redZoneBlock] = redZoneBlockMeta
                            }
                        }
                        ++zz
                    }
                    ++xx
                }
            }
        } else if (minusCoords) {
            xx = 0
            while (xx <= 4) {
                zz = -3
                while (zz <= 3) {
                    if (zz == -2 || zz == 2) {
                        this[w, x - zz, y + 1, z + xx, redZoneBlock] = redZoneBlockMeta
                        this[w, x - zz, y + 5, z + xx, redZoneBlock] = redZoneBlockMeta
                    }
                    if (xx != 0 && xx != 4) {
                        if (zz >= -1 && zz <= 1) {
                            this[w, x - zz, y + O, z + xx, redZoneGlass] = redZoneGlassMeta
                            this[w, x - zz, y + 6, z + xx, redZoneGlass] = redZoneGlassMeta
                        }
                        if (zz == -3 || zz == 3) {
                            this[w, x - zz, y + 4, z + xx, redZoneGlass] = redZoneGlassMeta
                            this[w, x - zz, y + 2, z + xx, redZoneGlass] = redZoneGlassMeta
                            this[w, x - zz, y + 3, z + xx, redZoneGlass] = redZoneGlassMeta
                        }
                    } else {
                        if (zz >= -1 && zz <= 1) {
                            this[w, x - zz, y + O, z + xx, redZoneBlock] = redZoneBlockMeta
                            this[w, x - zz, y + 6, z + xx, redZoneBlock] = redZoneBlockMeta
                        }
                        if (zz == -3 || zz == 3) {
                            this[w, x - zz, y + 2, z + xx, redZoneBlock] = redZoneBlockMeta
                            this[w, x - zz, y + 3, z + xx, redZoneBlock] = redZoneBlockMeta
                            this[w, x - zz, y + 4, z + xx, redZoneBlock] = redZoneBlockMeta
                        }
                    }
                    ++zz
                }
                ++xx
            }
        } else {
            xx = -4
            while (xx <= 0) {
                zz = -3
                while (zz <= 3) {
                    if (zz == -2 || zz == 2) {
                        this[w, x - zz, y + 1, z + xx, redZoneBlock] = redZoneBlockMeta
                        this[w, x - zz, y + 5, z + xx, redZoneBlock] = redZoneBlockMeta
                    }
                    if (xx != 0 && xx != -4) {
                        if (zz >= -1 && zz <= 1) {
                            this[w, x - zz, y + O, z + xx, redZoneGlass] = redZoneGlassMeta
                            this[w, x - zz, y + 6, z + xx, redZoneGlass] = redZoneGlassMeta
                        }
                        if (zz == -3 || zz == 3) {
                            this[w, x - zz, y + 2, z + xx, redZoneGlass] = redZoneGlassMeta
                            this[w, x - zz, y + 3, z + xx, redZoneGlass] = redZoneGlassMeta
                            this[w, x - zz, y + 4, z + xx, redZoneGlass] = redZoneGlassMeta
                        }
                    } else {
                        if (zz >= -1 && zz <= 1) {
                            this[w, x - zz, y + O, z + xx, redZoneBlock] = redZoneBlockMeta
                            this[w, x - zz, y + 6, z + xx, redZoneBlock] = redZoneBlockMeta
                        }
                        if (zz == -3 || zz == 3) {
                            this[w, x - zz, y + 2, z + xx, redZoneBlock] = redZoneBlockMeta
                            this[w, x - zz, y + 3, z + xx, redZoneBlock] = redZoneBlockMeta
                            this[w, x - zz, y + 4, z + xx, redZoneBlock] = redZoneBlockMeta
                        }
                    }
                    ++zz
                }
                ++xx
            }
        }
    }

    private fun redZone(w: World, x: Int, y: Int, z: Int) {
        val startRedZone = 6
        val corridorRange = 4
        for (rowCorridor in 0..4) {
            redZoneCorridor(w, x + startRedZone + corridorRange * rowCorridor, y, z, rotate = true, minusCoords = false)
            redZoneCorridor(w, x - startRedZone - corridorRange * rowCorridor, y, z, rotate = true, minusCoords = true)
            redZoneCorridor(w, x, y, z - startRedZone - corridorRange * rowCorridor, rotate = false, minusCoords = false)
            redZoneCorridor(w, x, y, z + startRedZone + corridorRange * rowCorridor, rotate = false, minusCoords = true)
        }
    }
}
