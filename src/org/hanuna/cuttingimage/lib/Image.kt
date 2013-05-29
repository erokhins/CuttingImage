package org.hanuna.cuttingimage.lib

import java.awt.image.BufferedImage
import util.MutableMatrix
import util.MutableMatrixImpl
import util.traverseLines

/**
 * @author: erokhins 
 */

trait Pixel{
    val r: Int
    val g: Int
    val b: Int
}

trait Image {
    val xSize: Int
    val ySize: Int

    fun get(x: Int, y: Int): Pixel
    fun set(x: Int, y: Int, pixel: Pixel)
}

class SimplePixel(override val r: Int, override val g: Int, override val b: Int) : Pixel

class BufImage(val bufferedImage: BufferedImage): Image {
    override val xSize: Int = bufferedImage.getWidth()
    override val ySize: Int = bufferedImage.getHeight()
    val initFun = {
        (x: Int, y:Int) ->
        buildPixel(bufferedImage.getRGB(x, y))
        SimplePixel(0,0,0)
    }

    val pixels: MutableMatrix<Pixel> = MutableMatrixImpl(bufferedImage.getWidth(), bufferedImage.getHeight(), initFun)

    override fun get(x: Int, y: Int): Pixel {
        return pixels[x,y];
    }
    override fun set(x: Int, y: Int, pixel: Pixel) {
        pixels[x,y] = pixel;
    }

    private fun buildRGB(p: Pixel) : Int {
        return (p.r shl 16) or (p.g shl 8) or (p.b shl 0)
    }

    private fun buildPixel(rgb: Int): Pixel {
        return SimplePixel((rgb shl 16) and 255, (rgb shl 8) and 255, rgb and 255)
    }

    private fun writeChanges(): BufferedImage {
        pixels.traverseLines {
            (x: Int, y: Int, p: Pixel) ->
            bufferedImage.setRGB(x, y, buildRGB(p))
        }
        return bufferedImage
    }
}

