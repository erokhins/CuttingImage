package org.hanuna.cuttingimage.run;

import java.io.IOException;
/**
 * @author: erokhins
 */
public class Main {

    public static void main(String[] args) throws IOException {
        for (int i = 1; i < 24; i++) {
            MyImage image = new MyImage(i + ".jpg");
            new CoolSelector(image).run();
            System.out.print("end " + i);
        }
    }
}
