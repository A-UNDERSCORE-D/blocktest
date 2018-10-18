package ad.blocktest.blocks

import ad.blocktest.BlockTest
import ad.blocktest.util.random
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.Explosion
import net.minecraft.world.World
import java.util.*


fun createExplosionAtLocation(worldIn: World, x: Double, y: Double, z: Double, strength: Float = 6F) {
    worldIn.createExplosion(null, x, y, z, strength, true)
}


object BlockBang : BlockBase(Material.ROCK) {
    init {
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
        unlocalizedName = BlockTest.modId + ".bang_block"
        setRegistryName("bang_block")
    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (worldIn.isRemote) {
            return true
        }

        this.doExplosion(worldIn, pos)
        return true
    }

    private fun doExplosion(worldIn: World, pos: BlockPos) {
        val x = pos.x.toDouble()
        val y = pos.y.toDouble()
        val z = pos.z.toDouble()

        for (ΔX in -3..3 step 3) {
            for (ΔZ in -3..3 step 3) {
                createExplosionAtLocation(worldIn, x + ΔX + ((0..50).random() / 10), y - 3 + ((0..50).random() / 10), z + ΔZ + ((0..50).random() / 10))
                createExplosionAtLocation(worldIn, x + ΔX + ((0..50).random() / 10), y + ((0..50).random() / 10), z + ΔZ + ((0..50).random() / 10))
                createExplosionAtLocation(worldIn, x + ΔX + ((0..50).random() / 10), y + 3 + ((0..50).random() / 10), z + ΔZ + ((0..50).random() / 10))
            }
        }
        worldIn.setBlockToAir(pos)
    }

    override fun onBlockExploded(world: World, pos: BlockPos, explosion: Explosion) {
        world.scheduleUpdate(pos, this, (2..10).random())
    }

    override fun updateTick(world: World, pos: BlockPos, state: IBlockState, rand: Random) {
        super.updateTick(world, pos, state, rand)
        doExplosion(world, pos)
    }
}
