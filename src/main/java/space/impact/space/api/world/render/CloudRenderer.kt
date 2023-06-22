package space.impact.space.api.world.render

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.WorldClient
import net.minecraftforge.client.IRenderHandler

class CloudRenderer : IRenderHandler() {
    @SideOnly(Side.CLIENT)
    override fun render(partialTicks: Float, world: WorldClient?, mc: Minecraft?) {}
}