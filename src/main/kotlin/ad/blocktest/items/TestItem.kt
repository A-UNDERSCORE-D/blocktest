package ad.blocktest.items

import ad.blocktest.BlockTest
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation

object TestItem: Item() {
    init {
        unlocalizedName = BlockTest.modId + ".test_item"
        registryName = ResourceLocation(BlockTest.modId, "test_item")
        creativeTab = CreativeTabs.MISC
    }
}