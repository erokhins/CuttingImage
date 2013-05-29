package org.hanuna.cuttingimage.lib

import java.awt.image.BufferedImage
import util.MutableMatrix
import util.traverseLines
import util.MutableMatrixInt

/**
 * @author: erokhins 
 */

trait Image {
    val xSize: Int
    val ySize: Int
    val red: MutableMatrix<Int>
    val green: MutableMatrix<Int>
    val blue: MutableMatrix<Int>

    fun set(x: Int, y: Int, r: Int, g: Int, b: Int)
}


class BufImage(val bufferedImage: BufferedImage): Image {
    override val xSize: Int = bufferedImage.getWidth()
    override val ySize: Int = bufferedImage.getHeight()

    override val red: MutableMatrix<Int> = MutableMatrixInt(bufferedImage.getWidth(), bufferedImage.getHeight(), {
        (x,y) ->
        (bufferedImage.getRGB(x, y) shr 16) and 255
    })

    override val green: MutableMatrix<Int> = MutableMatrixInt(bufferedImage.getWidth(), bufferedImage.getHeight(), {
        (x,y) ->
        (bufferedImage.getRGB(x, y) shr 8) and 255
    })

    override val blue: MutableMatrix<Int> = MutableMatrixInt(bufferedImage.getWidth(), bufferedImage.getHeight(), {
        (x,y) ->
        (bufferedImage.getRGB(x, y) shr 0) and 255
    })

    override fun set(x: Int, y: Int, r: Int, g: Int, b: Int) {
        red[x, y] = r
        green[x, y] = g
        blue[x, y] = b
    }

    // 0..4
    public fun cropImage(crop: Crop): BufferedImage {
        val width = crop.xLineCrop.end - crop.xLineCrop.start + 1
        val height = crop.yLineCrop.end - crop.yLineCrop.start + 1

        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        for (i in 0..width-1) {
            for (j in 0..height-1) {
                image.setRGB(i, j, bufferedImage.getRGB(i + crop.xLineCrop.start, j + crop.yLineCrop.start))
            }
        }
        return image
    }

    private fun buildRGB(x: Int, y: Int) : Int {
        return (red[x,y] shl 16) or (green[x,y] shl 8) or (blue[x,y] shl 0)
    }

    public fun writeChanges(): BufferedImage {
        for (i in 0..xSize-1) {
            for (j in 0..ySize-1) {
                bufferedImage.setRGB(i, j, buildRGB(i, j))
            }
        }
        return bufferedImage
    }
}

