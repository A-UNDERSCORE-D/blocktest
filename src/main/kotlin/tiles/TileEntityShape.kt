package ad.blocktest.tiles

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity

class TileEntityShape: TileEntity() {
    var shapeIdx = 0
        private set

    private var maxIdx: Int = -1

    fun setmaxIdx(value: Int) {
        maxIdx = value
        markDirty()
    }
    fun increment() {
        shapeIdx++
        if (shapeIdx > maxIdx) {
            shapeIdx = 0
        }
        markDirty()
    }

    override fun readFromNBT(compound: NBTTagCompound) {
        super.readFromNBT(compound)
        shapeIdx = compound.getInteger("shapeIdx")
        maxIdx = compound.getInteger("maxIdx")
    }

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        super.writeToNBT(compound)
        compound.setInteger("shapeIdx", shapeIdx)
        compound.setInteger("maxIdx", maxIdx)
        return compound
    }
}