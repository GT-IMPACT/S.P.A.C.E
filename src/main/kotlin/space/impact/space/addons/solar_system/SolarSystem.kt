package space.impact.space.addons.solar_system

import net.minecraftforge.common.DimensionManager
import space.impact.space.addons.solar_system.earth.moons.moon.world.WorldProviderMoon
import space.impact.space.addons.solar_system.jupiter.moons.europa.world.WorldProviderEuropa
import space.impact.space.api.block.BasicBlock
import space.impact.space.api.block.GeyserBlock
import space.impact.space.api.core.SystemRegister

object SolarSystem : SystemRegister {

    override val name: String = "solar_system"

    val EUROPA_BLOCKS = BasicBlock(name, "europa_rock", "europa", listOf("grunt", "stone", "brown_ice"))
    val EUROPA_GEYSERS = GeyserBlock(name, "europa_geyser", "europa", listOf("over_water", "under_water"))

    val MOON_BLOCKS = BasicBlock(name, "moon_rocks", "moon", listOf("grunt", "stone", "deep_stone"))

    override fun register() {

        DimensionManager.registerProviderType(2, WorldProviderEuropa::class.java, true)
        DimensionManager.registerDimension(2, 2)

        DimensionManager.registerProviderType(3, WorldProviderMoon::class.java, true)
        DimensionManager.registerDimension(3, 3)


    }
}