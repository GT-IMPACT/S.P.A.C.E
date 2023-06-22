package space.impact.space.config

import net.minecraftforge.common.config.Configuration
import java.io.File

object Config {

    public const val OVERFLOW_CYCLE_MAX = 9_999_999

    private const val CATEGORY_WORLD_ENV = "WORLD"

    private const val CATEGORY_CLIENT = "CLIENT"


    private inline fun onPostCreate(configFile: File?, crossinline action: (Configuration) -> Unit) {
        Configuration(configFile).let { config ->
            config.load()
            action(config)
            if (config.hasChanged()) {
                config.save()
            }
        }
    }

    fun createConfig(configFile: File?) {
        val config = File(File(configFile, "IMPACT"), "SPACE.cfg")
        onPostCreate(config) { cfg ->

            //WORLD
            isEnabledForceRespawn = cfg[CATEGORY_WORLD_ENV, "isEnabledForceRespawn", false].boolean

            //CLIENT
            isEnabledSupportCircleTexturePlanet = cfg[CATEGORY_CLIENT, "isEnabledSupportCircleTexturePlanet", false].boolean

            //OTHER
        }
    }

    var isEnabledForceRespawn: Boolean = false
    var isEnabledSupportCircleTexturePlanet: Boolean = false
}