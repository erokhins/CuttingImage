package org.hanuna.cuttingimage.run;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: erokhins
 */
public class CoolSelector {
    private final MyImage image;
    private final Set<Point> coolPixels = new HashSet<Point>();


    public CoolSelector(MyImage image) {
        this.image = image;
    }

    private boolean isCoolPixel(int x, int y) {
        int controlCount = image.getRed(x-1, y) + image.getRed(x, y-1) - 2 * image.getRed(x, y);

        return Math.abs(controlCount) > 10;
    }

    public void run() {
        for (int i = 10; i < image.getWidth() - 10; i++) {
            for (int j = 10; j < image.getHeight() - 10; j++) {
                if (isCoolPixel(i, j)){
                    coolPixels.add(new Point(i, j));
                }
            }
        }
        for (Point point : coolPixels) {
            image.setPixel(point.x, point.y, 255, 0, 0);
        }
        image.writeImage();
    }

    private class Point {
        int x, y;
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
