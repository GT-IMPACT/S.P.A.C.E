package space.impact.space.addons.solar_system

import space.impact.space.addons.solar_system.earth.moons.moon.world.WorldProviderMoon
import space.impact.space.addons.solar_system.earth.orbit.WorldProviderEarthOrbit
import space.impact.space.addons.solar_system.jupiter.moons.europa.world.WorldProviderEuropa
import space.impact.space.addons.solar_system.mars.WorldProviderMars
import space.impact.space.addons.solar_system.venus.world.WorldProviderVenus
import space.impact.space.api.block.BasicBlock
import space.impact.space.api.block.GeyserBlock
import space.impact.space.api.core.SystemRegister
import space.impact.space.utils.Registers

object SolarSystem : SystemRegister {

    override val name: String = "solar_system"

    val EUROPA_BLOCKS = BasicBlock(name, "europa_rock", "europa", listOf("grunt", "stone", "brown_ice"))
    val EUROPA_GEYSERS = GeyserBlock(name, "europa_geyser", "europa", listOf("over_water", "under_water"))

    val MOON_BLOCKS = BasicBlock(name, "moon_rocks", "moon", listOf("grunt", "stone", "deep_stone"))
    val EARTH_ORBIT_BLOCKS = BasicBlock(name, "earth_orbit_lab_blocks", "earth", listOf("lab1", "lab2", "lab3"))

    val VENUS_BLOCKS = BasicBlock(name, "venus_rocks", "venus", listOf("grunt", "stone", "volcanic"))
    val MARS_BLOCKS = BasicBlock(name, "mars_rocks", "mars", listOf("grunt", "stone", "deep_stone"))


    override fun register() {
        Registers.registerPlanet(2, 2, WorldProviderEuropa::class.java)
        Registers.registerPlanet(3, 3, WorldProviderMoon::class.java)
        Registers.registerPlanet(4, 4, WorldProviderVenus::class.java)
        Registers.registerPlanet(5, 5, WorldProviderMars::class.java)
        Registers.registerPlanet(1000, 1000, WorldProviderEarthOrbit::class.java)

    }
}