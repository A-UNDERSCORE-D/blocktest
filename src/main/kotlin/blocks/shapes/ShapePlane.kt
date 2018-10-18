package ad.blocktest.blocks.shapes

import ad.blocktest.util.CubeArea
import ad.blocktest.util.placeBlocksInArea
import net.minecraft.block.state.IBlockState
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object ShapePlane : IBlockShapeShape {
    override val name = "plane"

    override fun place(size: Int, worldIn: World, pos: BlockPos, facing: EnumFacing, toPlace: IBlockState) {
        placeBlocksInArea(worldIn, CubeArea(pos, size).toPlaneAt(pos.y), toPlace, pos)
    }
}
