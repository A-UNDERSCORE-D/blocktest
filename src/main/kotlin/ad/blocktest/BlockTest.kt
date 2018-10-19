package ad.blocktest

import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import org.apache.logging.log4j.Logger

@Suppress("MemberVisibilityCanBePrivate")
@Mod(modid = BlockTest.modId, version = BlockTest.modVersion, name = BlockTest.modName, modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter", useMetadata = true)
object BlockTest {
    const val modId = "blocktest"
    const val modVersion = "@VERSION@"
    const val modName = "Test Mod for adding blocks with kotlin"

    @SidedProxy(clientSide = "ad.blocktest.ClientProxy", serverSide = "ad.blocktest.ServerProxy")
    lateinit var proxy: CommonProxy
    lateinit var logger: Logger

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        logger = event.modLog
        proxy.logger = logger
        proxy.preInit(event)
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        proxy.init(event)
    }
}
