package ad.blocktest.blocks.shapes

import ad.blocktest.util.CubeArea
import net.minecraft.block.state.IBlockState
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object ShapeCubePointsAbove: IBlockShapeShape {
    override val name: String = "cube_points_above"

    override fun place(size: Int, worldIn: World, pos: BlockPos, facing: EnumFacing, toPlace: IBlockState) {
        val ca = CubeArea(pos.add(0, size + 5, 0), size)
        for (point in ca.getPoints()) {
            worldIn.setBlockState(point, toPlace)
        }
    }
}
