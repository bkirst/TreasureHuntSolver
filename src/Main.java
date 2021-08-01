import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
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
        images.add(ImageIO.read(new File("C:/Users/DoritoLoco/Pictures/gems/copper.bmp")));
        images.add(ImageIO.read(new File("C:/Users/DoritoLoco/Pictures/gems/silver.bmp")));
        images.add(ImageIO.read(new File("C:/Users/DoritoLoco/Pictures/gems/gold.bmp")));
        images.add(ImageIO.read(new File("C:/Users/DoritoLoco/Pictures/gems/bag.bmp")));
        images.add(ImageIO.read(new File("C:/Users/DoritoLoco/Pictures/gems/chest.bmp")));
        images.add(ImageIO.read(new File("C:/Users/DoritoLoco/Pictures/gems/greenchest.bmp")));
        images.add(ImageIO.read(new File("C:/Users/DoritoLoco/Pictures/gems/redchest.bmp")));
        images.add(ImageIO.read(new File("C:/Users/DoritoLoco/Pictures/gems/vault.bmp")));
        BufferedImage map = ImageIO.read(new File("C:/Users/DoritoLoco/Pictures/gems/map.png"));
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
//            for (Point point : matchingPoints) {
//                currentScreen.setRGB((int) point.getX(), (int) point.getY(), Color.red.getRGB());
//                currentScreen.setRGB((int) point.getX() + 1, (int) point.getY(), Color.red.getRGB());
//                currentScreen.setRGB((int) point.getX(), (int) point.getY() + 1, Color.red.getRGB());
//                currentScreen.setRGB((int) point.getX(), (int) point.getY(), Color.red.getRGB());
//                currentScreen.setRGB((int) point.getX() + 2, (int) point.getY(), Color.red.getRGB());
//                currentScreen.setRGB((int) point.getX(), (int) point.getY() + 2, Color.red.getRGB());
//                currentScreen.setRGB((int) point.getX(), (int) point.getY(), Color.red.getRGB());
//                currentScreen.setRGB((int) point.getX() + 3, (int) point.getY(), Color.red.getRGB());
//                currentScreen.setRGB((int) point.getX(), (int) point.getY() + 3, Color.red.getRGB());
//                currentScreen.setRGB((int) point.getX(), (int) point.getY(), Color.red.getRGB());
//                currentScreen.setRGB((int) point.getX() + 4, (int) point.getY(), Color.red.getRGB());
//                currentScreen.setRGB((int) point.getX(), (int) point.getY() + 4, Color.red.getRGB());
//            }
                totalDetected += matchingPoints.size();
                pointMap.put(thread.getGemImage(), matchingPoints);
            }
            if (totalDetected != 64) {
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
            System.out.println("Optimal Move: Swap " + move.getGem1Position() + " and " + move.getGem2Position() + " to clear " + move.getNumCleared() + " gems.");
            System.out.println("Executing move");
            Clicker.executeMove(move);


//            try {
//                ImageIO.write(currentScreen, "bmp", new File("C:/Users/DoritoLoco/Pictures/gems/debug.bmp"));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            Thread.sleep(2500);
        }
    }
}

