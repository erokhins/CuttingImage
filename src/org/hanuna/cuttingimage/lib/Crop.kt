package org.hanuna.cuttingimage.lib

import util.Matrix
import util.MutableMatrix
import util.MutableMatrixInt

/**
 * @author: erokhins 
 */

class LineCrop(val start: Int, val end: Int, val middle: Int? = null)

class Crop(val xLineCrop: LineCrop, val yLineCrop: LineCrop)


trait LineSelector {
    fun getCrop(): Crop
}

class LineSelector1(val image: BufImage): LineSelector {
    val R_POINT = 3
    val R_LINE = 5
    val D_MIN = 4

    val indent = 2 * R_POINT + 2

    val mask : MutableMatrix<Int> = MutableMatrixInt(image.xSize, image.ySize, {(a: Int, b: Int) -> 0 })


    private fun average(x : Int, y : Int, r : Int) : Int {
        var s : Int = 0
        for (i in -r..r) {
            for (j in -r..r) {
                s += image.blue[x+i, y+j]
            }
        }
        return s / ((2 * r + 1) * (2 * r + 1))
    }

    private fun isCoolPixel(x : Int, y : Int) : Int {
        val r = R_POINT
        var controlCount : Int = average(x - r, y, r) + average(x, y - r, r) - 2 * average(x, y, r)
        return if (Math.abs(controlCount) > 10) Math.abs(controlCount) else 0
    }

    public fun coolHorizontal(y : Int) : Boolean {
        var sum : Int = 0
        var r : Int = R_LINE
        for (i in 0..image.xSize - 1) {
            for (j in y - r..y + r) {
                if (mask[i, j] > 0)
                {
                    sum++
                }

            }
        }
        return sum > 2000
    }
    public fun coolVertical(x : Int) : Boolean {
        var sum : Int = 0
        var r : Int = R_LINE
        for (j in 0..image.ySize - 1) {
            for (i in x - r..x + r) {
                if (mask[i, j] > 0)
                {
                    sum++
                }

            }
        }
        return sum > 2000
    }

    private fun getLineCrop(coolLines: IntArray): LineCrop {
        var min = -1
        val size = coolLines.size
        var max = size
        fun isCoolPoint(i: Int): Boolean {
            val a = Math.max(0, i - D_MIN);
            val b = Math.min(size - 1, i + D_MIN)
            var sum = 0;
            for (j in a..b) {
                if (coolLines[j] > 0) {
                    sum++
                }
            }
            return sum > D_MIN
        }

        for(i in 0..size - 1) {
            if (min == -1 && coolLines[i] > 0) {
                if (isCoolPoint(i)) {
                    min = i;
                }
            }

            val oi = size - i - 1
            if (max == size && coolLines[oi] > 0) {
                if (isCoolPoint(oi)) {
                    max = oi
                }
            }
        }
        if (min == -1) {
            min = 0
        }
        if (max == size) {
            max = size - 1
        }
        return LineCrop(min, max);
    }

    override fun getCrop(): Crop {
        val padding : Int = 20
        for (i in padding..image.xSize - padding - 1) {
            for (j in padding..image.ySize - padding - 1) {
                mask[i, j] = isCoolPixel(i, j)
            }
        }



        var coolLines = IntArray(image.ySize)
        for (j in padding..image.ySize - padding - 1) {
            if (coolHorizontal(j))
            {
                coolLines[j] = 1;
                image.set(0, j, 255, 0, 0)
            }
        }
        val hCrop = getLineCrop(coolLines)

        coolLines = IntArray(image.xSize)
        for (i in padding..image.xSize - padding - 1) {
            if (coolVertical(i))
            {
                coolLines[i] = 1;
                image.set(i, 0, 255, 0, 0)
            }
        }
        val vCrop = getLineCrop(coolLines)

        return Crop(vCrop, hCrop)
    }
}

