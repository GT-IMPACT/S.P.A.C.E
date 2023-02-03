package space.impact.space.addons.solar_system

import space.impact.space.api.block.BasicBlock
import space.impact.space.api.block.GeyserBlock
import space.impact.space.api.core.SystemRegister

object SolarSystem : SystemRegister {

    override val name: String = "solar_system"


    val EUROPA_BLOCKS = BasicBlock(name, "europa_rock", "europa", listOf("grunt", "stone", "brown_ice"))
    val EUROPA_GEYSERS = GeyserBlock(name, "europa_geyser", "europa", listOf("over_water", "under_water"))



    override fun register() {

    }
}