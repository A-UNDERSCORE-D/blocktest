package ad.blocktest.util

import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

fun placeBlocksInArea(world: World, startPos: BlockPos, endPos: BlockPos, toPlace: IBlockState) {
    return placeBlocksInArea(world, startPos, endPos, toPlace, emptyArray())
}

fun placeBlocksInArea(world: World, startPos: BlockPos, endPos: BlockPos, toPlace: IBlockState, toSkip: BlockPos) {
    return placeBlocksInArea(world, startPos, endPos, toPlace, arrayOf(toSkip))
}

fun placeBlocksInArea(world: World, startPos: BlockPos, endPos: BlockPos, toPlace: IBlockState, toSkip: Array<BlockPos>) {
    return placeBlocksInArea(world, CubeArea(startPos, endPos), toPlace, toSkip)
}

fun placeBlocksInArea(world: World, cubeArea: CubeArea, toPlace: IBlockState, toSkip: BlockPos) {
    return placeBlocksInArea(world, cubeArea, toPlace, arrayOf(toSkip))
}

fun placeBlocksInArea(world: World, cubeArea: CubeArea, toPlace: IBlockState) {
    return placeBlocksInArea(world, cubeArea, toPlace, emptyArray())
}

fun placeBlocksInArea(world: World, cubeArea: CubeArea, toPlace: IBlockState, toSkip: Array<BlockPos>) {
    for (block in cubeArea.filter { it !in toSkip }) {
        world.setBlockState(block, toPlace)
    }
}
