package ad.blocktest.items

import net.minecraft.block.Block
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.World

class ItemBlockBase(block: Block) : ItemBlock(block) {
    override fun addInformation(stack: ItemStack, worldIn: World?, tooltip: MutableList<String>, flagIn: ITooltipFlag) {
        super.addInformation(stack, worldIn, tooltip, flagIn)
        val desc: String = TextComponentTranslation(this.block.unlocalizedName + ".description").formattedText
        tooltip.add(desc)
    }
}