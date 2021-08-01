package bkirst.treasurehuntsolver.game.interactions;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class ImageProcessThread extends Thread {
    List<Point> points;
    BufferedImage screenshot;
    BufferedImage gemImage;
    public ImageProcessThread(BufferedImage screenshot, BufferedImage gemImage)
    {
        this.screenshot = screenshot;
        this.gemImage = gemImage;
    }

    @Override
    public  void run()
    {
        points = ImageMatcher.match(gemImage, screenshot);
    }

    public List<Point> getPoints() {
        return points;
    }

    public BufferedImage getGemImage() {
        return gemImage;
    }
}
