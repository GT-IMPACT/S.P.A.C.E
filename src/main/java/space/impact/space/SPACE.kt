package space.impact.space

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.SidedProxy
import cpw.mods.fml.common.event.*
import space.impact.space.proxy.CommonProxy

@Mod(
    modid = MODID,
    version = VERSION,
    name = MODNAME,
    modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter",
    acceptedMinecraftVersions = "[1.7.10]",
    dependencies = "required-after:forgelin;"
)
object SPACE {

    @SidedProxy(clientSide = "$GROUPNAME.$MODID.proxy.ClientProxy", serverSide = "$GROUPNAME.$MODID.proxy.CommonProxy")
    lateinit var proxy: CommonProxy

    @Mod.InstanceFactory
    fun instance() = SPACE

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {

        proxy.preInit(event)
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        proxy.init(event)
    }

    @Mod.EventHandler
    fun postInit(event: FMLPostInitializationEvent) {
        proxy.postInit(event)
    }

    @Mod.EventHandler
    fun serverAboutToStart(event: FMLServerAboutToStartEvent) {
        proxy.serverAboutToStart(event)
    }

    @Mod.EventHandler
    fun serverStarting(event: FMLServerStartingEvent) {
        proxy.serverStarting(event)
    }

    @Mod.EventHandler
    fun serverStarted(event: FMLServerStartedEvent) {
        proxy.serverStarted(event)
    }

    @Mod.EventHandler
    fun serverStopping(event: FMLServerStoppingEvent) {
        proxy.serverStopping(event)
    }

    @Mod.EventHandler
    fun serverStopped(event: FMLServerStoppedEvent) {
        proxy.serverStopped(event)
    }
}