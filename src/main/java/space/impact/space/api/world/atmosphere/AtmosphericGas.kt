package space.impact.space.api.world.atmosphere

import space.impact.space.api.elements.PeriodicTable

interface AtmosphericGas {
    fun getElements(): List<PeriodicTable>
}