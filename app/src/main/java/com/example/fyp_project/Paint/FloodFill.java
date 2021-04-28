package com.example.fyp_project.Paint;

import android.graphics.Bitmap;
import android.graphics.Point;

import java.util.LinkedList;
import java.util.Queue;

public class FloodFill {

    public static void floodFill(Bitmap bitmap, Point point, int targetColour, int newColour) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (targetColour != newColour) {
            Queue<Point> queue = new LinkedList<>();
            do {
                int x = point.x;
                int y = point.y;
                while (x > 0 && bitmap.getPixel(x - 1, y) == targetColour) {
                    x--;
                }

                boolean spanUp = false;
                boolean spanDown = false;

                while (x < width && bitmap.getPixel(x, y) == targetColour) {
                    bitmap.setPixel(x, y, newColour);

                    if (!spanUp && y > 0 && bitmap.getPixel(x, y - 1) == targetColour) {
                        queue.add(new Point(x, y - 1));
                        spanUp = true;
                    } else if (spanUp && y > 0 && bitmap.getPixel(x, y - 1) != targetColour) {
                        spanUp = false;
                    }

                    if (!spanDown && y < height - 1 && bitmap.getPixel(x, y + 1) == targetColour) {
                        queue.add(new Point(x, y + 1));
                        spanDown = true;
                    } else if (spanDown && y < height - 1 && bitmap.getPixel(x, y + 1) != targetColour) {
                        spanDown = false;
                    }

                    x++;
                }

            } while ((point = queue.poll()) != null);
        }
    }
}
