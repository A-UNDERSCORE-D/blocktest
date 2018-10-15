package ad.blocktest

import ad.blocktest.items.TestItem
import ad.blocktest.tiles.TileEntityShape
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.util.ResourceLocation
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.registry.GameRegistry
import org.apache.logging.log4j.Logger


fun itemBlockWithRegistryName(block: Block): Item = ItemBlock(block).setRegistryName(block.registryName)!!

open class CommonProxy {
    lateinit var logger: Logger
    open fun init(event: FMLInitializationEvent) {
        logger.info("Common proxy init")
    }

    open fun preInit(event: FMLPreInitializationEvent) {
        logger.info("Common Proxy PreInit")
    }

    @Mod.EventBusSubscriber
    object EventHandler {
        @JvmStatic
        @SubscribeEvent
        fun onItemRegister(event: RegistryEvent.Register<Item>) {
            event.registry.registerAll(
                    itemBlockWithRegistryName(ModBlocks.blockTest),
                    itemBlockWithRegistryName(ModBlocks.blockBang),
                    itemBlockWithRegistryName(ModBlocks.blockRandPlace),
                    itemBlockWithRegistryName(ModBlocks.blockShape),
                    itemBlockWithRegistryName(ModBlocks.blockStopRain),
                    TestItem
            )
        }

        @JvmStatic
        @SubscribeEvent
        fun onBlockRegister(event: RegistryEvent.Register<Block>) {
            event.registry.registerAll(
                    ModBlocks.blockTest,
                    ModBlocks.blockBang,
                    ModBlocks.blockRandPlace,
                    ModBlocks.blockShape,
                    ModBlocks.blockStopRain
            )

            GameRegistry.registerTileEntity(
                    TileEntityShape::class.java,
                    ResourceLocation(BlockTest.modId + "_shape_block")
            )

        }
    }
}