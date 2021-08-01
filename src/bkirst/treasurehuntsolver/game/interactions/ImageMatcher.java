package bkirst.treasurehuntsolver.game.interactions;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.Robot;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

public class ImageMatcher {
    private static final int GRID_SQUARE_SIZE = 75;
    public static List<Point> match(BufferedImage subimage, BufferedImage image) {
        List<Point> toReturn = new ArrayList<>();
        int maxHeight = image.getHeight() - subimage.getHeight();
        for (int i = image.getWidth()/2; i <= image.getWidth() - subimage.getWidth(); i++) {
            nextPixel:
            for (int j = image.getHeight()/4; j <= maxHeight; j++) {
                if (isUnique(new Point(i, j), toReturn))
                {
                    int matchCount = 0;
                    for (int ii = 0; ii < subimage.getWidth(); ii++) {
                        for (int jj = 0; jj < subimage.getHeight(); jj++) {
                            if (toReturn.size() == 1)
                                maxHeight = Math.min(image.getHeight() - subimage.getHeight(), j + 1024);
                            if (subimage.getRGB(ii, jj) == image.getRGB(i + ii, j + jj))
                                matchCount++;
                            if (matchCount > (subimage.getWidth() * subimage.getHeight() * .05))
                            {
                                toReturn.add(new Point(i, j));
                                j += 40;
                                continue nextPixel;
                            }
                            if (ii * jj + jj > 25 && matchCount < (ii * jj + jj) * .05 )
                                continue nextPixel;
                        }
                    }
                }
            }
        }
        return toReturn;
    }

    /*
     * Checks if the provided point is far enough away from previous matches to possibly be unique
     */
    public static boolean isUnique (Point point, List<Point> uniquePoints)
    {
        double minDistance = Double.MAX_VALUE;
        for (Point uniquePoint : uniquePoints)
        {
            double distance = Math.abs(point.getX() - uniquePoint.getX()) + Math.abs(point.getY() - uniquePoint.getY());
            if (distance < minDistance)
                minDistance = distance;
        }
        return (minDistance > GRID_SQUARE_SIZE);
    }


    public static BufferedImage screenshot() throws AWTException {
        Robot r = new Robot();
        // take a full screenshot
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage capture = r.createScreenCapture(screenRect);
        return capture;
    }
}