package ad.blocktest

import net.minecraftforge.fml.common.event.FMLInitializationEvent

class ServerProxy: CommonProxy() {
    override fun init(event: FMLInitializationEvent) {
        super.init(event)
        logger.info("ServerProxy init")
    }
}