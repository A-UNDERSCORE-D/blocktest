package ad.blocktest.blocks

import ad.blocktest.BlockTest
import ad.blocktest.blocks.shapes.*
import ad.blocktest.tiles.TileEntityShape
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
    private val shapes: MutableList<IBlockShapeShape> = mutableListOf()

    init {
        setRegistryName("shape_block")
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
        unlocalizedName = BlockTest.modId + ".shape_block"

        registerShape(ShapeConcentricRectangles)
        registerShape(ShapeCubeAbove)
        registerShape(ShapeCubePointsAbove)
        registerShape(ShapeCubeWireframeAbove)
        registerShape(ShapeDiagonal)
        registerShape(ShapePerimeter)
        registerShape(ShapePlane)
    }

//    private val shapes: Map<String, (Int, World, BlockPos, EnumFacing, IBlockState) -> Unit> = mapOf(
//            "wall" to ::nonExistent,
//            "plane" to ::placePlane,
//            "checkerboard" to ::nonExistent,
//            "spiral" to ::nonExistent,
//            "circle" to ::nonExistent,
//            "perimeter" to ::placePerimeter,
//            "conc perimeter" to ::placeConcentricRectangles,
//            "diagonal" to ::placeDiagonal,
//            "cube above" to ::placeCubeAbove,
//            "cube points above" to ::placeCubePointsAbove,
//            "cube wireframe above" to ::placeCubeWireframeAbove
//    )

    override fun createNewTileEntity(worldIn: World, meta: Int): TileEntity? {
        val te = TileEntityShape()
        te.setmaxIdx(shapes.size - 1)
        return te
    }

    fun registerShape(shape: IBlockShapeShape) {
        BlockTest.logger.info("Registered shape $shape")
        shapes.add(shape)
    }

    private fun getTE(worldIn: World, pos: BlockPos): TileEntityShape {
        return worldIn.getTileEntity(pos)!! as TileEntityShape
    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (worldIn.isRemote) {
            return true
        }
        val te = getTE(worldIn, pos)
        if (te.maxIdx != shapes.size - 1) {
            te.setmaxIdx(shapes.size - 1)
        }

        val heldItem = playerIn.getHeldItem(hand)
        if (heldItem.isEmpty) {

            if (playerIn.isSneaking) {
                te.decrement()
            } else {
                te.increment()
            }
            val shape = shapes[te.shapeIdx]
            val localizedName = TextComponentTranslation(shape.unlocalizedName + ".name")

            val msg = TextComponentTranslation("message.blocktest.shape_block.change_shape", localizedName.formattedText)
            msg.style.color = TextFormatting.RED
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
            val shape = shapes[te.shapeIdx]
            shape.place(
                    heldItem.count,
                    worldIn,
                    pos,
                    facing,
                    toPlaceState
            )
        }

        return true
    }

}
