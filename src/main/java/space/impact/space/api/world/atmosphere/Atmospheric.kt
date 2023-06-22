package space.impact.space.api.world.atmosphere

import space.impact.space.api.elements.PeriodicTable
import space.impact.space.api.elements.PeriodicTable.*

enum class Atmospheric(private vararg val elements: PeriodicTable) : AtmosphericGas {
    OXYGEN(O),
    CO2(C, O);

    override fun getElements(): List<PeriodicTable> {
        return elements.toList()
    }
}