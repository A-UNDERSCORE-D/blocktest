package ad.blocktest.util

import net.minecraft.util.math.BlockPos

private fun orderPair(a: Int, b: Int): Pair<Int, Int> = if (a > b) Pair(b, a) else Pair(a, b)

typealias Corners = Pair<BlockPos, BlockPos>


class CubeArea(sX: Int, sY: Int, sZ: Int, eX: Int, eY: Int, eZ: Int) : Iterable<BlockPos> {

    constructor(startPos: BlockPos, endPos: BlockPos) : this(startPos.x, startPos.y, startPos.z, endPos.x, endPos.y, endPos.z)
    constructor(centerPos: BlockPos, size: Int) : this(centerPos.add(size, size, size), centerPos.add(-size, -size, -size))

    val x = orderPair(sX, eX)
    var y = orderPair(sY, eY)
    val z = orderPair(sZ, eZ)

    fun getCorners(): Corners {
        return Corners(
                BlockPos(x.first, y.first, z.first),
                BlockPos(x.second, y.second, z.second)
        )
    }

    fun pointNWB() = BlockPos(x.second, y.second, z.second)
    fun pointNWT() = BlockPos(x.second, y.first, z.second)

    fun pointNEB() = BlockPos(x.first, y.second, z.second)
    fun pointNET() = BlockPos(x.first, y.first, z.second)

    fun pointSWB() = BlockPos(x.second, y.second, z.first)
    fun pointSWT() = BlockPos(x.second, y.first, z.first)

    fun pointSEB() = BlockPos(x.first, y.second, z.first)
    fun pointSET() = BlockPos(x.first, y.first, z.first)

    fun getPoints(): Array<BlockPos> {
        if (!isYPlane()) {
            return arrayOf(
                    pointNWB(),
                    pointNWT(),
                    pointNEB(),
                    pointNET(),
                    pointSWB(),
                    pointSWT(),
                    pointSEB(),
                    pointSET()
            )
        } else {
            return arrayOf(
                    pointNWB(),
                    pointNEB(),
                    pointSEB(),
                    pointSWB()
            )
        }
    }
    
    fun getEdges(): Array<CubeArea> {
        if (!isYPlane()) {
            return arrayOf(
                    CubeArea(pointNWB(), pointNEB()),   // Lower rectangle
                    CubeArea(pointSWB(), pointSEB()),
                    CubeArea(pointSWB(), pointNWB()),
                    CubeArea(pointSEB(), pointNEB()),

                    CubeArea(pointNWT(), pointNET()),   // Upper rectangle
                    CubeArea(pointSWT(), pointSET()),
                    CubeArea(pointSWT(), pointNWT()),
                    CubeArea(pointSET(), pointNET()),

                    CubeArea(pointNWB(), pointNWT()),   // Connecting pillars
                    CubeArea(pointNEB(), pointNET()),
                    CubeArea(pointSWB(), pointSWT()),
                    CubeArea(pointSEB(), pointSET())

            )
        }
        else {
            return arrayOf(
                    CubeArea(pointNWB(), pointNEB()),   // Lower rectangle
                    CubeArea(pointSWB(), pointSEB()),
                    CubeArea(pointSWB(), pointNWB()),
                    CubeArea(pointSEB(), pointNEB())
            )
        }
    }



    override operator fun iterator(): Iterator<BlockPos> {
        return object : Iterator<BlockPos> {
            var curX = x.first - 1
            var curY = y.first
            var curZ = z.first

            override fun next(): BlockPos {
                curX++
                if (curX > x.second) {
                    curX = x.first
                    curZ++
                }
                if (curZ > z.second) {
                    curZ = z.first
                    curY++
                }
                return BlockPos(curX, curY, curZ)
            }

            override fun hasNext(): Boolean {
                return !(curX == x.second && curY == y.second && curZ == z.second)
            }
        }
    }

    override fun toString(): String {
        return "CubeArea: ${getCorners()}"
    }

    fun toPlaneAt(Y: Int): CubeArea {
        y = Pair(Y, Y)
        return this
    }

    fun isYPlane(): Boolean {
        return y.first == y.second
    }
}
