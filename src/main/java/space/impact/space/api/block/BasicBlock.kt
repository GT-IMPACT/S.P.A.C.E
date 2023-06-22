package space.impact.space.api.block

import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import net.minecraft.world.World
import space.impact.space.MODID
import space.impact.space.addons.solar_system.SolarSystem

open class BasicBlock(
    protected val system: String,
    protected val blockName: String,
    protected val textureFolder: String,
    protected val blocks: List<String>,
) : Block(Material.rock) {

    @SideOnly(Side.CLIENT)
    protected var icons: Array<IIcon?> = arrayOfNulls(blocks.size)

    init {
        unlocalizedName = blockName
        setHardness(2.0f)
        setHarvestLevel("pickaxe", 2)
        GameRegistry.registerBlock(this, BasicItemBlock::class.java, blockName)
    }

    @SideOnly(Side.CLIENT)
    override fun registerIcons(reg: IIconRegister) {
        blocks.forEachIndexed { index, s ->
            icons[index] = reg.registerIcon("$MODID:$system/$textureFolder/${s.lowercase()}")
        }
    }

    @SideOnly(Side.CLIENT)
    override fun getIcon(side: Int, meta: Int): IIcon? {
        return icons[meta]
    }

    @SideOnly(Side.CLIENT)
    override fun getSubBlocks(block: Item, creativeTabs: CreativeTabs?, list: MutableList<in ItemStack>) {
        for (i in blocks.indices) {
            list.add(ItemStack(block, 1, i))
        }
    }

    override fun getDamageValue(world: World, x: Int, y: Int, z: Int): Int {
        return world.getBlockMetadata(x, y, z)
    }

    override fun onBlockPlacedBy(world: World, x: Int, y: Int, z: Int, entity: EntityLivingBase?, stack: ItemStack) {
        world.setBlockMetadataWithNotify(x, y, z, stack.metadata, 3)
    }

    class BasicItemBlock(block: Block) : ItemBlock(block) {
        init {
            maxDurability = 0
            hasSubtypes = true
        }

        override fun getUnlocalizedName(stack: ItemStack): String {
            return "$unlocalizedName.${stack.metadata}"
        }
    }
}