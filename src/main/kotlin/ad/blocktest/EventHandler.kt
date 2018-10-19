package ad.blocktest

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Mod.EventBusSubscriber
object EventHandler {
    @JvmStatic
    @SubscribeEvent
    fun onBlockRegister(event: RegistryEvent.Register<Block>) {
        BlockTest.proxy.initBlocks(event.registry)
    }

    @JvmStatic
    @SubscribeEvent
    fun onItemRegister(event: RegistryEvent.Register<Item>) {
        BlockTest.proxy.initItems(event.registry)
    }

    @JvmStatic
    @SubscribeEvent
    @Suppress("UNUSED_PARAMETER")
    fun onModelRegister(event: ModelRegistryEvent) {
        BlockTest.proxy.registerModels()
    }
}