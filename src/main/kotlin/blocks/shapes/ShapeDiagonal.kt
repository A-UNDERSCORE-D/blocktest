package ad.blocktest.blocks.shapes

import ad.blocktest.util.CubeArea
import net.minecraft.block.state.IBlockState
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object ShapeDiagonal: IBlockShapeShape {
    override val name: String = "diagonal"

    override fun place(size: Int, worldIn: World, pos: BlockPos, facing: EnumFacing, toPlace: IBlockState) {
        for (i in size downTo 1) {
            val ca = CubeArea(pos, i).toPlaneAt(pos.y)
            for (c in ca.getPoints()) {
                worldIn.setBlockState(c, toPlace)
            }
        }
    }
}
