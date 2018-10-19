package ad.blocktest.blocks

import ad.blocktest.BlockTest
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.*

class BlockStopRain : BlockBase(Material.ROCK) {

    companion object {
        val redstoneInverted: PropertyBool = PropertyBool.create("active")
    }

    init {
        setRegistryName("rain_block")
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
        unlocalizedName = BlockTest.modId + ".rain_block"
        soundType = SoundType.CLOTH
        defaultState = this.blockState.baseState.withProperty(redstoneInverted, false)
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return if (state.getValue(redstoneInverted)) 1 else 0
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        return defaultState.withProperty(redstoneInverted, meta == 1)
    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer,
                                  hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (worldIn.isRemote) {
            return true
        }
        val currentState = worldIn.getBlockState(pos)
        val autoClearRainActive = currentState.getValue(redstoneInverted)
        if (playerIn.isSneaking) {
            worldIn.setBlockState(pos, blockState.baseState.withProperty(redstoneInverted, !autoClearRainActive))
        } else {
            disableRain(worldIn)
        }
        return true
    }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, redstoneInverted)
    }

    override fun getStateForPlacement(world: World, pos: BlockPos, facing: EnumFacing, hitX: Float, hitY: Float,
                                      hitZ: Float, meta: Int, placer: EntityLivingBase, hand: EnumHand): IBlockState {
        world.scheduleUpdate(pos, this, 10) // Thanks DE for this cool idea
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand)
    }

    override fun updateTick(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random) {
        super.updateTick(worldIn, pos, state, rand)
        worldIn.scheduleUpdate(pos, this, 20)
        val isPowered = worldIn.isBlockPowered(pos)
        val redstoneIsInverted = worldIn.getBlockState(pos).getValue(redstoneInverted)
        val isRainOrThunder = worldIn.isRaining || worldIn.isThundering

        if ((redstoneIsInverted && !isPowered) && isRainOrThunder) {
            disableRain(worldIn)
            BlockTest.logger.info("Rain stopped by rain block ($state) at $pos")
        } else if ((!redstoneIsInverted && isPowered) && isRainOrThunder) {
            disableRain(worldIn)
            BlockTest.logger.info("Rain stopped by rain block ($state) at $pos")
        }
    }

    private fun disableRain(worldIn: World) {
        worldIn.worldInfo.isRaining = false
        worldIn.worldInfo.isThundering = false
    }
}