package ad.blocktest.blocks

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.Item
import net.minecraftforge.client.model.ModelLoader

open class BlockBase(blockMaterialIn: Material): Block(blockMaterialIn) {
    open fun registerItemModel() {
        // Should only be called on the client from
        ModelLoader.setCustomModelResourceLocation(
                Item.getItemFromBlock(this),
                0,
                ModelResourceLocation(this.registryName!!, "inventory")
        )
    }
}