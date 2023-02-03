package space.impact.space.world.moons.earth_moon

import net.minecraft.init.Blocks
import net.minecraft.world.World
import net.minecraft.world.gen.feature.WorldGenerator
import space.impact.space.api.world.gen.biome.BiomeDecoratorSpaceBase
import space.impact.space.api.world.gen.world.WorldGenMinableMeta


class BiomeDecoratorMoonIce : BiomeDecoratorSpaceBase() {

    private var world: World? = null
    private val oreGenIce: WorldGenerator = WorldGenMinableMeta(Blocks.packed_ice, 0, 20, true, Blocks.stone, 0)
    private val oreGenWater: WorldGenerator =  WorldGenMinableMeta(Blocks.water, 0, 20, true, Blocks.stone, 0)

    override fun decorate() {
        generateOther(2, oreGenIce, 10, 60)
        generateOther(16, oreGenWater, 40, 75)
    }

    override fun getCurrentWorld(): World? {
        return world
    }

    override fun setCurrentWorld(world: World?) {
        this.world = world
    }
}