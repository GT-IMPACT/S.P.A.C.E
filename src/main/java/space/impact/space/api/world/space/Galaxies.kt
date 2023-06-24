package space.impact.space.api.world.space

import space.impact.space.api.world.bodies.Moon
import space.impact.space.api.world.bodies.Orbit
import space.impact.space.api.world.bodies.Planet

object Galaxies {

    val EARTH_MOON = Moon(2, "moon")
    val EARTH_ORBIT = Orbit(1000, "earth_orbit")

    val EUROPE_MOON = Moon(3, "europa")

    val VENUS_PLANET = Planet(4, "venus")
    val MARS_PLANET = Planet(5, "mars")

}