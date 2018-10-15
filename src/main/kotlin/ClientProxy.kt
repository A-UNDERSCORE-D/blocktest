package ad.blocktest

import net.minecraftforge.fml.common.event.FMLInitializationEvent

class ClientProxy: CommonProxy() {
    override fun init(event: FMLInitializationEvent) {
        super.init(event)
        logger.info("ClientProxy init")
    }
}