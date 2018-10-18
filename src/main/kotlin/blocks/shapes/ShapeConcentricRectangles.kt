package ad.blocktest.blocks.shapes

import net.minecraft.block.state.IBlockState
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object ShapeConcentricRectangles: IBlockShapeShape {
    override val name: String = "concentric_rectangles"

    override fun place(size: Int, worldIn: World, pos: BlockPos, facing: EnumFacing, toPlace: IBlockState) {
        for (i in size downTo 1 step 2) {
            ShapePerimeter.place(i, worldIn, pos, facing, toPlace)
        }
    }
}
