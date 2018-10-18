package ad.blocktest

import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

class ClientProxy : CommonProxy() {
    override fun preInit(event: FMLPreInitializationEvent) {
        super.preInit(event)
        BlockTest.logger.info("Client Proxy preinit")
    }

    override fun init(event: FMLInitializationEvent) {
        super.init(event)
        logger.info("ClientProxy init")
    }

    override fun registerModels() {
        super.registerModels()
        BlockTest.logger.info("Started registering models")
        for (block in ModBlocks.BLOCKS) {
            block.registerItemModel()
        }
        BlockTest.logger.info("Registered block item models")

        for (item in ModItems.ITEMS) {
            ModelLoader.setCustomModelResourceLocation(item, 0, ModelResourceLocation(item.registryName!!, "inventory"))
        }
        BlockTest.logger.info("Registered Item models")
        BlockTest.logger.info("Finished registering models")
    }
}
