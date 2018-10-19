package ad.blocktest

import ad.blocktest.items.ItemBlockBase
import ad.blocktest.items.TestItem
import ad.blocktest.tiles.TileEntityShape
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.registries.IForgeRegistry
import org.apache.logging.log4j.Logger


fun itemBlockWithRegistryName(block: Block): Item = ItemBlockBase(block).setRegistryName(block.registryName)!!

open class CommonProxy {
    lateinit var logger: Logger
    open fun init(event: FMLInitializationEvent) {
        logger.info("Common proxy init")
    }

    open fun preInit(event: FMLPreInitializationEvent) {
        logger.info("Common Proxy PreInit")
    }

    open fun initItems(registry: IForgeRegistry<Item>) {

        //Register itemBlocks
        for (block in ModBlocks.BLOCKS) {
            registry.register(itemBlockWithRegistryName(block))
        }
        BlockTest.logger.info("Registered ItemBlocks")
        for (item in ModItems.ITEMS) {
            registry.register(item)
        }
        BlockTest.logger.info("Registered Items")
    }

    open fun initBlocks(registry: IForgeRegistry<Block>) {
        for (block in ModBlocks.BLOCKS) {
            registry.register(block)
        }
        BlockTest.logger.info("Registered Blocks")

        GameRegistry.registerTileEntity(
                TileEntityShape::class.java,
                ResourceLocation(BlockTest.modId, "shape_block")
        )
        BlockTest.logger.info("Registered tileEntities")
    }

    open fun registerModels() {}
}