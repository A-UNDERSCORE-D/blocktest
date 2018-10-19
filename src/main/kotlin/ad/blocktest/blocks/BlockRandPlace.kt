package ad.blocktest.blocks

import ad.blocktest.BlockTest
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.*

object BlockRandPlace : BlockBase(Material.ROCK) {
    init {
        unlocalizedName = BlockTest.modId + ".rand_block"
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
        setRegistryName("rand_block")
        setHardness(.1F)
        tickRandomly = true
    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (worldIn.isRemote) {
            return true
        }

        placeRandomBlocks(worldIn, pos, this.defaultState)
        return true
    }

    override fun updateTick(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random) {
        BlockTest.logger.info("RandPlace block at $pos on world $worldIn got a random tick!")
        placeRandomBlocks(worldIn, pos, this.defaultState)
    }

    private fun placeRandomBlocks(worldIn: World, pos: BlockPos, iBlockState: IBlockState) {
        val y = pos.y + 1
        var x = 0
        var z = 0
        when (Random().nextInt(4)) {
            0 -> {
                x = pos.x + Random().nextInt(20)
                z = pos.z + Random().nextInt(20)
            }
            1 -> {
                x = pos.x - Random().nextInt(20)
                z = pos.z - Random().nextInt(20)
            }
            2 -> {
                x = pos.x - Random().nextInt(20)
                z = pos.z + Random().nextInt(20)
            }
            3 -> {
                x = pos.x + Random().nextInt(20)
                z = pos.z - Random().nextInt(20)
            }
        }

        worldIn.setBlockState(BlockPos(x, y, z), iBlockState)
    }
}