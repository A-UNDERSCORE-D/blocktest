package ad.blocktest.blocks.shapes

import ad.blocktest.util.CubeArea
import ad.blocktest.util.placeBlocksInArea
import net.minecraft.block.state.IBlockState
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object ShapePerimeter : IBlockShapeShape {
    override val name: String = "perimeter"

    override fun place(size: Int, worldIn: World, pos: BlockPos, facing: EnumFacing, toPlace: IBlockState) {
        val area = CubeArea(pos, size).toPlaneAt(pos.y)
        for (edge in area.getEdges()) {
            placeBlocksInArea(worldIn, edge, toPlace, pos)
        }
    }
}
