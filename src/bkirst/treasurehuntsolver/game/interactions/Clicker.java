package bkirst.treasurehuntsolver.game.interactions;

import bkirst.treasurehuntsolver.game.interactions.ImageMatcher;
import bkirst.treasurehuntsolver.model.Move;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.util.List;

public class Clicker {
    public static void executeMove(Move move) {
        click(move.getGem1().getScreenX(), move.getGem1().getScreenY());

        try {
            Thread.sleep((long) Math.random() * 500 + 200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        click(move.getGem2().getScreenX(), move.getGem2().getScreenY());

    }

    public static boolean clickImage(BufferedImage image) {
        BufferedImage currentScreen = null;
        try {
            currentScreen = ImageMatcher.screenshot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        List<Point> points = ImageMatcher.match(image, currentScreen);
        if (points.isEmpty())
        {
            System.out.println("Can't find image to click. Returning.");
            return false;
        }
        click((int) points.get(points.size()-1).getX(), (int) points.get(points.size()-1).getY() + 5);
        return true;
    }

    private static void click(int x, int y) {
        try {
            Robot bot = new Robot();
            bot.mouseMove(x, y);
            Thread.sleep((long) Math.random() * 500 + 100);
            bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            Thread.sleep((long) Math.random() * 500 + 50);
            bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
}
