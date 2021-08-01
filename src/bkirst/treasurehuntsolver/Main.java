package bkirst.treasurehuntsolver;

import bkirst.treasurehuntsolver.game.interactions.Clicker;
import bkirst.treasurehuntsolver.game.interactions.ImageMatcher;
import bkirst.treasurehuntsolver.game.interactions.ImageProcessThread;
import bkirst.treasurehuntsolver.model.BoardSolver;
import bkirst.treasurehuntsolver.model.GameBoard;
import bkirst.treasurehuntsolver.model.Move;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
  Run Gems of War at 1280x1024 resolution and place it in the lower right of a 1440p monitor
 */
public class Main {
    public static void main(String[] args) throws Exception {
        List<BufferedImage> images = new ArrayList<>();
        List<ImageProcessThread> threads;
        Map<BufferedImage, List<Point>> pointMap = new HashMap<>();
        images.add(ImageIO.read(new File("./resources/copper.png")));
        images.add(ImageIO.read(new File("./resources/silver.png")));
        images.add(ImageIO.read(new File("./resources/gold.png")));
        images.add(ImageIO.read(new File("./resources/bag.png")));
        images.add(ImageIO.read(new File("./resources/chest.png")));
        images.add(ImageIO.read(new File("./resources/greenchest.png")));
        images.add(ImageIO.read(new File("./resources/redchest.png")));
        images.add(ImageIO.read(new File("./resources/vault.png")));
        BufferedImage map = ImageIO.read(new File("./resources/map.png"));
        int maxMatch = 5;
        int failureCount = 0;
        while (true) {
            BufferedImage currentScreen = ImageMatcher.screenshot();
            int totalDetected = 0;

            threads = new ArrayList<>();
            for (int i = 0; i < maxMatch; i++) {
                ImageProcessThread thread = new ImageProcessThread(currentScreen, images.get(i));
                thread.start();
                threads.add(thread);
            }
            boolean running = true;
            while (running) {
                running = false;
                for (Thread thread : threads) {
                    if (thread.isAlive()) {
                        running = true;
                        Thread.sleep(1000);
                        break;
                    }
                }
            }
            for (ImageProcessThread thread : threads) {
                List<Point> matchingPoints = thread.getPoints();
            for (Point point : matchingPoints) {
                currentScreen.setRGB((int) point.getX(), (int) point.getY(), Color.red.getRGB());
                currentScreen.setRGB((int) point.getX() + 1, (int) point.getY(), Color.red.getRGB());
                currentScreen.setRGB((int) point.getX(), (int) point.getY() + 1, Color.red.getRGB());
                currentScreen.setRGB((int) point.getX(), (int) point.getY(), Color.red.getRGB());
                currentScreen.setRGB((int) point.getX() + 2, (int) point.getY(), Color.red.getRGB());
                currentScreen.setRGB((int) point.getX(), (int) point.getY() + 2, Color.red.getRGB());
                currentScreen.setRGB((int) point.getX(), (int) point.getY(), Color.red.getRGB());
                currentScreen.setRGB((int) point.getX() + 3, (int) point.getY(), Color.red.getRGB());
                currentScreen.setRGB((int) point.getX(), (int) point.getY() + 3, Color.red.getRGB());
                currentScreen.setRGB((int) point.getX(), (int) point.getY(), Color.red.getRGB());
                currentScreen.setRGB((int) point.getX() + 4, (int) point.getY(), Color.red.getRGB());
                currentScreen.setRGB((int) point.getX(), (int) point.getY() + 4, Color.red.getRGB());
            }
                totalDetected += matchingPoints.size();
                pointMap.put(thread.getGemImage(), matchingPoints);
            }
            if (totalDetected != 64) {
                try {
                    ImageIO.write(currentScreen, "bmp", new File("./resources/debug.bmp"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (maxMatch != images.size()) {
                    System.out.println("Couldn't find all the images, incrementing maxMatch to " + maxMatch++);
                    continue;
                } else if (failureCount < 1) {
                    failureCount++;
                    System.out.println("FAILURE! Incorrect number of board objects detected. Trying again.");
                    continue;
                } else if (failureCount > 3) {
                    System.out.println("CATASTROPHIC FAILURE! Incorrect number of board objects detected 4 times in a row while trying to start a new game. Exiting program.");
                    break;
                } else {
                    System.out.println("FAILURE! Incorrect number of board objects detected twice. Attempting to play again");
                    Thread.sleep(10000);
                    currentScreen = ImageMatcher.screenshot();
                    boolean success = Clicker.clickImage(map);
                    if (success)
                    {
                        File outputFile = new File("C:\\Users\\DoritoLoco\\Pictures\\gems\\highscores\\" + System.currentTimeMillis() + ".png");
                        ImageIO.write(currentScreen, "png", outputFile);
                    }
                    continue;
                }
            } else {
                failureCount = 0;
                System.out.println("Identified all 64 board objects. Building game board model.");
            }

            GameBoard board = new GameBoard(images, pointMap);
            System.out.println(board);

            Move move = BoardSolver.getOptimalMove(board);
            System.out.println("Optimal bkirst.treasurehuntsolver.model.Move: Swap " + move.getGem1Position() + " and " + move.getGem2Position() + " to clear " + move.getNumCleared() + " gems.");
            System.out.println("Executing move");
            Clicker.executeMove(move);



            Thread.sleep(2500);
        }
    }
}

