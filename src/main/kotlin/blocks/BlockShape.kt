package ad.blocktest.blocks

import ad.blocktest.BlockTest
import ad.blocktest.tiles.TileEntityShape
import ad.blocktest.util.CubeArea
import net.minecraft.block.Block
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.ItemBlock
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.World

fun pozToNegRange(a: Int, b: Int) = if (a < b) (a..b) else (b..a)

object BlockShape : BlockBase(Material.ROCK), ITileEntityProvider {
    init {
        setRegistryName("shape_block")
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
        unlocalizedName = BlockTest.modId + ".shape_block"
    }

    private val states: Map<String, (Int, World, BlockPos, EnumFacing, IBlockState) -> Unit> = mapOf(
            "wall" to ::nonExistent,
            "plane" to ::placePlane,
            "checkerboard" to ::nonExistent,
            "spiral" to ::nonExistent,
            "circle" to ::nonExistent,
            "box" to ::placePerimeter,
            "cube above" to ::placeCubeAbove,
            "cube points above" to ::placeCubePointsAbove,
            "cube wireframe above" to ::placeCubeWireframeAbove
    )

    override fun createNewTileEntity(worldIn: World, meta: Int): TileEntity? {
        val te = TileEntityShape()
        te.setmaxIdx(states.size - 1)
        return te
    }

    private fun getTE(worldIn: World, pos: BlockPos): TileEntityShape {
        return worldIn.getTileEntity(pos)!! as TileEntityShape
    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (worldIn.isRemote) {
            return true
        }

        val te = getTE(worldIn, pos)
        if (te.maxIdx < states.size - 1) {
            te.setmaxIdx(states.size - 1)
        }

        val heldItem = playerIn.getHeldItem(hand)
        if (heldItem.isEmpty) {

            if (playerIn.isSneaking) {
                te.decrement()
            } else {
                te.increment()
            }

            val name = states.entries.elementAt(te.shapeIdx).key
            val msg = TextComponentTranslation("message.blocktest.shape_block.change_shape", name)
            msg.style.color = TextFormatting.AQUA
            playerIn.sendStatusMessage(msg, true)

        } else if (heldItem.item is ItemBlock || heldItem.item == Items.STICK) {

            lateinit var toPlaceState: IBlockState

            if (heldItem.item is ItemBlock) {
                val toPlace = getBlockFromItem(heldItem.item)
                @Suppress("DEPRECATION")
                toPlaceState = toPlace.getStateFromMeta(heldItem.metadata)
            } else {
                toPlaceState = Blocks.AIR.defaultState
            }
            val name = states.entries.elementAt(te.shapeIdx).key
            val func = states[name]!!
            func(
                    heldItem.count,
                    worldIn,
                    pos,
                    facing,
                    toPlaceState
            )
        }

        return true
    }

    private fun placePlane(size: Int, worldIn: World, pos: BlockPos, facing: EnumFacing, toPlace: IBlockState) {
        val startX = pos.x - size
        val startZ = pos.z - size
        val endX = pos.x + size
        val endZ = pos.z + size
        placeBlocksInArea(worldIn, BlockPos(startX, pos.y, startZ), BlockPos(endX, pos.y, endZ), toPlace, arrayOf(pos))
    }

    private fun placeWall(size: Int, worldIn: World, pos: BlockPos, facing: EnumFacing, toPlace: IBlockState) {
        when (facing) {
            EnumFacing.EAST -> {

            }
            EnumFacing.DOWN -> placePlane(size, worldIn, pos, facing, toPlace)
            EnumFacing.UP -> placePlane(size, worldIn, pos, facing, toPlace)
            EnumFacing.NORTH -> TODO()
            EnumFacing.SOUTH -> TODO()
            EnumFacing.WEST -> TODO()
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun placePerimeter(size: Int, world: World, pos: BlockPos, facing: EnumFacing, toPlace: IBlockState) {
        // Z = +s <-> -n
        // X = +e <-> -w
        val cubeArea = CubeArea(pos, size).toPlaneAt(pos.y)
        placeBlocksInArea(world, cubeArea.pointNWB(), cubeArea.pointSWB(), toPlace)
        placeBlocksInArea(world, cubeArea.pointSWB(), cubeArea.pointSEB(), toPlace)
        placeBlocksInArea(world, cubeArea.pointSEB(), cubeArea.pointNEB(), toPlace)
        placeBlocksInArea(world, cubeArea.pointNEB(), cubeArea.pointNWB(), toPlace)
    }
    private fun placeCubeAbove(size: Int, worldIn: World, pos: BlockPos, facing: EnumFacing, toPlace: IBlockState) {
        placeBlocksInArea(worldIn, CubeArea(pos.add(0, size + 5, 0), size), toPlace)
    }

    private fun placeCubePointsAbove(size: Int, worldIn: World, pos: BlockPos, facing: EnumFacing, toPlace: IBlockState) {
        val ca = CubeArea(pos.add(0, size + 5, 0), size)
        for (point in ca.getPoints()) {
            worldIn.setBlockState(point, toPlace)
        }
    }

    private fun placeCubeWireframeAbove(size: Int, world: World, pos: BlockPos, facing: EnumFacing, toPlace: IBlockState) {
        val ca = CubeArea(pos.add(0, size + 5, 0), size)
        for (line in ca.getEdges()) {
            placeBlocksInArea(world, line, toPlace)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun nonExistent(size: Int, worldIn: World, pos: BlockPos, facing: EnumFacing, toPlace: IBlockState) {
    }

    // **** Place blocks funcs

    private fun placeBlocksInArea(world: World, startPos: BlockPos, endPos: BlockPos, toPlace: IBlockState) {
        return placeBlocksInArea(world, startPos, endPos, toPlace, emptyArray())
    }

    private fun placeBlocksInArea(world: World, startPos: BlockPos, endPos: BlockPos, toPlace: IBlockState, toSkip: Array<BlockPos>) {
        return placeBlocksInArea(world, CubeArea(startPos, endPos), toPlace, toSkip)
    }

    private fun placeBlocksInArea(world: World, cubeArea: CubeArea, toPlace: IBlockState) {
        return placeBlocksInArea(world, cubeArea, toPlace, emptyArray())
    }

    private fun placeBlocksInArea(world: World, cubeArea: CubeArea, toPlace: IBlockState, toSkip: Array<BlockPos>) {
        for (block in cubeArea) {
            if (block in toSkip) {
                continue
            }
            world.setBlockState(block, toPlace)
        }
    }
}
