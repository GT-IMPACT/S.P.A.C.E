package space.impact.space.config

import net.minecraftforge.common.config.Configuration
import java.io.File

object Config {

    private const val CATEGORY_WORLD_ENV = "WORLD"



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

            //OTHER

        }
    }

    var isEnabledForceRespawn: Boolean = false
}