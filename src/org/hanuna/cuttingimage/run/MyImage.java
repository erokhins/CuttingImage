package org.hanuna.cuttingimage.run;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

/**
 * @author: erokhins
 */
public class MyImage {
    private final String OPEN_PATH = "img/in/";
    private final String SAVE_PATH = "img/out/";
    private final int[][] imageBlue;

    private final String imageName;
    private final BufferedImage image;
    private final ColorModel colorModel;
    private final WritableRaster raster;


    private void writeJpeg(BufferedImage image, String destFile, float quality) {
        ImageWriter writer = null;
        FileImageOutputStream output = null;
        try {
            writer = ImageIO.getImageWritersByFormatName("jpeg").next();
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality);
            output = new FileImageOutputStream(new File(destFile));
            writer.setOutput(output);
            IIOImage iioImage = new IIOImage(image, null, null);
            writer.write(null, iioImage, param);
        } catch (IOException ex) {
            System.out.println(ex);
        }
             }

    private int buildRGB(int r, int g, int b) {
        return (r << 16) | (g << 8) | (b << 0);
    }

    public MyImage(String imageName) throws IOException {
        this.imageName = imageName;
        image = ImageIO.read(new File(OPEN_PATH + imageName));
        colorModel = image.getColorModel();
        raster = image.getRaster();

        imageBlue = new int[image.getWidth()][image.getHeight()];
        initBlue();
    }

    private void initBlue() {
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                imageBlue[i][j] = colorModel.getBlue(raster.getDataElements(i, j, null));
            }
        }
    }





    public void writeImage() {
        writeJpeg(image, SAVE_PATH + imageName, .90f);
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public int getRed(int x, int y) {
        return colorModel.getRed(raster.getDataElements(x, y, null));
    }

    public int getGreen(int x, int y) {
        return colorModel.getGreen(raster.getDataElements(x, y, null));
    }

    public int getBlue(int x, int y) {
        return imageBlue[x][y];
    }

    public void setPixel(int x, int y, int r, int g, int b) {
        image.setRGB(x, y, buildRGB(r, g, b));
    }

    public int getAverage(int x, int y) {
        return (getRed(x, y) + getGreen(x, y) + getBlue(x, y)) / 3;
    }
}
