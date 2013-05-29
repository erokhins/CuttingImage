package org.hanuna.cuttingimage.run;

import java.io.IOException;
import java.io.File
import javax.imageio.ImageIO
import org.hanuna.cuttingimage.lib.BufImage
import org.hanuna.cuttingimage.lib.LineSelector
import org.hanuna.cuttingimage.lib.LineSelector1
import javax.imageio.IIOImage
import javax.imageio.stream.FileImageOutputStream
import javax.imageio.ImageWriteParam
import javax.imageio.ImageWriter
import java.awt.image.BufferedImage

/**
 * @author: erokhins
 */

private fun writeJpeg(image : BufferedImage, destFile : String, quality : Float) {
    val writer = ImageIO.getImageWritersByFormatName("jpeg").next()
    val param = writer.getDefaultWriteParam()
    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT)
    param.setCompressionQuality(quality)

    val output = FileImageOutputStream(File(destFile))
    writer.setOutput(output)
    val iioImage = IIOImage(image, null, null)
    writer.write(null, iioImage, param)
}

fun main(args: Array<String>) {
    val inName = args[0]
    val outName = args[1]

    val bufferedImage = ImageIO.read(File(inName))!!
    val image = BufImage(bufferedImage)
    val crop = LineSelector1(image).getCrop()

    val resultImage =  image.cropImage(crop)
    writeJpeg(resultImage, outName, .90)
}
