package ad.blocktest.blocks.shapes

import ad.blocktest.BlockTest.modId
import net.minecraft.block.state.IBlockState
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

interface IBlockShapeShape {
    val name: String
    val unlocalizedName: String
        get() = "shape.$modId.$name"

    fun place(size: Int, worldIn: World, pos: BlockPos, facing: EnumFacing, toPlace: IBlockState)
}
