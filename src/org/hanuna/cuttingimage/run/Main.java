package org.hanuna.cuttingimage.run;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.awt.image.ColorModel.*;
/**
 * @author: erokhins
 */
public class Main {

    public static void main(String[] args) throws IOException {
        MyImage image = new MyImage("1.jpg");
        new CoolSelector(image).run();
    }
}
