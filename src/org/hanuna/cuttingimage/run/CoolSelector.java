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
                s += image.getBlue(x + i, y + j);
            }
        }
        return s / ((2 * r + 1) * (2 * r + 1));
    }

    private int isCoolPixel(int x, int y) {
        int r = 3;
        int controlCount = average(x - r, y, r) + average(x, y - r, r)  - 2 * average(x, y, r);

        return Math.abs(controlCount) > 10 ? Math.abs(controlCount) : 0;
    }

    public boolean coolHorizontal(int y) {
        int sum = 0;
        int r = 5;

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = y-r; j <= y+r; j++) {
                if (mask[i][j] > 0) {
                    sum++;
                }
            }
        }
        return sum > 4000;
    }

    public boolean coolVertical(int x) {
        int sum = 0;
        int r = 5;

        for (int j = 0; j < image.getHeight(); j++) {
            for (int i = x-r; i <= x+r; i++) {
                if (mask[i][j] > 0) {
                    sum++;
                }
            }
        }
        return sum > 4000;
    }

    public void run() {
        int padding = 20;
        for (int i = padding; i < image.getWidth() - padding; i++) {
            for (int j = padding; j < image.getHeight() - padding; j++) {
                mask[i][j] = isCoolPixel(i, j);
            }
        }
        System.out.println("mask calculate finished");

        for (int j = padding; j < image.getHeight() - padding; j++) {
            if (coolHorizontal(j)) {
                for (int i = 0; i < 100; i++) {
                    image.setPixel(i, j, 255, 0, 0);
                }
            }
        }

        for (int i = padding; i < image.getWidth() - padding; i++) {
            if (coolVertical(i)) {
                for (int j = 0; j < 100; j++) {
                    image.setPixel(i, j, 255, 0, 0);
                }
            }
        }
        /*
        for (int i = padding; i < image.getWidth() - padding; i++) {
            for (int j = padding; j < image.getHeight() - padding; j++) {
                if (mask[i][j] > 0) {
                    image.setPixel(i, j, mask[i][j] * 10, 0, 0);
                }
            }
        } */
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
