package ad.blocktest

import ad.blocktest.blocks.BlockBang
import ad.blocktest.blocks.BlockRandPlace
import net.minecraft.block.Block
import net.minecraftforge.fml.common.registry.GameRegistry

object ModBlocks {
    @GameRegistry.ObjectHolder("blocktest:test_block")
    val blockTest: Block = ad.blocktest.blocks.TestBlock()

    @GameRegistry.ObjectHolder("blocktest:rand_block")
    val blockRandPlace: BlockRandPlace = ad.blocktest.blocks.BlockRandPlace

    @GameRegistry.ObjectHolder("blocktest:bang_block")
    val blockBang: ad.blocktest.blocks.BlockBang = ad.blocktest.blocks.BlockBang

    @GameRegistry.ObjectHolder("blocktest:shape_block")
    val blockShape: ad.blocktest.blocks.BlockShape = ad.blocktest.blocks.BlockShape

    @GameRegistry.ObjectHolder("blocktest:rain_block")
    val blockStopRain: ad.blocktest.blocks.BlockStopRain = ad.blocktest.blocks.BlockStopRain()
}