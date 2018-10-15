package ad.blocktest.blocks

import ad.blocktest.BlockTest
import ad.blocktest.tiles.TileEntityShape
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

object BlockShape : Block(Material.ROCK), ITileEntityProvider {
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
            "box" to ::placeBox
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

//        playerIn.sendStatusMessage(TextComponentString(facing.toString()), false)


        val heldItem = playerIn.getHeldItem(hand)
        if (heldItem.isEmpty) {

            te.increment()
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

        for (x in startX..endX) {
            for (z in startZ..endZ) {
                val toPlacePos = BlockPos(x, pos.y, z)
                if (toPlacePos == pos) {
                    continue
                }
                worldIn.setBlockState(toPlacePos, toPlace)
            }
        }
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

    private fun placeBox(size: Int, worldIn: World, pos: BlockPos, facing: EnumFacing, toPlace: IBlockState) {
        val startX = pos.x - size
        val startZ = pos.z - size

        val endX = pos.x + size
        val endZ = pos.z + size
        // Z = +s <-> -n
        // X = +e <-> -w
        val pointNW = BlockPos(startX, pos.y, startZ)
        val pointSE = BlockPos(endX, pos.y, endZ)
        val pointNE = BlockPos(startX, pos.y, endZ)
        val pointSW = BlockPos(endX, pos.y, startZ)
        placeBlocksInArea(worldIn, pointNW, pointSW, toPlace)
        placeBlocksInArea(worldIn, pointSW, pointSE, toPlace)
        placeBlocksInArea(worldIn, pointSE, pointNE, toPlace)
        placeBlocksInArea(worldIn, pointNE, pointNW, toPlace)
    }

    private fun placeBlocksInArea(world: World, startPos: BlockPos, endPos: BlockPos, toPlace: IBlockState) {
        var counter: Int = 0
        for (y in pozToNegRange(startPos.y, endPos.y)) {
            for (x in pozToNegRange(startPos.x, endPos.x)) {
                for (z in pozToNegRange(startPos.z, endPos.z)) {
                    world.setBlockState(BlockPos(x, y, z), toPlace)
                    counter++
                }
            }
        }
        println("Placed $counter blocks")
    }

    @Suppress("UNUSED_PARAMETER")
    private fun nonExistent(size: Int, worldIn: World, pos: BlockPos, facing: EnumFacing, toPlace: IBlockState) {
    }
}
