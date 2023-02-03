package space.impact.space.api.world.gen.world

import space.impact.space.api.world.atmosphere.AtmosphericGas
import space.impact.space.api.world.bodies.CelestialBody

interface SpaceProvider {

    /**
     * Default 0.040 - 0.065
     */
    fun getGravitationMultiply(): Float

    /**
     * 10 - more quiet, 0.1 - 10 times louder
     */
    fun getSoundVolReductionAmount(): Float

    /**
     * if players can breathe here
     */
    fun hasBreathableAtmosphere(): Boolean

    /**
     * Does the atmosphere of this measurement contain the specified gas or not
     */
    fun isGasPresent(gas: AtmosphericGas): Boolean

    /**
     * This value will affect the player's heat level, causing damage to him if he reaches too high or too low a level.
     *
     * Positive integer for hot celestial bodies, negative for cold ones. Zero for neutral
     */
    fun getThermalLevelModifier(): Float

    /**
     * Overworld has a value of 1.0F, the Moon has a value of 0.0F.
     *
     * The magnitude of the flag movement. Relative to the earth value of 1.0F
     */
    fun getWindLevel(): Float

    /**
     * Factor by which the sun is to be drawn smaller (<1.0) or larger (>1.0) than the sun on the Overworld
     */
    fun getSolarSize(): Float

    /**
     * The celestial body object for this dimension
     */
    fun getCelestialBody(): CelestialBody

}