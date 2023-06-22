package space.impact.space.addons.solar_system.venus.world

import net.minecraft.init.Blocks
import net.minecraft.world.World
import net.minecraft.world.gen.feature.WorldGenerator
import space.impact.space.addons.solar_system.SolarSystem
import space.impact.space.api.world.gen.biome.BiomeDecoratorSpaceBase
import space.impact.space.api.world.gen.generators.WorldGenSmallLakes

class BiomeDecoratorVenus : BiomeDecoratorSpaceBase() {

    private var world: World? = null

    private var lavaLakesGen: WorldGenerator? = null

    override fun decorate() {
        val world = getCurrentWorld() ?: return
        val rand = rand ?: return
        lavaLakesGen = WorldGenSmallLakes(Blocks.flowing_lava, SolarSystem.VENUS_BLOCKS, 2)
        if (world.rand.nextInt(10) == 0) {
            val var3 = chunkX + rand.nextInt(16) + 8
            val var5 = chunkZ + rand.nextInt(16) + 8
            val var4 = world.getTopSolidOrLiquidBlock(var3, var5)
            lavaLakesGen?.generate(world, rand, var3, var4, var5)
        }
    }

    override fun getCurrentWorld(): World? {
        return world
    }

    override fun setCurrentWorld(world: World?) {
        this.world = world
    }
}