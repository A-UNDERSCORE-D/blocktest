package ad.blocktest

import net.minecraftforge.fml.common.registry.GameRegistry

object ModBlocks {
    @GameRegistry.ObjectHolder("blocktest:test_block")
    val blockTest = ad.blocktest.blocks.TestBlock()

    @GameRegistry.ObjectHolder("blocktest:rand_block")
    val blockRandPlace = ad.blocktest.blocks.BlockRandPlace

    @GameRegistry.ObjectHolder("blocktest:bang_block")
    val blockBang = ad.blocktest.blocks.BlockBang

    @GameRegistry.ObjectHolder("blocktest:shape_block")
    val blockShape = ad.blocktest.blocks.BlockShape

    @GameRegistry.ObjectHolder("blocktest:rain_block")
    val blockStopRain = ad.blocktest.blocks.BlockStopRain()

    val BLOCKS = arrayOf(
            blockTest,
            blockRandPlace,
            blockBang,
            blockShape,
            blockStopRain
    )
}