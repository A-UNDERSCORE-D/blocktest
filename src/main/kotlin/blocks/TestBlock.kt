package ad.blocktest.blocks

import ad.blocktest.BlockTest
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.Random

class TestBlock: Block(Material.ROCK) {
    init {
        unlocalizedName = BlockTest.modId + ".test_block"
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
        setRegistryName("test_block")
        setHardness(.1F)
    }

    override fun getItemDropped(state: IBlockState, rand: Random, fortune: Int): Item {
        return Item.getItemFromBlock(this)
    }

    override fun onBlockClicked(worldIn: World, pos: BlockPos, playerIn: EntityPlayer) {
        super.onBlockClicked(worldIn, pos, playerIn)
    }

}