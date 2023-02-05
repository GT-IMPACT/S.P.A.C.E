package space.impact.space.api.world.atmosphere

interface PlanetFog {
    fun getFogDensity(x: Int, y: Int, z: Int): Float
    fun getFogColor(x: Int, y: Int, z: Int): Int
}