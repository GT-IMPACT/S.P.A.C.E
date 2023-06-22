package space.impact.space.api.block

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import net.minecraft.world.World
import space.impact.space.MODID
import space.impact.space.SPACE
import space.impact.space.api.world.world_math.Vector3
import space.impact.space.client.effects.EffectData
import java.util.*

class GeyserBlock(system: String, blockName: String, textureFolder: String, blocks: List<String>) : BasicBlock(system, blockName, textureFolder, blocks) {

    init {
        tickRandomly = false
        setHardness(2.0f)
        setResistance(3.0f)
    }

    @SideOnly(Side.CLIENT)
    private var iconsTop: Array<IIcon?> = arrayOfNulls(blocks.size)

    override fun getItemDropped(meta: Int, random: Random?, par3: Int): Item? {
        return null
    }

    @SideOnly(Side.CLIENT)
    override fun registerIcons(reg: IIconRegister) {
        blocks.forEachIndexed { index, s ->
            val path = "$MODID:$system/$textureFolder/${s.lowercase()}"
            iconsTop[index] = reg.registerIcon("${path}_top")
            icons[index] = reg.registerIcon(if (s == "under_water") "ice_packed" else path)
        }
    }

    override fun getIcon(side: Int, meta: Int): IIcon? {
        if (side > 1) return icons[meta]
        return iconsTop[meta]
    }

    override fun onEntityWalking(world: World, x: Int, y: Int, z: Int, entity: Entity) {
        if (entity is EntityLivingBase) {
            ++entity.motionY
        }
    }

    @SideOnly(Side.CLIENT)
    override fun randomDisplayTick(world: World, x: Int, y: Int, z: Int, rand: Random) {

        val effectData1 = EffectData.IceWaterEffect(50, 32, false, Vector3(1.0, 1.0, 1.0), 1.0)
        val effectData2 = EffectData.IceWaterEffect(50, 5, false, Vector3(1.0, 1.0, 1.0), 1.0)
        val effectData3 = EffectData.IceWaterEffect(160, 17, true, Vector3(1.0, 1.0, 1.0), 1.0)
        
        if (!world.isAirBlock(x, y + 1, z) && world.getBlock(x, y + 1, z) != Blocks.snow_layer) {
            if (world.getBlock(x, y + 1, z).material == Material.water) {
                SPACE.proxy.spawnParticle(
                    Vector3(x.toDouble() + rand.nextDouble(), y.toDouble() + 1.0 + rand.nextDouble(), z.toDouble() + rand.nextDouble()),
                    Vector3(0.0, 0.3, 0.0), effectData1
                )
                SPACE.proxy.spawnParticle(
                    Vector3(x.toDouble() + rand.nextDouble(), y.toDouble() + 1.0 + rand.nextDouble(), z.toDouble() + rand.nextDouble()),
                    Vector3(0.0, 0.3, 0.0), effectData1
                )
                SPACE.proxy.spawnParticle(
                    Vector3(x.toDouble() + rand.nextDouble(), y.toDouble() + 1.0 + rand.nextDouble(), z.toDouble() + rand.nextDouble()),
                    Vector3(0.0, 0.3, 0.0), effectData1
                )
            }
        } else {
            SPACE.proxy.spawnParticle(
                Vector3(x.toDouble() + rand.nextDouble(), y.toDouble() + 1.0 + rand.nextDouble(), z.toDouble() + rand.nextDouble()),
                Vector3(0.0, 0.3, 0.0), effectData2
            )
            SPACE.proxy.spawnParticle(
                Vector3(x.toDouble() + rand.nextDouble(), y.toDouble() + 1.0 + rand.nextDouble(), z.toDouble() + rand.nextDouble()),
                Vector3(0.0, 0.3, 0.0), effectData2
            )
            SPACE.proxy.spawnParticle(
                Vector3(x.toDouble() + rand.nextDouble(), y.toDouble() + 1.0 + rand.nextDouble(), z.toDouble() + rand.nextDouble()),
                Vector3(0.0, 0.3, 0.0), effectData2
            )
            SPACE.proxy.spawnParticle(
                Vector3(x.toDouble() + rand.nextDouble(), y.toDouble() + 1.0 + rand.nextDouble(), z.toDouble() + rand.nextDouble()),
                Vector3(0.03, 0.3, 0.03), effectData3
            )
            SPACE.proxy.spawnParticle(
                Vector3(x.toDouble() + rand.nextDouble(), y.toDouble() + 1.0 + rand.nextDouble(), z.toDouble() + rand.nextDouble()),
                Vector3(0.0, 0.3, 0.0), effectData3
            )
            SPACE.proxy.spawnParticle(
                Vector3(x.toDouble() + rand.nextDouble(), y.toDouble() + 1.0 + rand.nextDouble(), z.toDouble() + rand.nextDouble()),
                Vector3(-0.03, 0.3, -0.03), effectData3
            )
            SPACE.proxy.spawnParticle(
                Vector3(x.toDouble() + rand.nextDouble(), y.toDouble() + 1.0 + rand.nextDouble(), z.toDouble() + rand.nextDouble()),
                Vector3(0.03, 0.3, -0.03), effectData3
            )
            SPACE.proxy.spawnParticle(
                Vector3(x.toDouble() + rand.nextDouble(), y.toDouble() + 1.0 + rand.nextDouble(), z.toDouble() + rand.nextDouble()),
                Vector3(-0.03, 0.3, 0.03), effectData3
            )
            SPACE.proxy.spawnParticle(
                Vector3(x.toDouble() + rand.nextDouble(), y.toDouble() + 1.0 + rand.nextDouble(), z.toDouble() + rand.nextDouble()),
                Vector3(0.03, 0.3, 0.03), effectData3
            )
            SPACE.proxy.spawnParticle(
                Vector3(x.toDouble() + rand.nextDouble(), y.toDouble() + 1.0 + rand.nextDouble(), z.toDouble() + rand.nextDouble()),
                Vector3(0.0, 0.3, 0.0), effectData3
            )
            SPACE.proxy.spawnParticle(
                Vector3(x.toDouble() + rand.nextDouble(), y.toDouble() + 1.0 + rand.nextDouble(), z.toDouble() + rand.nextDouble()),
                Vector3(-0.03, 0.3, -0.03), effectData3
            )
        }
    }
}