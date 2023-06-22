package space.impact.space.addons.solar_system.jupiter.moons.europa.world

import net.minecraft.block.material.Material
import net.minecraft.init.Blocks
import net.minecraft.world.World
import net.minecraft.world.gen.feature.WorldGenerator
import space.impact.space.addons.solar_system.SolarSystem
import space.impact.space.api.world.gen.biome.BiomeDecoratorSpaceBase
import space.impact.space.api.world.gen.world.WorldGenMinableMeta

class BiomeDecoratorEuropaOre : BiomeDecoratorSpaceBase() {

    companion object {
        private val BLOCK_SURFACE = SolarSystem.EUROPA_BLOCKS
        private val BLOCK_GEYSER = SolarSystem.EUROPA_GEYSERS
    }

    private var world: World? = null
    private val oeGenIce: WorldGenerator = WorldGenMinableMeta(Blocks.packed_ice, 0, 20, true, Blocks.water, 0)
    private val oreGenBrownIce: WorldGenerator = WorldGenMinableMeta(BLOCK_SURFACE, 2, 40, true, Blocks.packed_ice, 0)

    override fun decorate() {
        generateOther(2, oeGenIce, 10, 60);
        generateOther(16, oreGenBrownIce, 40, 75);


        val world = getCurrentWorld() ?: return
        val rand = rand ?: return

        var randPosX: Int
        var randPosY: Int
        var randPosZ: Int
        var i = 0
        while (i < 400) {
            randPosX = chunkX + rand.nextInt(16)
            randPosY = rand.nextInt(70)
            randPosZ = chunkZ + rand.nextInt(16)
            if ((world.isAirBlock(randPosX, randPosY + 1, randPosZ)
                        || world.getBlock(randPosX, randPosY + 1, randPosZ).material == Material.water)
                && (world.getBlock(randPosX, randPosY, randPosZ) == Blocks.packed_ice
                        || world.getBlock(randPosX, randPosY, randPosZ) == BLOCK_SURFACE
                        && world.getBlockMetadata(randPosX, randPosY, randPosZ) == 1)
            ) {
                world.setBlock(randPosX, randPosY, randPosZ, BLOCK_GEYSER, 0, 3)
            }
            ++i
        }

        i = 0
        while (i < 40) {
            randPosX = chunkX + rand.nextInt(16)
            randPosY = rand.nextInt(100)
            randPosZ = chunkZ + rand.nextInt(16)
            if (world.isAirBlock(randPosX, randPosY + 1, randPosZ)
                && !world.isAirBlock(randPosX + 1, randPosY, randPosZ)
                && !world.isAirBlock(randPosX - 1, randPosY, randPosZ)
                && !world.isAirBlock(randPosX, randPosY, randPosZ + 1)
                && !world.isAirBlock(randPosX, randPosY, randPosZ - 1)
            ) {
                val block = world.getBlock(randPosX, randPosY, randPosZ)
                val meta = world.getBlockMetadata(randPosX, randPosY, randPosZ)
                val isAccessGenerateGeyser = block == Blocks.packed_ice || block == BLOCK_SURFACE && meta == 0

                if (isAccessGenerateGeyser) {
                    world.setBlock(randPosX, randPosY, randPosZ, BLOCK_GEYSER, 1, 3)
                    world.setBlock(randPosX, randPosY - 1, randPosZ, Blocks.water)
                    world.setBlock(randPosX, randPosY - 2, randPosZ, Blocks.water)
                    world.setBlock(randPosX, randPosY - 3, randPosZ, Blocks.water)
                    world.setBlock(randPosX, randPosY - 4, randPosZ, Blocks.water)
                }
            }
            ++i
        }
    }

    override fun setCurrentWorld(world: World?) {
        this.world = world
    }

    override fun getCurrentWorld(): World? {
        return world
    }
}