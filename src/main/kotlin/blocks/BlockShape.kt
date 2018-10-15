package ad.blocktest.blocks

import ad.blocktest.BlockTest
import ad.blocktest.tiles.TileEntityShape
import net.minecraft.block.Block
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemBlock
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.World

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
            "circle" to ::nonExistent
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
        playerIn.sendStatusMessage(TextComponentString(facing.toString()), false)

        val te = getTE(worldIn, pos)

        val heldItem = playerIn.getHeldItem(hand)
        if (heldItem.isEmpty) {
            te.increment()
            val name = states.entries.elementAt(te.shapeIdx).key
            val msg = TextComponentTranslation("message.blocktest.shape_block.change_shape", name)
            msg.style.color = TextFormatting.AQUA
            playerIn.sendStatusMessage(msg, true)
            return true
        } else if (heldItem.item is ItemBlock) {
            val toPlace = getBlockFromItem(heldItem.item)
            val name = states.entries.elementAt(te.shapeIdx).key
            @Suppress("DEPRECATION")
            val toPlaceState = toPlace.getStateFromMeta(heldItem.metadata)
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
    private fun placeArea(startX: Int, startZ: Int, endX: Int, endZ: Int, world: World, toPlace: IBlockState) {

    }

    @Suppress("UNUSED_PARAMETER")
    private fun nonExistent(size: Int, worldIn: World, pos: BlockPos, facing: EnumFacing, toPlace: IBlockState) {
    }
}
