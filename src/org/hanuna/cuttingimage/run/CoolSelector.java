package org.hanuna.cuttingimage.run;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author: erokhins
 */
public class CoolSelector {
    private final MyImage image;
    private final int[][] mask;


    public CoolSelector(MyImage image) {
        this.image = image;
        mask = new int[image.getWidth()][image.getHeight()];
    }

    private int average(int x, int y, int r) {
        int s = 0;
        for (int i = -r; i < r + 1; i++) {
            for (int j = -r; j < r + 1; j++) {
                s += image.getAverage(x + i, y + j);
            }
        }
        return s / ((2 * r + 1) * (2 * r + 1));
    }

    private int isCoolPixel(int x, int y) {
        int r = 4;
        int controlCount = average(x - r, y, r) + average(x, y - r, r)  - 2 * average(x, y, r);

        return Math.abs(controlCount) > 10 ? Math.abs(controlCount) : 0;
    }

    public void run() {
        int padding = 20;
        for (int i = padding; i < image.getWidth() - padding; i++) {
            for (int j = padding; j < image.getHeight() - padding; j++) {
                mask[i][j] = isCoolPixel(i, j);
            }
        }
        for (int i = padding; i < image.getWidth() - padding; i++) {
            for (int j = padding; j < image.getHeight() - padding; j++) {
                if (mask[i][j] > 0) {
                    image.setPixel(i, j, mask[i][j] * 10, 0, 0);
                }
            }
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
